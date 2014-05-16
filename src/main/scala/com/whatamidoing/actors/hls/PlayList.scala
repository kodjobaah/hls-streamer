package com.whatamidoing.actors.hls

import akka.actor.{Props, Actor, ActorLogging}
import com.whatamidoing.actors.hls.model.Value.GetPlayList
import spray.http._

import spray.caching.Cache
import org.greencheek.spray.cache.memcached.MemcachedCache
import scala.concurrent.{Await, ExecutionContext, Future}
import com.whatamidoing.actors.hls.model.Value.GetPlayList
import spray.http.HttpResponse
import com.whatamidoing.actors.hls.model.Value.GetPlayList

/**
 * Created by kodjobaah on 08/05/2014.
 */
class PlayList(val streamName: String) extends Actor with ActorLogging {


  val mediaType = MediaType.custom("application/x-mpegURL")
  val contentType = ContentType(mediaType, None)

  import ExecutionContext.Implicits.global
  import scala.concurrent.duration._
  val cache: Cache[String] = new MemcachedCache[String](memcachedHosts = "localhost:11211")
  def receive = {

    case GetPlayList(sender,streamName) =>
      val pl:Future[String] = cachedOp("PlayList-"+streamName)
      val playList = Await.result(pl, 20 seconds)
      if (playList.size > 1)  {
        val body = playList //I also tried Array.empty[Byte], and it had the same behavior
        val entity = HttpEntity(contentType, body)
          sender ! HttpResponse(StatusCodes.OK, entity)
      } else {

         sender ! HttpResponse(status = StatusCodes.NotFound, entity = "No playlist for " + streamName)
      }
  }

  import ExecutionContext.Implicits.global
  def cachedOp[T](key: T): Future[String] = cache(key){
    empty()
  }

  def empty(): String = {
    ""
  }


}

object PlayList {

  def apply(streamName: String) = Props(classOf[PlayList],streamName)
}
