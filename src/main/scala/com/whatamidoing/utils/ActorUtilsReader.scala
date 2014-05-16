package com.whatamidoing.utils

import akka.actor.ActorSystem
import com.whatamidoing.cypher.CypherReaderFunction
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.Future

object ActorUtilsReader {

  val system = ActorSystem("whatamidoing-system")
  implicit val timeout = Timeout(500 seconds)
  var neo4jreader = ActorUtils.neo4jreader

  import models.Messages._


  def streamNameForToken(token: String) = {
    val findStreamForToken = CypherReaderFunction.findActiveStreamForToken(token)
    val findStreamForTokenResponse: Future[Any] = ask(neo4jreader, PerformReadOperation(findStreamForToken)).mapTo[Any]
    var streamName = Await.result(findStreamForTokenResponse, 10 seconds) match {
      case ReadOperationResult(readResults) => {

        if (readResults.results.size > 0) {
          readResults.results.head.asInstanceOf[String]
        } else {
          ""
        }
      }
    }

    streamName
  }

  def getValidToken(token: String) = {
    val getValidToken = CypherReaderFunction.getValidToken(token)
    println("--inside getting token")
    val getValidTokenResponse: Future[Any] = ask(neo4jreader, PerformReadOperation(getValidToken)).mapTo[Any]


    var res = Await.result(getValidTokenResponse, 10 seconds) match {
      case ReadOperationResult(readResults) => {
        readResults.results
      }
    }

    res
  }


  def findActiveStreamForToken(token: String): String = {

    val findStreamForToken = CypherReaderFunction.findActiveStreamForToken(token)
    val getValidTokenResponse: Future[Any] = ask(neo4jreader, PerformReadOperation(findStreamForToken)).mapTo[Any]

    var streamName = Await.result(getValidTokenResponse, 10 seconds) match {
      case ReadOperationResult(readResults) => {

        if (readResults.results.size > 0) {
          readResults.results.head.asInstanceOf[String]
        } else {
          ""
        }
      }
    }
    streamName
  }

  import models.UserInformation

  def fetchUserInformation(token: String): UserInformation = {

    val fetchUserInformation = CypherReaderFunction.fetchUserInformation(token)
    val fetchUserInformationResponse: Future[Any] = ask(neo4jreader, PerformReadOperation(fetchUserInformation)).mapTo[Any]

    val results = Await.result(fetchUserInformationResponse, 30 seconds) match {
      case ReadOperationResult(readResults) => {

        var result = UserInformation()
        for (res <- readResults.results) {
          var x: UserInformation = res match {
            case (Some(email), Some(firstName), Some(lastName), Some(domId)) => {

              val u = "userInformation:" + email
              UserInformation(email.asInstanceOf[String], firstName.asInstanceOf[String], lastName.asInstanceOf[String], Option(domId.asInstanceOf[String]))
            }
            case (Some(email), Some(firstName), Some(lastName), None) => {
              UserInformation(email.asInstanceOf[String], firstName.asInstanceOf[String], lastName.asInstanceOf[String], None)


            }
            case _ => null
          }
          if (x != null) {
            result = x
          }

        }

        result
      }
    }
    results
  }


  def getRoomJid(token: String): String = {

    val getRoomJid = CypherReaderFunction.getRoomJid(token)
    val getRoomJidResponse: Future[Any] = ask(neo4jreader, PerformReadOperation(getRoomJid)).mapTo[Any]

    val results = Await.result(getRoomJidResponse, 30 seconds) match {
      case ReadOperationResult(readResults) => {

        var result = List[String]()
        for (res <- readResults.results) {
          var x: String = res match {
            case Some(ip) => ip.asInstanceOf[String]
            case _ => null
          }
          if (x != null) {

            result = result :+ x
          }

        }
        if (result.size > 0) {
          result.head
        } else {
          ""
        }
      }

    }
    results
  }

  def getRoomJidForStream(stream: String): String = {

    val getRoomJidForStream = CypherReaderFunction.getRoomJidForStream(stream)
    val getRoomJidForStreamResponse: Future[Any] = ask(neo4jreader, PerformReadOperation(getRoomJidForStream)).mapTo[Any]

    val results = Await.result(getRoomJidForStreamResponse, 30 seconds) match {
      case ReadOperationResult(readResults) => {

        var result = List[String]()
        for (res <- readResults.results) {
          var x: String = res match {
            case Some(ip) => ip.asInstanceOf[String]
            case _ => null
          }
          if (x != null) {

            result = result :+ x
          }

        }
        if (result.size > 0) {
          result.head
        } else {
          ""
        }
      }

    }
    results
  }


}