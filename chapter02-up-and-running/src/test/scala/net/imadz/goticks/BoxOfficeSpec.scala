package net.imadz.goticks

import akka.actor.{ActorRef, Props, ActorSystem}
import akka.testkit.{DefaultTimeout, ImplicitSender, TestKit}
import net.imadz.goticks.BoxOffice.{EventCreated, CreateEvent}
import net.imadz.goticks.TicketSeller.{Add, Ticket}
import org.scalatest.{MustMatchers, WordSpecLike}

class BoxOfficeSpec extends TestKit(ActorSystem("testBoxOffice"))
  with WordSpecLike
  with MustMatchers
  with ImplicitSender
  with DefaultTimeout
  with StopSystemAfterAll {

  "The TicketMaster" must {

    "Create an event and get tickets from the correct Ticket Seller" in {
      import net.imadz.goticks.BoxOffice._
      import net.imadz.goticks.TicketSeller._

      val boxOffice = system.actorOf(BoxOffice.props)
      boxOffice ! CreateEvent("RHCP", 10)
      expectMsg(EventCreated)

      boxOffice ! GetTickets("RHCP", 1)
      expectMsg(Tickets("RHCP", Vector(Ticket(1))))

      boxOffice ! GetTickets("DavidBowie", 1)
      expectMsg(Tickets("DavidBowie"))
    }

    "Create a child actor when an event is created and send it a Tickets message" in {
      val boxOffice = system.actorOf(Props(
        new BoxOffice{
          override def createTicketSeller(name: String): ActorRef = testActor
        }
      ))

      val tickets = 3
      val expectedTickets = (1 to tickets).map(Ticket).toVector
      boxOffice ! CreateEvent("RHCP", tickets)
      expectMsg(Add(expectedTickets))
      expectMsg(EventCreated)
    }
  }
}