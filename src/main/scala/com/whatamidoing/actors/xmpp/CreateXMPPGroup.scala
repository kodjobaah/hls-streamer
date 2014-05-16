package com.whatamidoing.actors.xmpp

import akka.actor.Actor
import akka.actor.Props

import models.Messages.CreateXMPPGroupMessage

import org.jivesoftware.smackx.muc.RoomInfo
import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory


class CreateXMPPGroup() extends Actor {


  val log = LoggerFactory.getLogger("CreateXMPPGroupå")
  override def receive: Receive = {

    case CreateXMPPGroupMessage(roomJid, token) =>

      import org.jivesoftware.smack.Connection
      import org.jivesoftware.smack.ConnectionConfiguration
      import org.jivesoftware.smack.XMPPConnection

      //Connection.DEBUG_ENABLED = true

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

        // Create a MultiUserChat using a Connection for a room
        import org.jivesoftware.smackx.muc.MultiUserChat
        val muc: MultiUserChat = new MultiUserChat(conn, roomJid)

        // Create the room
        muc.create("initialCreation")

        // Send an empty room configuration form which indicates that we want
        // an instant room
        import org.jivesoftware.smackx.Form
        import org.jivesoftware.smackx.FormField

        // Get the the room's configuration form
        val form: Form = muc.getConfigurationForm
        // Create a new form to submit based on the original form
        val submitForm: Form = form.createAnswerForm()

        // Add default answers to the form to submit
        import scala.collection.JavaConversions._
        val fields = form.getFields
        for (field <- fields) {
          if (!FormField.TYPE_HIDDEN.equals(field.getType) && field.getVariable != null) {
            // Sets the default value as the answer
            submitForm.setDefaultAnswer(field.getVariable)
          }
        }
        // make the room peristent
        submitForm.setAnswer("muc#roomconfig_persistentroom", true)
        // Send the completed form (with default values) to the server to configure the room
        muc.sendConfigurationForm(submitForm)

        //var info: RoomInfo = MultiUserChat.getRoomInfo(conn, roomJid);

      } finally {
        conn.disconnect()
      }


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

object CreateXMPPGroup {

  def props() = Props(classOf[CreateXMPPGroup])

}