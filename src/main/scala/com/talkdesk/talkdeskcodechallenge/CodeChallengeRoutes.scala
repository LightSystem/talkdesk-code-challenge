package com.talkdesk.talkdeskcodechallenge

import cats.data.Kleisli
import cats.effect.IO
import org.http4s.dsl.io._
import org.http4s.implicits._
import org.http4s.{HttpRoutes, Request, Response}

object CodeChallengeRoutes {

  def codeChallengeRoutes(service: PhoneInformationAggregator): Kleisli[IO, Request[IO], Response[IO]] = HttpRoutes.of[IO] {
    case GET -> Root / "status" => IO(Response(Ok))
    case req @ POST -> Root / "aggregate" => service.aggregate(req)
  }.orNotFound

}
