package net.imadz.testdriven

import akka.actor.{Actor, ActorLogging, ActorRef}

/**
  * Created by Barry on 2/15/16.
  */
class Greeter02(listener : Option[ActorRef]) extends Actor with ActorLogging {
  override def receive: Receive = {
    case Greeting(name) =>
      val message = "Hello " + name + "!"

      log info message

      listener foreach (_ ! message)
  }
}
