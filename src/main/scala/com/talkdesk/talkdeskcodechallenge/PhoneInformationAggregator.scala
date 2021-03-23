package com.talkdesk.talkdeskcodechallenge

import cats.effect.IO
import com.talkdesk.talkdeskcodechallenge.apimodel.Request._
import com.talkdesk.talkdeskcodechallenge.datasource.{BusinessSectorAPI, PhoneNumberPrefixes}
import io.circe.syntax._
import org.http4s.circe._
import org.http4s.dsl.io._
import org.http4s.{Request, Response}

class PhoneInformationAggregator(businessSectorAPI: BusinessSectorAPI) {
  def aggregate(req: Request[IO]): IO[Response[IO]] = for {
    reqParsed <- req.as[AggregateRequest]
    sectors <- businessSectorAPI.requestBusinessSector(reqParsed)
    apiResponse <- PhoneNumberPrefixes.groupByPrefixAndBusinessSector(sectors)
    encodedResp <- Ok(apiResponse.asJson)
  } yield encodedResp

}
