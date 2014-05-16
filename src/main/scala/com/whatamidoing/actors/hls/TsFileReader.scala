package com.whatamidoing.actors.hls

import akka.actor.{Props, Actor, ActorLogging}
import com.whatamidoing.actors.hls.model.Value._
import spray.http._

import spray.caching.Cache
import org.greencheek.spray.cache.memcached.MemcachedCache
import scala.concurrent.{ExecutionContext, Await, Future}
import java.io.IOException
import spray.http.HttpResponse
import com.whatamidoing.actors.hls.model.Value.GetTsFile
import spray.http.HttpResponse
import spray.http.{ChunkedResponseStart,MessageChunk}
import spray.http.HttpData.{FileBytes}
import com.typesafe.config.ConfigFactory

/**
 * Created by kodjobaah on 08/05/2014.
 */
class TsFileReader(val streamName: String) extends Actor with ActorLogging {


  import scala.concurrent.duration._

  val cache: Cache[String] = new MemcachedCache[String](memcachedHosts = "localhost:11211")

  val config = ConfigFactory.load()
  val segDirectory: String = config.getString("segment.directory")
  val fileName = segDirectory+streamName+".ts"

  def receive = {

    case GetTsFile(sender,streamName) =>

      var openedFile = true
      import java.io.File
      var file: File = null
      try {
       file = new File(fileName)

      } catch {
        case io: IOException =>
        openedFile = false
      }

      if ((openedFile) && (file.exists())){
        //val tsFileReader = priority.actorOf(TsFileReader(streamName))
        //tsFileReader ! GetTsFile(sender,streamName)
        val mediaType = MediaType.custom("video/MP2T")
        val contentType = ContentType(mediaType, None)
        val body = Array(1.toByte) //I also tried Array.empty[Byte], and it had the same behavior
        val entity = HttpEntity(contentType, body)
        val response = HttpResponse(StatusCodes.OK, entity)
        sender ! ChunkedResponseStart(response).withAck(ReadSegment)
      }  else {
        sender ! HttpResponse(status = StatusCodes.NotFound, entity = "Unable to open ts file")
      }

    case ReadSegment  =>

      var openedFile = true
      import java.io.File
      var file: File = null
      try {
        file = new File(fileName)

      } catch {
        case io: IOException =>
          openedFile = false
      }

      if((openedFile) && (file.exists())) {

        log.info("------------ Inside ts file reading file")
        val fileBytes = HttpData.fromFile(fileName = fileName,length = file.length())
        sender ! MessageChunk(data = fileBytes).withAck(FinnishReadingSegment)
      } else {
        log.info("------------ Should be reading file but it is close")
        sender ! ChunkedMessageEnd
      }

    case FinnishReadingSegment =>
      log.info("------------ Inside ts file read: FinnishReading")
      sender ! ChunkedMessageEnd

  }

  import ExecutionContext.Implicits.global
  def cachedOp[T](key: T): Future[String] = cache(key){

    empty()
  }

  def empty(): String = {
    ""
  }


}

object TsFileReader {

  def apply(streamName: String) = Props(classOf[TsFileReader],streamName)
}
