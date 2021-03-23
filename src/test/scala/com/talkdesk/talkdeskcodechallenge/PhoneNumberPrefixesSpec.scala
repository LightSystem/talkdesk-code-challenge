package com.talkdesk.talkdeskcodechallenge

import com.talkdesk.talkdeskcodechallenge.datasource.{BusinessSectorResponse, PhoneNumberPrefixes}

class PhoneNumberPrefixesSpec extends org.specs2.mutable.Specification {

  "PhoneNumberPrefixes" >> {
    "Groups by sector correctly" >> {
      val grouping = PhoneNumberPrefixes.groupByPrefixAndBusinessSector(
        Set(BusinessSectorResponse("+1983248", "Technology"), BusinessSectorResponse("+1382355", "Technology"))
      ).unsafeRunSync()

      grouping must have size 1
      grouping must haveKey("1")
      grouping must haveValue(Map("Technology" -> 2))
    }
   "When prefix is not present it is skipped" >> {
     val grouping = PhoneNumberPrefixes.groupByPrefixAndBusinessSector(
       Set(BusinessSectorResponse("notpresent", "Technology"), BusinessSectorResponse("+1382355", "Technology"))
     ).unsafeRunSync()

     grouping must haveValue(Map("Technology" -> 1))
   }
  }

}
