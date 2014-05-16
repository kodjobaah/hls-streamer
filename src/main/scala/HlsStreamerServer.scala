import akka.actor._
import com.whatamidoing.actors.hls.model.Value.{ReadSegment, GetPlayList, GetTsFile}
import com.whatamidoing.actors.hls.{TsFileReader, PlayList}
import org.slf4j.LoggerFactory
import akka.io.IO
import scala.Some
import com.whatamidoing.utils.ActorUtils
import com.typesafe.config.ConfigFactory

import spray.http.HttpMethods.{GET, POST}
import spray.http.{Uri, StatusCodes, HttpRequest, HttpResponse}
import spray.can.Http

object HlsStreamerServer extends App {


  val config = ConfigFactory.load()
  val httpSystem = ActorSystem("videostreaming")
  val streamingServer = httpSystem.actorOf(Props[StreamingServer])
  val httpServerName = config.getString("http.server.name")
  val httpServerPort = config.getInt("http.server.port")

  val serverName = config.getString("server.name")
  IO(Http)(httpSystem) ! Http.Bind(
    streamingServer,
    httpServerName,
    httpServerPort
  )
  val serverPort = config.getInt("server.port")


  class StreamingServer extends Actor {

    val log = LoggerFactory.getLogger("SocketServer")
    val cl = ActorUtils.getClass.getClassLoader
    val priority = ActorSystem("priority", ConfigFactory.load(), cl)

    def receive = {

      case Http.Connected(remoteAddress, localAddress) =>
        val conn = priority.actorOf(Props(classOf[HlsProvider]))
        sender ! Http.Register(conn)

      case x => log.debug("DEFAULT_MESSAGE:" + x.toString)

    }
  }

  class HlsProvider extends Actor with ActorLogging {
    val priority = ActorSystem("testSystem")

    val pathTest = """^(.*)\/(.*)(.m3u8)$""".r
    val pathTestTsFile = """^(.*)\/(.*)(.ts)$""".r

    var tsFileReader: ActorRef = _

    def performOperation(uri: Uri) {
      log.info("pathturl:" + uri.path.toString())
      val res = pathTest findFirstIn uri.path.toString() match {
        case Some(pathTest(before, streamName, suffix)) =>
          val playList = priority.actorOf(PlayList(streamName))
          playList ! GetPlayList(sender, streamName)
          "found"
        case _ =>
          log.info("this is not a request for a m3u8 file")
          ""
      }

      if (res != "found") {
        val tsRes = pathTestTsFile findFirstIn uri.path.toString() match {

          case Some(pathTestTsFile(before, streamName, suffix)) =>
            tsFileReader = priority.actorOf(TsFileReader(streamName))
            tsFileReader ! GetTsFile(sender, streamName)
            "found"
          case _ =>
            log.info("this is not a request for a ts file")
            ""
        }

        if (tsRes != "found") {
          sender ! HttpResponse(status = StatusCodes.Unauthorized, entity = "Can not Perform this Request")
        }
      }
    }
    def receive = {

      case HttpRequest(POST, uri, headers, _, _) =>
        performOperation(uri)

      case HttpRequest(GET, uri, headers, _, _) =>
        performOperation(uri)

      case ReadSegment =>
        log.info("------------ should be sending back segment")
        sender ! HttpResponse(status = StatusCodes.Unauthorized, entity = "Should be reading segment")
    }
  }


}