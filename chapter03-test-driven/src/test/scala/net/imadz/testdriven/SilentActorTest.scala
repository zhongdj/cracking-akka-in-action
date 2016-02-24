package net.imadz.testdriven

import akka.actor.{Props, ActorSystem}
import akka.testkit.{TestActorRef, TestKit}
import net.imadz.testdriven.SilentActorProtocol.{GetState, SilentMessage}
import org.scalatest.{MustMatchers, WordSpecLike}

/**
  * Created by Barry on 2/6/16.
  */
class SilentActorTest extends TestKit(ActorSystem("testsystem")) with WordSpecLike with MustMatchers with StopSystemAfterAll {

  "A Silent Actor" must {

    "change state when it receives message, single threaded" in {

      val silentActor = TestActorRef[SilentActor]
      silentActor ! SilentMessage("whisper")
      silentActor.underlyingActor.state must (contain("whisper"))

    }

    "change state when it receives message, multiple threaded" in {
      val silentActor = system.actorOf(Props(new SilentActor), "silent_actor")
      silentActor ! SilentMessage("whisper1")
      silentActor ! SilentMessage("whisper2")
      silentActor ! GetState(testActor)

      expectMsg(Vector("whisper1", "whisper2"))
    }
  }


}
