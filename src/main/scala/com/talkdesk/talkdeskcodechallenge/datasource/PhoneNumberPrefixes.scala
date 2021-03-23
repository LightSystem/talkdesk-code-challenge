package com.talkdesk.talkdeskcodechallenge.datasource

import better.files.Resource
import cats.effect.IO
import com.talkdesk.talkdeskcodechallenge.apimodel.Response.APIResponse

import scala.concurrent.ExecutionContext.global
import scala.concurrent.Future
import scala.io.Source
import scala.util.{Failure, Success, Try}

object PhoneNumberPrefixes {
  private val prefixes = Future {
    val inputStream = Resource.my.getAsStream("/prefixes.txt")
    val lines = Try {
      Source.fromInputStream(inputStream).getLines().map { l: String => l.trim }.filterNot(_.isEmpty).toSet
    }
    inputStream.close()
    lines match {
      case Failure(error) => throw new RuntimeException("Failure reading prefixes file", error)
      case Success(value) => value
    }
  }(global)

  /**
   * Given phone numbers and associated business sectors, get the prefixes defined in prefixes file and group sectors by prefix
   * @param businessSectors phone numbers and associated business sectors
   * @return see [[com.talkdesk.talkdeskcodechallenge.apimodel.Response.APIResponse]]
   */
  def groupByPrefixAndBusinessSector(businessSectors: Set[BusinessSectorResponse]): IO[APIResponse] = IO.fromFuture(IO(
      prefixes.map { prefixes =>
        businessSectors.view.map { case BusinessSectorResponse(number, sector) =>
          val formattedNumber = if(number.startsWith("+")) number.drop(1) else number
          val possiblePrefixes = formattedNumber.zipWithIndex.map { case (char, idx) =>
            formattedNumber.substring(0, idx) + char
          }

          possiblePrefixes.find(prefixes.contains) -> sector
        }.collect { case (Some(prefix), sector) =>
          prefix -> sector
        }.groupBy { case (prefix, _) =>
          prefix
        }.map { case (prefix, valuesAfterGrouping) =>
          prefix -> valuesAfterGrouping.map { case (_, sector) => sector }
        }.map { case (prefix, allSectors) =>
          val uniqueSectors = allSectors.toSet

          prefix -> uniqueSectors.map { sector =>
            sector -> allSectors.count(_ == sector)
          }.toMap
        }
    }(global)
  ))

}
