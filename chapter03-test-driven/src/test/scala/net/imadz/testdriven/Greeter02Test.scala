package net.imadz.testdriven

import akka.actor.{UnhandledMessage, Props, ActorRef, ActorSystem}
import akka.testkit.TestKit
import org.scalatest.{MustMatchers, WordSpecLike}

/**
  * Created by Barry on 2/15/16.
  */
class Greeter02Test extends TestKit(ActorSystem("testsystem"))
  with WordSpecLike
  with MustMatchers
  with StopSystemAfterAll {

  "The Greeter" must {
    """say Hello World! when a Greeting("World") is sent to it""" in {
      val props = Props(new Greeter02(Some(testActor)))
      val greeter = system.actorOf(props)
      greeter ! Greeting("World")
      expectMsg("Hello World!")
    }

    "say something else and see what happened" in {
      val props = Props(new Greeter02(Some(testActor)))
      val greeter = system.actorOf(props, "greeter02-2")
      system.eventStream.subscribe(testActor, classOf[UnhandledMessage])
      greeter ! "World"
      expectMsg(UnhandledMessage("World", greeter, testActor))
    }
  }

}
