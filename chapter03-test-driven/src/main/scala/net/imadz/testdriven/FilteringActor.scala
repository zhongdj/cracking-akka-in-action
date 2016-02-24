package net.imadz.testdriven

import akka.actor.{ActorRef, Actor}
import akka.actor.Actor.Receive

/**
  * Created by Barry on 2/14/16.
  */
class FilteringActor(nextActor: ActorRef, bufferSize: Int) extends Actor {

  var recentMessages = Set.empty[Long]

  import FilteringActorProtocol._

  override def receive: Receive = {
    case e @ Event(id) if !recentMessages.contains(id) =>
      recentMessages += id
      nextActor ! e
      if (recentMessages.size > bufferSize) recentMessages = recentMessages.tail
  }

}

object FilteringActorProtocol {

  case class Event(id: Long)

}