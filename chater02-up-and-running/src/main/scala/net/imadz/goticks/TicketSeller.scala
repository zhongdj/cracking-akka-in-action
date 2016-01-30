package net.imadz.goticks

import akka.actor.{Actor, PoisonPill, Props}

/**
  * Created by Barry on 1/30/16.
  */
object TicketSeller {
  def props(name: String): Props = Props(new TicketSeller(name))

  case class Add(tickets: Vector[Ticket])

  case class Buy(tickets: Int)

  case class Ticket(id: Int)

  case class Tickets(event: String, entries: Vector[Ticket] = Vector.empty[Ticket])

  case object GetEvent

  case object Cancel

}

class TicketSeller(event: String) extends Actor {

  import TicketSeller._

  var tickets = Vector.empty[Ticket]

  override def receive: Receive = {
    case Add(newTickets) => tickets = tickets ++ newTickets
    case Buy(nrOfTickets) =>
      val entries = tickets.take(nrOfTickets)
      if (entries.size >= nrOfTickets) {
        sender() ! Tickets(event, entries)
        tickets = tickets.drop(nrOfTickets)
      } else sender() ! Tickets(event)
    case GetEvent => sender() ! Some(BoxOffice.Event(event, tickets.size))
    case Cancel =>
      sender() ! Some(BoxOffice.Event(event, tickets.size))
      self ! PoisonPill
  }
}
