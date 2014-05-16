package com.whatamidoing.services.xmpp

import org.jivesoftware.smack.Connection
import org.jivesoftware.smack.ConnectionConfiguration
import org.jivesoftware.smack.XMPPConnection


class BasicCreateRoom(val roomJid: String, val conn: XMPPConnection) {


  def createRoom() = {
    System.setProperty("smack.debugEnabled", "true");
    // Create a connection to the jabber.org server.
    import org.jivesoftware.smack.XMPPException
    try {
      conn.connect()
      System.out.println("---ABLE TO CONNECT:" + conn.isConnected())
      conn.login("admin", "tigase")
      System.out.println("success:" + conn.isAuthenticated())
      System.out.println("---ABLE TO LOGIN")

      import org.jivesoftware.smack.packet.Presence
      val presence = new Presence(Presence.Type.available)
      presence.setPriority(0)
      presence.setTo("room220@muc.my/JustMe")

      import org.jivesoftware.smackx.packet.MUCInitialPresence
      val init = new MUCInitialPresence()
      presence.addExtension(init)
      conn.sendPacket(presence)

    } catch {
      case e: XMPPException => {
        conn.disconnect()
        e.printStackTrace()
      }
    }

  }


}

object BasicCreateRoom {

  def apply(roomJid: String, conn: XMPPConnection) = new BasicCreateRoom(roomJid, conn)
}