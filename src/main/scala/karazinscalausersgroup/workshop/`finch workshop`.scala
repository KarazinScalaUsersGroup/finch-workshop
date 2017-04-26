package karazinscalausersgroup.workshop

import java.util.UUID

import com.twitter.finagle.Http
import com.twitter.finagle.param.Stats
import com.twitter.util.Await
import io.circe.generic.auto._
import io.finch._
import io.finch.circe._
import model.Ticket

import com.twitter.server.TwitterServer
import com.twitter.finagle.stats.Counter

/**
  * A simple CRUD Finch application
  *
  * Phase 5. Update by uuid in path
  *
  * Use the following sbt command to run the application.
  *
  * {{{
  *   $ sbt sbt 'runMain karazinscalausersgroup.workshop.finch$u0020workshop'
  * }}}
  *
  * Use the following HTTPie/curl commands to test endpoints.
  *
  * {{{
  *   $ http GET :8080/get/ticket/uuid
  *
  *   $ http POST :8080/add/ticket uuid=e5c06c7e-a2b5-45f8-a5e7-90de7ede1f76 reporter="Descartes" assignee="Newton" description="Create cool Finch workshop"
  *
  *   $ http POST :8080/update/ticket uuid=e5c06c7e-a2b5-45f8-a5e7-90de7ede1f76 reporter="Descartes" assignee="Leibniz" description="Create cool Finch workshop"
  * }}}
  */
object `finch workshop` extends App {

  val `get ticket`: Endpoint[Ticket] =
    get("get" :: "ticket" :: uuid) { uuid: UUID =>
      storage get uuid match {
        case Some(ticket) => Ok(ticket)
        case None         => NoContent
      }
    }

  val `add ticket`: Endpoint[Ticket] =
    post("add" :: "ticket" :: jsonBody[Ticket]) { ticket: Ticket =>
      storage put ticket

      Ok(ticket)
    }

  val `update ticket`: Endpoint[Ticket] =
    post("update" :: "ticket" :: uuid :: jsonBody[Ticket]) { (uuid: UUID, updatedTicket: Ticket) =>
      storage get uuid match {
        case Some(oldTicket) => storage put updatedTicket; Ok(updatedTicket)
        case None            => NoContent
      }
    }


  val api = `get ticket` :+: `add ticket` :+: `update ticket`

  val service = Http.server.serve(":8080", api.toServiceAs[Application.Json])

  Await.ready(service)

}
