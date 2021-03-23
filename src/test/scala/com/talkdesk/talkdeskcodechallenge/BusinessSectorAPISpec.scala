package com.talkdesk.talkdeskcodechallenge

import java.util.concurrent.Executors

import cats.effect.{ContextShift, IO}
import com.talkdesk.talkdeskcodechallenge.datasource.{BusinessSectorAPI, BusinessSectorResponse}
import org.http4s.client.blaze.BlazeClientBuilder

import scala.concurrent.ExecutionContext

class BusinessSectorAPISpec extends org.specs2.mutable.Specification {

  private val TestExecutionContext: ExecutionContext = ExecutionContext.fromExecutor(Executors.newSingleThreadExecutor())

  private implicit val contextShift: ContextShift[IO] = IO.contextShift(TestExecutionContext)

  private lazy val businessSectorAPI = BlazeClientBuilder[IO](TestExecutionContext).resource.map(new BusinessSectorAPI(_))

  "BusinessSectorAPI" >> {
    "Returns results for valid numbers" >> {
      val results = businessSectorAPI.use { api =>
        api.requestBusinessSector(List("+1983248", "001382355", "+147 8192", "+4439877"))
      }.unsafeRunSync()

      results must contain(BusinessSectorResponse("+1983248", "Technology"))
    }
    "Fail on invalid number" >> {
      val results = businessSectorAPI.use { api =>
        api.requestBusinessSector(List("tests"))
      }.unsafeRunSync()

      results must be empty
    }
  }

}
