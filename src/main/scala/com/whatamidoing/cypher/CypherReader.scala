package com.whatamidoing.cypher


object CypherReader {

  def searchForUser(user: String): String = {
    val search = s"""
    		match (a:User)
    		where a.email = "$user" 
    		return a.password as password, a.email as email
    		"""
    return search
  }

  def findActiveStreamForToken(token: String) : String = {
    
    val res=s"""
    		match (a:AuthenticationToken)
    		where a.token="$token" and a.valid="true"
    		with a
    		match (a)-[r]-(b)
    		where type(r) = 'USING' and b.state='active'
    		return b.name as name
      
      """
      return res
    
  }
  

 def fetchUserDetails(token: String): String = {
     val res= s"""
     match (a:AuthenticationToken) where a.token="$token" and a.valid="true"
     match (b)-[r:HAS_TOKEN]-(a)
     return b.email as email, b.firstName as firstName , b.lastName as lastName
     """
    // Logger.info("--fecthUserDetails["+res+"]")
     return res
  }


 def fetchUserInformation(token: String): String = {

     val res= s"""
     	 match (a:AuthenticationToken) where a.token="$token"
     	 match (b)-[r:HAS_TOKEN]-(a)
     	 return b.email as email, b.firstName as firstName , b.lastName as lastName, b.domId as domId
     """
   //  Logger.info("--fecthUserInformation["+res+"]")
     return res
  }


 def getRoomJid(token: String): String = {

    val res =s"""
       match (a:AuthenticationToken) where a.token = "$token"
      with a
      match s-[u:USING]->a
      where s.state="active"
      with s
      match (s)-[ur:USING_ROOM]-(rm:Room)
      return distinct rm.id as jid
      """
     // Logger.info("--getGroupJid["+res+"]")
    return res
  }

  def getValidToken(token: String): String = {

    val res=s"""
    		match (token:AuthenticationToken)
    		where token.token="$token" and token.valid="true"
    		return token.token as token

      """
    return res

  }

  def getRoomJidForStream(stream: String): String = {
      
      val res = s"""
      match (s:Stream) where s.name ="$stream"
      with s
      match (s)-[ur:USING_ROOM]-(rm:Room)
      return distinct rm.id as jid
      """
      //Logger.info("--getRoomJidForStream["+res+"]")
      return res

  }


}