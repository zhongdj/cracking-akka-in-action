package net.imadz.faulttolerance

import akka.actor._
import akka.actor.SupervisorStrategy._

/**
  * Created by Barry on 2/24/16.
  */
class ActorLifecycleHooks extends Actor with ActorLogging {

  override def supervisorStrategy = OneForOneStrategy() {
    case e : IllegalStateException => Restart
  }

  override def receive = {
    case "restart" => throw new IllegalStateException
    case "stop" => self ! PoisonPill
    case msg => ()
  }

  override def preStart = {
    super.preStart
    log.info("preStart")
  }

  override def postStop = {
    log.info("postStop")
    super.postStop
  }

  override def preRestart(reason: Throwable, message: Option[Any]) = {
    log.info("preRestart")
    super.preRestart(reason, message)
  }

  override def postRestart(reason: Throwable) = {
    log.info("postRestart")
    super.postRestart(reason)
  }

}

object ActorLifecycleHooks {
  def props = Props(new ActorLifecycleHooks)
}
