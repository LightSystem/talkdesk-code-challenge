package com.talkdesk.talkdeskcodechallenge.apimodel

import cats.effect.IO
import org.http4s.EntityDecoder
import org.http4s.circe.jsonOf

object Request {
  type AggregateRequest = List[String]
  implicit val aggregateRequestDecoder: EntityDecoder[IO, AggregateRequest] = jsonOf[IO, AggregateRequest]
}
