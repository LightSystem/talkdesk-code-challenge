package com.talkdesk.talkdeskcodechallenge.apimodel

object Response {
  type APIResponse = Map[Prefix, PerBusinessSector]

  type Prefix = String
  type PerBusinessSector = Map[String, Int]
}