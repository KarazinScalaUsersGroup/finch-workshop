import io.finch._, com.twitter.finagle.http.{Request, Method}
import io.finch._
import com.twitter.finagle.http.{Request, Method}

val endpoint1: Endpoint0 = "foo"
endpoint1(Input.get("/foo")).isMatched
endpoint1(Input.get("/bar")).isMatched

/**
  * string: Endpoint[String]
  * long: Endpoint[Long]
  * int: Endpoint[Int]
  * boolean: Endpoint[Boolean]
  * uuid: Endpoint[java.lang.UUID]
  */

val endpoint2 = endpoint1 :: boolean
endpoint2(Input.get("/foo/true")).isMatched
endpoint2(Input.get("/foo/17")).isMatched
endpoint2(Input.get("/bar/true")).isMatched

val endpoint3 = endpoint1 :: int
endpoint3(Input.get("/foo/true")).isMatched
endpoint3(Input.get("/foo/17")).isMatched
endpoint3(Input.get("/bar/true")).isMatched

val api = endpoint2 :+: endpoint3
api(Input.get("/foo/true")).isMatched
api(Input.get("/foo/17")).isMatched
api(Input.get("/bar/true")).isMatched

