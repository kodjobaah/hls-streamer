package com.whatamidoing.actors.xmpp

import akka.actor.{ActorLogging, Actor, Props}

import models.Messages.CreateXMPPDomainMessage
import com.typesafe.config.ConfigFactory

class CreateXMPPDomain() extends Actor with ActorLogging {

  override def receive: Receive = {

    case CreateXMPPDomainMessage(domainId) =>

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


      // Create a connection to the jabber.org server.
      val config: ConnectionConfiguration = new ConnectionConfiguration(xmppIp, xmppPort.toInt, xmppDomain)
      config.setSASLAuthenticationEnabled(true)
      val conn: XMPPConnection = new XMPPConnection(config)
      try {
        conn.connect()
        conn.login(adminUserName, adminPassword)

        import com.whatamidoing.services.xmpp.AddHocCommands
        val sa = new AddHocCommands()
        sa.addNewVirtualHost(conn, domainId)

      } finally {
        conn.disconnect()
      }
      import models.Messages.Done
      sender ! Done(status = true)


    case _ =>
  }

  /**
   * If this child has been restarted due to an exception attempt redelivery
   * based on the message configured delay
   */
  override def preRestart(reason: Throwable, message: Option[Any]) {
    log.info("Scheduling email message to be sent after attempts:" + message.get)
    self ! message.get
  }

}

object CreateXMPPDomain {
  def props() = Props(classOf[CreateXMPPDomain])
}