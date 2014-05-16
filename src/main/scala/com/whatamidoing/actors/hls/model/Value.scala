package com.whatamidoing.actors.hls.model

import akka.actor.ActorRef

/**
 * Created by kodjobaah on 07/05/2014.
 */

object Value {
  case class GetPlayList(val sender: ActorRef, val streamName: String)
  case class GetTsFile(val sender: ActorRef, val streamName: String)
  case class ReadSegment()
  case class FinnishReadingSegment()
}
