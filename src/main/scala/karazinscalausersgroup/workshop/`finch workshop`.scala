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
  * Phase 2. Read with NoContent support
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
  * }}}
  */
object `finch workshop` extends App {

  val ticket = Ticket(UUID.randomUUID(), "Descartes", "Newton", "Create cool Finch workshop")

  println(ticket.uuid)

  storage put ticket

  val `get ticket`: Endpoint[Ticket] =
    get("get" :: "ticket" :: uuid) { uuid: UUID =>
      storage get uuid match {
        case Some(ticket) => Ok(ticket)
        case None         => NoContent
      }
    }

  val service = Http.server.serve(":8080", `get ticket`.toServiceAs[Application.Json])

  Await.ready(service)

}
