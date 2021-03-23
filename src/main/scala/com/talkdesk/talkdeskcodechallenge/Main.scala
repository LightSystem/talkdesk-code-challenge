package com.talkdesk.talkdeskcodechallenge

import java.util.concurrent.Executors

import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits._
import com.talkdesk.talkdeskcodechallenge.datasource.BusinessSectorAPI
import org.http4s.client.Client
import org.http4s.client.blaze.BlazeClientBuilder
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.middleware.Logger
import org.http4s.util.threads.threadFactory

import scala.concurrent.ExecutionContext

object Main extends IOApp {
  def run(args: List[String]): IO[ExitCode] = httpClient.use { client =>
    BlazeServerBuilder[IO]
      .bindHttp(8080, "0.0.0.0")
      .withHttpApp(createHttpApp(client))
      .serve
      .compile
      .drain
      .as(ExitCode.Success)
  }


  private def httpClient = {
    val httpClientEC = ExecutionContext.fromExecutor(
      Executors.newFixedThreadPool(
        Runtime.getRuntime.availableProcessors(), threadFactory({l => s"http-client-thread-$l"}, daemon = true)
      )
    )
    BlazeClientBuilder[IO](httpClientEC).resource
  }

  private def createHttpApp(httpClient: Client[IO]) = {
    val dataSource = new BusinessSectorAPI(httpClient)
    val service = new PhoneInformationAggregator(dataSource)

    Logger.httpApp(logHeaders = true, logBody = true)(CodeChallengeRoutes.codeChallengeRoutes(service))
  }
}