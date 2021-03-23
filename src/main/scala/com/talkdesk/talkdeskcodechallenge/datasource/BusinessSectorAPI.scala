package com.talkdesk.talkdeskcodechallenge.datasource

import cats.effect.{IO, _}
import cats.implicits._
import com.talkdesk.talkdeskcodechallenge.apimodel.Request.AggregateRequest
import io.circe.generic.auto._
import org.http4s._
import org.http4s.circe._
import org.http4s.client._
import org.slf4j.LoggerFactory

import scala.util.{Failure, Success}

class BusinessSectorAPI(httpClient: Client[IO])(implicit contextShift: ContextShift[IO]) {

  private val logger = LoggerFactory.getLogger(getClass)

  def requestBusinessSector(phoneNumbers: AggregateRequest): IO[Set[BusinessSectorResponse]] = phoneNumbers.parTraverse { number =>
    val target = uri"https://challenge-business-sector-api.meza.talkdeskstg.com/sector/" / number
    httpClient.expect(target)(jsonOf[IO, BusinessSectorResponse]).attempt.map {
      case Left(unexpectedStatus: UnexpectedStatus) if unexpectedStatus.status == Status.BadRequest =>
        logger.info(s"Received BadRequest Status from BusinessSectorAPI, skipping phone number: $number")
        Failure(unexpectedStatus)
      case Left(error) =>
        throw new RuntimeException("Failure when calling BusinessSectorAPI", error)
      case Right(response) => Success(response)
    }
  }.map(_.collect { case Success(response) => response }.toSet)

}
