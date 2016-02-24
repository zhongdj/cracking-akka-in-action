package net.imadz.testdriven

import akka.actor.ActorRef

/**
  * Created by Barry on 2/6/16.
  */
object SilentActorProtocol {

  case class SilentMessage(message: String)

  case class GetState(receiver: ActorRef)

}
