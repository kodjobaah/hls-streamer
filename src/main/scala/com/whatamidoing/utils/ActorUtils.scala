package com.whatamidoing.utils

import akka.actor.{ActorRef, ActorSystem}
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.Await

import scala.concurrent.duration._
import scala.concurrent.Future
import com.whatamidoing.cypher.CypherWriterFunction
import com.whatamidoing.actors.xmpp.XmppSupervisor
import com.whatamidoing.actors.neo4j.Neo4JWriter
import com.whatamidoing.actors.neo4j.Neo4JReader
import com.typesafe.config.ConfigFactory
import models.Neo4jResult


object ActorUtils {

  val system = ActorSystem("whatamidoing-system")
  val cl = ActorUtils.getClass.getClassLoader
  val priority = ActorSystem("priority", ConfigFactory.load(), cl)
  implicit val timeout = Timeout(10 seconds)
  var xmppSupervisor = system.actorOf(XmppSupervisor.props(), "xmppSupervisor")
  var neo4jwriter = system.actorOf(Neo4JWriter.props(), "neo-4j-writer-supervisor")
  var neo4jreader = system.actorOf(Neo4JReader.props(), "neo-4j-reader-supervisor")

  val frameSupervisors = scala.collection.mutable.Map[String, (ActorRef, String)]()

  val Tag: String = "ActorUtils"

  import models.Messages._

  def getStringValueFronResult(results: Neo4jResult): String = {
    if (results.results.size > 0) {
      results.results.head.asInstanceOf[String]
    } else {
      ""
    }

  }


  def invalidateToken(token: String) = {
    val invalidateToken = CypherWriterFunction.invalidateToken(token)
    val writerResponse: Future[Any] = ask(neo4jwriter, PerformOperation(invalidateToken)).mapTo[Any]


    val res = Await.result(writerResponse, 10 seconds) match {
      case WriteOperationResult(results) =>
        if (results.results.size > 0) {
          results.results.head.asInstanceOf[String]
        } else {
          ""
        }

    }

    res
  }


  def createStream(token: String, streamId: String): String = {
    // Logger("FrameSupervisor-receive").info("creating actor for token:"+streamName)

    val stream = CypherWriterFunction.createStream(streamId, token)
    val writeResponse: Future[Any] = ask(neo4jwriter, PerformOperation(stream)).mapTo[Any]

    val res = Await.result(writeResponse, 10 seconds) match {
      case WriteOperationResult(results) =>
        results.results.mkString

    }
    res
  }


  def closeStream(streamName: String) = {
    val closeStream = CypherWriterFunction.closeStream(streamName)
    val writeResponse: Future[Any] = ask(neo4jwriter, PerformOperation(closeStream)).mapTo[Any]

    Await.result(writeResponse, 10 seconds) match {
      case WriteOperationResult(results) =>
        results.results.mkString
    }
  }


  def associateRoomWithStream(token: String, roomId: String): String = {
    val associateRoomWithStream = CypherWriterFunction.associateRoomWithStream(token, roomId)
    val associateRoomWithStreamResponse: Future[Any] = ask(neo4jwriter, PerformOperation(associateRoomWithStream)).mapTo[Any]

    val res = Await.result(associateRoomWithStreamResponse, 10 seconds) match {
      case WriteOperationResult(results) =>
        results.results.mkString

    }
    res
  }

  def invalidateAllStreams(token: String) = {

    val invalidateAllStreams = CypherWriterFunction.invalidateAllStreams(token)
    val writerResponse: Future[Any] = ask(neo4jwriter, PerformOperation(invalidateAllStreams)).mapTo[Any]

    val res = Await.result(writerResponse, 10 seconds) match {
      case WriteOperationResult(results) =>
        if (results.results.size > 0) {
          results.results.head.asInstanceOf[String]
        } else {
          ""
        }

    }
    res
  }


  def updateUserInformation(token: String, domId: String) = {

    val updateUserInformation = CypherWriterFunction.updateUserInformation(token, domId)
    val writerResponse: Future[Any] = ask(neo4jwriter, PerformOperation(updateUserInformation)).mapTo[Any]

    val res = Await.result(writerResponse, 10 seconds) match {
      case WriteOperationResult(results) =>
        if (results.results.size > 0) {
          results.results.head.asInstanceOf[String]
        } else {
          ""
        }

    }
    res
  }

  def createXmppDomain(domain: String) {
    import models.Messages.CreateXMPPDomainMessage
    val mess = CreateXMPPDomainMessage(domain)

    val response: Future[Any] = ask(xmppSupervisor, mess).mapTo[Any]
    import models.Messages.Done
    Await.result(response, 10 seconds) match {
      case Done(results) =>

    }


  }


  def createXmppGroup(roomJid: String, token: String) = {
    import models.Messages.CreateXMPPGroupMessage
    val message = CreateXMPPGroupMessage(roomJid, token)
    xmppSupervisor ! message
  }


  def createXmppRoom(roomJid: String) = {
    import models.Messages.CreateXMPPRoomMessage
    val message = CreateXMPPRoomMessage(roomJid)
    xmppSupervisor ! message
  }

  def removeRoom(roomJid: String) = {
    import models.Messages.RemoveXMPPRoomMessage
    val message = RemoveXMPPRoomMessage(roomJid)
    xmppSupervisor ! message
  }

}