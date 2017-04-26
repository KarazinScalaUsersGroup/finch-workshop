package karazinscalausersgroup.workshop

import java.util.UUID

import com.twitter.app.Flag
import com.twitter.finagle.Http
import com.twitter.finagle.param.Stats
import com.twitter.util.Await
import io.circe.generic.auto._
import io.finch._
import io.finch.circe._
import model.Ticket

import com.twitter.server.TwitterServer
import com.twitter.finagle.stats.{MetricsStatsReceiver, Counter}

/**
  * A simple CRUD Finch application with TwitterServer and metrics
  *
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
  *   $ http POST :8080/add/ticket reporter="Descartes" assignee="Newton" description="Create cool Finch workshop"
  *
  *   $ http POST :8080/update/ticket/uuid reporter="Descartes" assignee="Leibniz" description="Create cool Finch workshop"
  *
  *   $ http GET :9990/admin/metrics.json | jq '.get_requests_counter'
  * }}}
  */
object `finch workshop` extends TwitterServer {

  val port: Flag[Int] = flag("port", 8080, "TCP port for HTTP server")

  val counter = MetricsStatsReceiver.defaultRegistry.createCounter("get_requests_counter")

  val `get ticket`: Endpoint[Ticket] =
    get("get" :: "ticket" :: uuid) { uuid: UUID =>

      counter.increment()

      storage get uuid match {
        case Some(ticket) => Ok(ticket)
        case None         => NoContent
      }
    }

  val added: Endpoint[Ticket] = jsonBody[UUID => Ticket].map(_(UUID.randomUUID()))

  val `add ticket`: Endpoint[Ticket] =
    post("add" :: "ticket" :: added) { ticket: Ticket =>
      storage put ticket

      Ok(ticket)
    }

  val patched: Endpoint[Ticket => Ticket] = jsonBody[Ticket => Ticket]

  val `update ticket`: Endpoint[Ticket] =
    post("update" :: "ticket" :: uuid :: patched) { (uuid: UUID, pt: Ticket => Ticket) =>

      storage get uuid match {
        case Some(oldTicket) =>
          val `updated ticket` = pt(oldTicket)
          storage put `updated ticket`
          Ok(`updated ticket`)

        case None =>
          NoContent
      }
    }

  val api = `get ticket` :+: `add ticket` :+: `update ticket`

  def main() {
    log.info("Serving the Todo application")

    val server = Http.server
      .withStatsReceiver(statsReceiver)
      .serve(s":${port()}", api.toServiceAs[Application.Json])

    onExit { server.close() }

    Await.ready(adminHttpServer)
  }

}
