package com.whatamidoing.actors.xmpp

import akka.actor.Actor
import akka.actor.Props


import models.Messages.RemoveXMPPRoomMessage
import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory

class RemoveXMPPRoom extends Actor {

  def log = LoggerFactory.getLogger("RemoveXMPPRoom")


  override def receive: Receive = {

    case RemoveXMPPRoomMessage(roomJid) =>
      import org.jivesoftware.smack.ConnectionConfiguration
      import org.jivesoftware.smack.XMPPConnection

      //      	   Connection.DEBUG_ENABLED = true
      import org.jivesoftware.smack.SmackConfiguration
      SmackConfiguration.setLocalSocks5ProxyPort(-1)



      val configFile = ConfigFactory.load()
      val xmppDomain = configFile.getString("xmpp.domain")
      val xmppIp = configFile.getString("xmpp.ip")
      val xmppPort = configFile.getString("xmpp.port")
      val adminUserName = configFile.getString("xmpp.admin.username")
      val adminPassword = configFile.getString("xmpp.admin.password")
      val mucAdmin = configFile.getString("xmpp.muc.admin")


      // Create a connection to the jabber.org server.
      val config: ConnectionConfiguration = new ConnectionConfiguration(xmppIp,xmppPort.toInt,xmppDomain)
      config.setSASLAuthenticationEnabled(true)
      val conn: XMPPConnection = new XMPPConnection(config)
      try {
        conn.connect()
        conn.login(adminUserName, adminPassword)

        import com.whatamidoing.services.xmpp.AddHocCommands
        val adc = new AddHocCommands()
        adc.removeRoom(conn,roomJid,mucAdmin)

      } finally {
        conn.disconnect()
      }

    case _ =>

  }
}


object RemoveXMPPRoom {
  def props() = Props(classOf[RemoveXMPPRoom])
}