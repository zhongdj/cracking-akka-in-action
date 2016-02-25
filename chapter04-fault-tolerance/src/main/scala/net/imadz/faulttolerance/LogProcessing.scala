package net.imadz.faulttolerance

/**
  * Created by Barry on 2/24/16.
  */
class LogProcessing {

}

trait FileDiscoverCapability {
  def register(actorRef : ActorRef) = ???
}

object LogProcessProtocol {
  case class LogFile(file: File)
  case class Line(logLine: String)
}

@SerialVersionUID(1L)
class DiskError(msg: String) extends Error(msg) with Serializable
@SerialVersionUID(1L)
class FileCorruptedException extends Exception with Serializable
@SerialVersionUID(1L)
class DBConnectionBrokenException extends Exception with Serializable

case class DBConn(url : String) {
  def write(line: String) = {}
}

class DBWriter(connection: DBConn) extends Actor {
  override def receive = {
    case Line(logLine) => connection.write(logLine)
  }
}
class DBWriterSupervisor(writerProps : Props) extends Actor  {

  override def supervisorStrategy = OneForOneStrategy() {
    case e : DBConnectionBrokenException => Restart
  }

  val writer = context.actorOf(writerProps)

  override def receive = {
    case l @ Line(logLine) = writer ! l
  }
}

class LogProccesor(dbWriterSupervisor : ActorRef) extends Actor {

  override def receive = {
    case LogFile(file) => Source.from(file).toLines.foreach {
      dbWriterSupervisor ! _
    }
  }
}

class LogProcSupervisor(dbWriterSupervisorProps : Props) extends Actor {

  override def supervisorStrategy = OneForOneStrategy() {
    case e: FileCorruptedException => Stop
  }

  val dbWriterSupervisor = context.actorOf(dbWriterSupervisorProps)
  val logProcessorProps = Props(new LogProcessor(dbWriterSupervisor))
  val logProcessor = context.actorOf(logProcessorProps)

  override def receive = {
    case m : LogFile(file) =>  logProcessor ! m
  }
}

class LogFileDispatcher(source: String, logProcSupervisor: ActorRef) extends Actor  with FileDiscoverCapability {

  register(self)

  override def receive = {
    case m: LogFile(file) => logProcSupervisor ! m
  }

}
class FileWatcherSupervisor(sources: Vector[String], logProcSupervisorProps: Props) extends Actor  {

  override def supervisorStrategy = OneForAllStrategy() {
    case e : DiskError => Stop
  }

  val logProcSupervisor = context.actorOf(logProcSupervisorProps)
  var fileWatchers = sources.map { source : String =>
    val fileWatcher = context.actorOf(Props(new FileWatcher(source, logProcSupervisor)))
    context.watch(fileWatcher)
    fileWatcher
  }
  override def receive = {
    case Terminated(fileWatcher) =>
      fileWatchers -= fileWatcher
      if (fileWatchers.isEmpty) self ! PoisonPill
  }
}

