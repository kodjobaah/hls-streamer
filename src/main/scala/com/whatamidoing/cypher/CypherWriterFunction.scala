package com.whatamidoing.cypher

import org.joda.time.DateTime

object CypherWriterFunction {

  import models._
  import org.anormcypher._

  def closeStream(stream: String): () => Neo4jResult = {
    val closeStream: Function0[Neo4jResult] = () => {
      val closeStream = Cypher(CypherWriter.closeStream(stream)).execute()

      val dt = new DateTime()
      val day = dt.getDayOfMonth
      val time = dt.getHourOfDay + ":" + dt.getMinuteOfHour + ":" + dt.getSecondOfMinute
      val dayDescription = "day " + day + " - month " + dt.getMonthOfYear + "- year " + dt.getYear
      val endStream = Cypher(CypherWriter.associateStreamCloseToDay(stream, dayDescription, time)).execute()

      val results: List[String] = List(closeStream.toString, endStream.toString)
      val neo4jResult = new Neo4jResult(results)
      // Logger("CypherWriterFunction.closeStream").info("results from closing stream:" + results)
      neo4jResult
    }
    closeStream
  }


  def createStream(stream: String, token: String): () => Neo4jResult = {
    val createStream: Function0[Neo4jResult] = () => {
      val dt = new DateTime
      val day = dt.getDayOfMonth
      val dayDescription = "day " + day + " - month " + dt.getMonthOfYear + "- year " + dt.getYear
      val time = dt.getHourOfDay + ":" + dt.getMinuteOfHour + ":" + dt.getSecondOfMinute
      val streamName = "stream-" + time
      val createStream = Cypher(CypherWriter.createStream(stream, streamName)).execute()


      val linkStreamToDay = Cypher(CypherWriter.linkStreamToDay(stream, dayDescription, time)).execute()

      val linkSteamToToken = Cypher(CypherWriter.linkStreamToToken(stream, token)).execute()

      //  Logger("CypherWriterFunction.createStream").info("this is createStream: " + createStream)
      //  Logger("CypherWriterFunction.createStream").info("this is linkStream: " + linkStreamToDay)
      //  Logger("CypherWriterFunction.createUser").info("this is three: " + linkSteamToToken)

      val results: List[String] = List(createStream.toString, linkStreamToDay.toString, linkSteamToToken.toString)
      val neo4jResult = new Neo4jResult(results)
      neo4jResult

    }
    createStream

  }

  def invalidateToken(token: String): () => Neo4jResult = {

    val invalidate: Function0[Neo4jResult] = () => {
      val invalidate = Cypher(CypherWriter.invalidateToken(token)).execute()

      val results: List[String] = List(invalidate.toString)
      val neo4jResult = new Neo4jResult(results)
      neo4jResult
    }

    invalidate
  }


  def associateRoomWithStream(token: String, roomId: String): () => Neo4jResult = {
    val associateRoomWithStream: Function0[Neo4jResult] = () => {
      val associateRoomWithStream = Cypher(CypherWriter.associateRoomWithStream(token, roomId)).execute()
      val neo4jResult = new Neo4jResult(List(associateRoomWithStream.toString))
      neo4jResult
    }
    associateRoomWithStream
  }


  def invalidateAllStreams(token: String): () => Neo4jResult = {

    val invalidate: Function0[Neo4jResult] = () => {
      val invalidate = Cypher(CypherWriter.invalidateAllStreams(token)).execute()

      val results: List[String] = List(invalidate.toString)
      val neo4jResult = new Neo4jResult(results)
      neo4jResult
    }

    invalidate
  }

  def updateUserInformation(token: String, domId: String): () => Neo4jResult = {

    val updateUserInformation: Function0[Neo4jResult] = () => {
      val userInformation = Cypher(CypherWriter.updateUserInformation(token, domId)).execute()

      val results: List[String] = List(userInformation.toString)
      val neo4jResult = new Neo4jResult(results)
      neo4jResult
    }

    updateUserInformation
  }


}