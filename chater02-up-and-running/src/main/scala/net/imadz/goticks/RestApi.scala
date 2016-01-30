package net.imadz.goticks

import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import spray.http.StatusCodes
import spray.httpx.SprayJsonSupport._
import spray.routing._

import scala.concurrent.ExecutionContext

/**
  * Created by Barry on 1/30/16.
  */
class RestApi(timeout: Timeout) extends HttpServiceActor with RestRoutes {

  implicit val requestTimeout = timeout

  implicit def executionContext = context.dispatcher

  def createBoxOffice = context.actorOf(BoxOffice.props, BoxOffice.name)

  override def receive: Receive = runRoute(routes)
}


trait RestRoutes extends HttpService
  with BoxOfficeApi
  with EventMarshalling {

  import StatusCodes._

  def routes: Route = eventsRoute ~ eventRoute ~ ticketsRoute

  def eventsRoute: Route = pathPrefix("events") {
    pathEndOrSingleSlash {
      get {
        onSuccess(getEvents()) { events =>
          complete(OK, events)
        }
      }
    }
  }

  def eventRoute: Route = pathPrefix("events" / Segment) { event =>
    pathEndOrSingleSlash {
      post {
        entity(as[EventDescription]) { ed =>
          onSuccess(createEvent(event, ed.tickets)) {
            case BoxOffice.EventCreated =>
              complete(Created)
            case BoxOffice.EventExists =>
              val err = Error(s"${event} event exists already.")
              complete(BadRequest, err)
          }
        }
      } ~
        get {
          onSuccess(getEvent(event)) {
            _.fold(complete(NotFound))(e => complete(OK, e))
          }
        } ~
        delete {
          onSuccess(cancelEvent(event)) {
            _.fold(complete(NotFound))(e => complete(OK, e))
          }
        }
    }
  }

  def ticketsRoute: Route = pathPrefix("events" / Segment / "tickets") { event =>
    post {
      pathEndOrSingleSlash {
        entity(as[TicketRequest]) { request =>
          onSuccess(requestTickets(event, request.tickets)) { tickets =>
            if (tickets.entries.isEmpty) complete(NotFound)
            else complete(Created, tickets)
          }
        }
      }
    }
  }

}

trait BoxOfficeApi {

  import BoxOffice._

  def createBoxOffice(): ActorRef

  implicit def executionContext: ExecutionContext

  implicit def requestTimeout: Timeout

  lazy val boxOffice = createBoxOffice()

  def createEvent(event: String, nrOfTickets: Int) =
    boxOffice.ask(CreateEvent(event, nrOfTickets))
      .mapTo[EventResponse]

  def getEvents() =
    boxOffice.ask(GetEvents)
      .mapTo[Events]

  def getEvent(event: String) =
    boxOffice.ask(GetEvent(event))
      .mapTo[Option[Event]]

  def cancelEvent(event: String) =
    boxOffice.ask(CancelEvent(event))
      .mapTo[Option[Event]]

  def requestTickets(event: String, tickets: Int) =
    boxOffice.ask(GetTickets(event, tickets))
      .mapTo[TicketSeller.Tickets]

}