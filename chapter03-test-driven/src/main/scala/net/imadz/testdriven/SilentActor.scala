package net.imadz.testdriven

import akka.actor.Actor
import net.imadz.testdriven.SilentActorProtocol.{GetState, SilentMessage}

/**
  * Created by Barry on 2/6/16.
  */
class SilentActor extends Actor {
  var internalState = Vector.empty[String]

  def state = internalState

  override def receive: Receive = {
    case SilentMessage(msg) => internalState = internalState :+ msg
    case GetState(receiver) => receiver ! internalState
  }
}
