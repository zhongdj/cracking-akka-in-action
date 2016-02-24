package net.imadz.testdriven

import akka.actor.Actor.Receive
import akka.actor.{Actor, ActorRef}

/**
  * Created by Barry on 2/14/16.
  */
object Kiosk01Protocol {

  case class Ticket(seat: Int)

  case class Game(name: String, tickets: Seq[Ticket])

}


class Kiosk01(nextKiosk: ActorRef) extends Actor {

  import Kiosk01Protocol._

  override def receive: Receive = {
    case game @ Game(_, tickets) =>
      nextKiosk ! game.copy(tickets = tickets.tail)
  }
}