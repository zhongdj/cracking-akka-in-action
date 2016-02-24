package net.imadz.testdriven

import akka.actor.{ActorSystem, Props}
import akka.testkit.{CallingThreadDispatcher, EventFilter, TestKit}
import com.typesafe.config.ConfigFactory
import net.imadz.testdriven.Greeter01Test._
import net.imadz.testdriven.GreeterProtocol.Greeting
import org.scalatest.{MustMatchers, WordSpecLike}

/**
  * Created by Barry on 2/15/16.
  */
class Greeter01Test extends TestKit(testSystem)
  with WordSpecLike
  with MustMatchers
  with StopSystemAfterAll {

  "The Greeter" must {
    """say Hello World! when a Greeting("World") is sent to it""" in {
      val dispatcherId = CallingThreadDispatcher.Id
      val props = Props[Greeter].withDispatcher(dispatcherId)
      val greeter = system.actorOf(props)
      EventFilter.info(message = "Hello World!", occurrences = 1)
        .intercept{
          greeter ! Greeting("World")
        }
    }
  }

}

object Greeter01Test {
  val testSystem = {
    val config = ConfigFactory.parseString(
      """akka.loggers = [ akka.testkit.TestEventListener ] """
    )
    ActorSystem("testsystem", config)
  }
}
