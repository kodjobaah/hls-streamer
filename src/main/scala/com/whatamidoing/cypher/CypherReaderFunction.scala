package com.whatamidoing.cypher

import models.Neo4jResult
import org.anormcypher.Cypher

object CypherReaderFunction {

  val Tag: String = "CypherBuilder"


  def findActiveStreamForToken(token: String): () => Neo4jResult = {
     val findActiveStream: Function0[Neo4jResult] = () => {
       val name = Cypher(CypherReader.findActiveStreamForToken(token)).apply().map(row => (row[String]("name"))).toList
       val neo4jResult = new Neo4jResult(name)
      // Logger(Tag).info("findActiveStreamForToken: name of active strem:"+name)
       neo4jResult
     }
     findActiveStream
  }
  

 def fetchUserDetails(cpId: String): () => Neo4jResult = {

    val fetchUserDetails: Function0[Neo4jResult] = () => {
      val fetchUserDetails = Cypher(CypherReader.fetchUserDetails(cpId)).apply().map(row => (row[Option[String]]("email"),row[Option[String]]("firstName"),row[Option[String]]("lastName"))).toList
      val neo4jResult = new Neo4jResult(fetchUserDetails)
      neo4jResult
    }
    fetchUserDetails
 }


 def fetchUserInformation(token: String): () => Neo4jResult = {

    val fetchUserInformation: Function0[Neo4jResult] = () => {
      System.out.println("user information")
      val fetchUserInformation = Cypher(CypherReader.fetchUserInformation(token)).apply().map(row => (row[Option[String]]("email"),row[Option[String]]("firstName"),row[Option[String]]("lastName"),row[Option[String]]("domId"))).toList
      System.out.println("results:"+fetchUserInformation)
      val neo4jResult = new Neo4jResult(fetchUserInformation)
      neo4jResult
    }
    fetchUserInformation
 }

 def getRoomJid(token: String): () => Neo4jResult = {

    val getRoomJid: Function0[Neo4jResult] = () => {
    	val getRoomJid = Cypher(CypherReader.getRoomJid(token)).apply().map(row => (row[Option[String]]("jid"))).toList
     	val neo4jResult = new Neo4jResult(getRoomJid)
	neo4jResult
    }
    getRoomJid
  }

 def getRoomJidForStream(stream: String): () => Neo4jResult = {

    val getRoomJidForStream: Function0[Neo4jResult] = () => {
    	val getRoomJidForStream = Cypher(CypherReader.getRoomJidForStream(stream)).apply().map(row => (row[Option[String]]("jid"))).toList
     	val neo4jResult = new Neo4jResult(getRoomJidForStream)
	neo4jResult
    }
    getRoomJidForStream
  }


  def getValidToken(token: String): () => Neo4jResult = {
    val getValidToken: Function0[Neo4jResult] = () => {
      val tokens = Cypher(CypherReader.getValidToken(token)).apply().map(row => (row[String]("token"))).toList
      val neo4jResult = new Neo4jResult(tokens)
      // Logger(Tag).info("getUserToken:this is a valid token: " + tokens)
      neo4jResult
    }
    getValidToken

  }


}