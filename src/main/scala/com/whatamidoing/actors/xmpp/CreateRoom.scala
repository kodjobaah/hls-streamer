package com.whatamidoing.actors.xmpp

import akka.actor.Actor
import akka.actor.Props


import models.Messages.CreateXMPPRoomMessage
import com.typesafe.config.ConfigFactory

class CreateRoom() extends Actor {


  val config = ConfigFactory.load()
  val xmppDomain = config.getString("xmpp.domain")
  val xmppIp = config.getString("xmpp.ip")
  val xmppPort = config.getString("xmpp.port")
  val adminUserName = config.getString("xmpp.admin.username")
  val adminPassword = config.getString("xmpp.admin.password")

  override def receive: Receive = {

    case CreateXMPPRoomMessage(roomJid) =>

      import org.jivesoftware.smack.Connection
      import org.jivesoftware.smack.ConnectionConfiguration
      import org.jivesoftware.smack.XMPPConnection

      //      	   Connection.DEBUG_ENABLED = true
      import org.jivesoftware.smack.SmackConfiguration
      SmackConfiguration.setLocalSocks5ProxyPort(-1)

      // Create a connection to the jabber.org server.
      val config: ConnectionConfiguration = new ConnectionConfiguration(xmppIp, xmppPort.toInt, xmppDomain)
      config.setSASLAuthenticationEnabled(true)
      val conn: XMPPConnection = new XMPPConnection(config)
      try {
        conn.connect()
        conn.login(adminUserName, adminPassword)

        import com.whatamidoing.services.xmpp.BasicCreateRoom
        def basicCreateRoom = BasicCreateRoom(roomJid, conn)
        basicCreateRoom.createRoom


      } finally {
        conn.disconnect()
      }


    case _ =>


  }

}

object CreateRoom {

  def props() = Props(classOf[CreateRoom])
}