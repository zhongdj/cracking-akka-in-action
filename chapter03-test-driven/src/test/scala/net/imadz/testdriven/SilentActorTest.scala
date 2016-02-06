package net.imadz.testdriven

import akka.actor.ActorSystem
import akka.testkit.TestKit
import org.scalatest.{MustMatchers, WordSpecLike}

/**
  * Created by Barry on 2/6/16.
  */
class SilentActorTest extends TestKit(ActorSystem("testsystem")) with WordSpecLike with MustMatchers with StopSystemAfterAll {

  "A Silent Actor" must {
    "change state when it receives message, single threaded" in {
      fail("no implementation")
    }

    "change state when it receives message, multiple threaded" in {
      fail("no implementation")
    }
  }


}
