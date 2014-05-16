package com.whatamidoing.actors.xmpp

import akka.actor.Actor
import akka.actor.Props

import play.api.Logger

import models.Messages.RemoveXMPPRoomMessage
class RemoveXMPPRoom extends Actor {


     override def receive: Receive = {

      case RemoveXMPPRoomMessage(roomJid) => {
      	   import org.jivesoftware.smack.Connection
      	   import org.jivesoftware.smack.ConnectionConfiguration
      	   import org.jivesoftware.smack.XMPPConnection

      	   Connection.DEBUG_ENABLED = true

	   import play.api.Play
      	   implicit var currentPlay = Play.current
      	   val xmppDomain = Play.current.configuration.getString("xmpp.domain").get
      	   val xmppIp = Play.current.configuration.getString("xmpp.ip").get
      	   val xmppPort = Play.current.configuration.getString("xmpp.port").get
      	   val adminUserName = Play.current.configuration.getString("xmpp.admin.username").get
      	   val adminPassword = Play.current.configuration.getString("xmpp.admin.password").get


      	   // Create a connection to the jabber.org server.
      	   val config: ConnectionConfiguration = new ConnectionConfiguration(xmppIp,xmppPort.toInt,xmppDomain)
      	   config.setSASLAuthenticationEnabled(true)
      	   val conn: XMPPConnection = new XMPPConnection(config)
      	   import org.jivesoftware.smack.XMPPException
      	   try { 
      	       conn.connect()  
               Logger.info("---ABLE TO CONNECT:"+conn.isConnected())
      	       conn.login(adminUserName, adminPassword)
	       Logger.info("success:"+conn.isAuthenticated())
               Logger.info("---ABLE TO LOGIN")

               import com.whatamidoing.services.xmpp.AddHocCommands
	       val adc = new AddHocCommands()
	       adc.removeRoom(conn,roomJid)
      	    
     	    } finally {
               conn.disconnect()
	   }
      }
      case _ => {}

     }
}


object RemoveXMPPRoom {
    def props() = Props(new RemoveXMPPRoom())
}