package com.whatamidoing.services.xmpp

import org.jivesoftware.smackx.commands.AdHocCommand
import org.jivesoftware.smackx.commands.RemoteCommand
import org.jivesoftware.smack.XMPPConnection;

/**
* {{{<iq type="set"
    to="vhost-man@existing.domain.com"
    id="aacba">
  <command xmlns="http://jabber.org/protocol/commands"
           node="VHOSTS_UPDATE">
    <x xmlns="jabber:x:data" type="submit">
      <field type="text-single"
             var="VHost">
        <value>new-virt.domain.com</value>
      </field>
      <field type="list-single"
             var="Enabled">
        <value>true</value>
      </field>
*    </x>
*  </command>
*</iq>
*}}}
*/
class AddVhostAdHocCommand(conn: XMPPConnection,node: String, jid: String) extends RemoteCommand(conn,node,jid) {


}