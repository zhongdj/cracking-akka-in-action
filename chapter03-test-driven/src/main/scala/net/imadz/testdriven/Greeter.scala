package net.imadz.testdriven

import akka.actor.{Actor, ActorLogging}
import net.imadz.testdriven.GreeterProtocol.Greeting

/**
  * Created by Barry on 2/15/16.
  */

object GreeterProtocol {

  case class Greeting(name: String)

}
import akka.actor.{ActorLogging, Actor}

case class Greeting(message: String)

class Greeter extends Actor with ActorLogging {
  def receive = {
    case Greeting(message) => log.info("Hello {}!", message) //<co id="say_hello_something"/>
  }
}