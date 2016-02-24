package net.imadz.testdriven

import akka.actor.{ActorSystem, Props}
import akka.testkit.TestKit
import org.scalatest.{MustMatchers, WordSpecLike}

/**
  * Created by Barry on 2/14/16.
  */
class FilteringActorTest extends TestKit(ActorSystem("testSystem")) with WordSpecLike with MustMatchers with StopSystemAfterAll {

  "filter out particular message" in {
    import FilteringActorProtocol._

    val props = Props(new FilteringActor(testActor, 5))
    val filteringActor = system.actorOf(props)

    filteringActor ! Event(1)
    filteringActor ! Event(2)
    filteringActor ! Event(1)
    filteringActor ! Event(3)
    filteringActor ! Event(2)
    filteringActor ! Event(4)
    filteringActor ! Event(4)
    filteringActor ! Event(5)
    filteringActor ! Event(5)
    filteringActor ! Event(6)

    val eventIds = receiveWhile() {
      case Event(id) if id <= 5 => id
    }
    eventIds must be(List(1, 2, 3, 4, 5))

    expectMsg(Event(6))
  }
}
