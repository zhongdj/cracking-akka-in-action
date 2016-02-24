package net.imadz.testdriven

import akka.actor.{Props, ActorSystem}
import akka.testkit.TestKit
import org.scalatest.{MustMatchers, WordSpecLike}

/**
  * Created by Barry on 2/14/16.
  */
class SendingActorTest extends TestKit(ActorSystem("test")) with WordSpecLike with MustMatchers with StopSystemAfterAll {

  "A Sending Actor" must {

    "send a message to an actor when it has finished" in {

      import Kiosk01Protocol._

      val prop = Props(new Kiosk01(testActor))
      val sendingActor = system.actorOf(prop)
      sendingActor ! Game("Lakes vs Bulls", Vector(Ticket(1), Ticket(2), Ticket(3)))
      expectMsgPF(){
        case game @ Game(_, tickets) => tickets.size must be(2)
      }

    }
  }
}
