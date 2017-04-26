package karazinscalausersgroup.workshop

import com.twitter.finagle.Http
import com.twitter.util.Await
import io.finch._

/**
  * A simple Finch Echo Server application
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
  *   $ http GET :8080/hello/world
  * }}}
  */
object `finch workshop` extends App {

  val api: Endpoint[String] = get("hello" :: string) { name: String => Ok(s"Hello, $name!") }

  val service = Http.server.serve(":8080", api.toServiceAs[Text.Plain])

  Await.ready(service)

}

