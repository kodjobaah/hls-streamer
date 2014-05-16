package com.whatamidoing.cypher

object CypherWriter {
  def createStream(streamId: String, stream: String): String = {
    val t = s"""
                 create (stream:Stream {id:"$streamId",name:"$stream", state:"active"})
                """
    return t
  }
 
 def linkStreamToToken(stream: String, token: String): String = {
    val t = s"""
    
    		match (a:Stream), (b:AuthenticationToken)
    		where a.id="$stream" and b.token="$token"
    		create a-[r:USING]->b
    		return r
    """
    return t
   
 }
 

 def linkStreamToDay(stream: String, day: String, time: String): String = {
    val t = s"""
    			match (a:Stream), (b:Day)
    			where a.id="$stream" AND b.description="$day"
    			create a-[r:BROADCAST_ON {time:"$time"}]->b
                return r
    			"""
    return t
  }
 

  def associateStreamCloseToDay(stream: String, day: String, time: String): String  = {
     val linkCloseStreamToDay = s"""
     
 			  match (a:Stream), (b:Day)
			  where a.id="$stream" AND b.description = "$day"
			  create a-[r:BROADCAST_ENDED_ON {time:"$time"}]->b
			  return r
			  
			  """
    return linkCloseStreamToDay    
  }
  
  def closeStream(stream: String): String = {
    
    val res=s"""
    		match (stream:Stream)
    		where stream.id="$stream"
    		SET stream.state ="inactive"
    		return stream.state as state
    """
    return res
  }
  
  def invalidateToken(token: String) : String = {
      val res=s"""
    		match (a:AuthenticationToken)
    		where a.token = "$token"
    		SET a.valid ="false"
    		return a.valid as valid
      """
      return res
    
  }
  

  def associateRoomWithStream(token: String, roomId: String ): String = {
      val res = s"""
      match (a:AuthenticationToken) where a.token = "$token"
      with a
      match (s)-[u:USING]->(a)
      where s.state="active"
      with s
      create (s)-[ur:USING_ROOM]->(rm:Room {id:"$roomId"})
      return s,ur,rm
      """
    //  Logger.info("---associateRoomWithStream["+res+"]")
      return res
  }  

  def invalidateAllStreams(token: String) : String = {
      val res = s"""
      match (t:AuthenticationToken) where t.token="$token"
      with t
      match (t)-[u:USING]-(s)
      set s.state = "inactive"
      return s
      """
    //  Logger.info("---invalidateAllStreams["+res+"]")
      return res
  }

  def updateUserInformation(token: String, domId: String): String = {
      val res= s"""
      	  match (a)-[ht:HAS_TOKEN]-(b)
     	    where b.valid="true" and b.token="$token"
	        set a.domId = "$domId"
     	    return a.domId
     """
    // Logger.info("--updateUserInformation["+res+"]")
     return res

  }

}