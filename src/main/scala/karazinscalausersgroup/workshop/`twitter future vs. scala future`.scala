package karazinscalausersgroup.workshop

import scala.concurrent.{Future => ScalaFuture, Await => ScalaAwait}
import com.twitter.util.{Future => TwitterFuture, Await => TwitterAwait}

import scala.concurrent.duration._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.postfixOps

// extend values by adding the conversion method "as"
import com.twitter.bijection.Conversion.asMethod

// pull in various implicit converters that "as" expects,
// including twitter2ScalaFuture:
import com.twitter.bijection.twitter_util.UtilBijections._


/**
  * A simple twitter future to scala future and vice versa custom converter
  *
  *
  * {{{
  *   $ sbt 'runMain karazinscalausersgroup.workshop.twitter$u0020future$u0020vs$u002E$u0020scala$u0020future'
  * }}}
  */
object `twitter future vs. scala future` extends App {

  val `scala future`: ScalaFuture[Int] = ScalaFuture { 17 }

  val `twitter future`: TwitterFuture[Int] = `scala future`.as[TwitterFuture[Int]]

  assert(TwitterAwait.result(`twitter future`) == ScalaAwait.result(`scala future`, 1 second))

}
