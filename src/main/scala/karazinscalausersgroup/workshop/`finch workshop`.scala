package karazinscalausersgroup.workshop

import java.util.UUID

import com.twitter.finagle.Http
import com.twitter.finagle.param.Stats
import com.twitter.util.Await
import io.circe.generic.auto._
import io.finch._
import io.finch.circe._
import model.Ticket
import storage._

import com.twitter.server.TwitterServer
import com.twitter.finagle.stats.Counter

/**
  * A simple Finch application
  *
  * Use the following sbt command to run the application.
  *
  * {{{
  *   $ sbt sbt 'runMain karazinscalausersgroup.workshop.finch$u0020workshop'
  * }}}
  *
  */
object `finch workshop` extends App {



}
