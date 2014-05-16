package com.whatamidoing.services.xmpp


import org.jivesoftware._
import smack._

import smackx._
import commands._
import smackx.packet.DataForm
import com.typesafe.config.ConfigFactory

class AddHocCommands {


  def removeRoom(conn: XMPPConnection, roomJid: String, mucAdmin: String) {

    import com.whatamidoing.services.xmpp.AddVhostAdHocCommand
    val node = "room-remove"
    val setCommand = new AddVhostAdHocCommand(conn,node,mucAdmin)

    setCommand.execute()

    import org.jivesoftware.smackx.FormField
    import org.jivesoftware.smackx.Form
    import org.jivesoftware.smackx.packet.DataForm
    import org.jivesoftware.smackx.Form

    val submitDataForm = new DataForm(Form.TYPE_SUBMIT)
    val rn = new FormField("room-name")
    rn.setType(FormField.TYPE_TEXT_SINGLE)
    rn.setLabel("Room name")
    rn.addValue(roomJid)
    submitDataForm.addField(rn)


    val reason = new FormField("Reason")
    reason.setType(FormField.TYPE_TEXT_SINGLE)
    reason.setLabel("reason")
    reason.addValue("session ended")
    submitDataForm.addField(reason)

    val alt = new FormField("alternate-jid")
    alt.setType(FormField.TYPE_TEXT_SINGLE)
    alt.setLabel("Alternate room")
    submitDataForm.addField(alt)

    val submitForm: Form =  new Form(submitDataForm)
    setCommand.complete(submitForm)

    var answer: Form = setCommand.getForm()
    var its = answer.getFields
    while(its.hasNext()) {
      val it = its.next()
      System.out.println("answer["+it.toXML()+"]")
    }



  }
  def addNewVirtualHost(conn: XMPPConnection,newDomain: String) = {
    import com.whatamidoing.services.xmpp.AddVhostAdHocCommand

    val config = ConfigFactory.load()
    val xmppDomain = config.getString("xmpp.domain")
    val jid ="vhost-man@"+xmppDomain
    val node = "comp-repo-item-add"
    val setCommand = new AddVhostAdHocCommand(conn,node,jid)

    setCommand.execute()

    import org.jivesoftware.smackx.FormField
    import org.jivesoftware.smackx.Form
    import org.jivesoftware.smackx.packet.DataForm
    import org.jivesoftware.smackx.Form

    val submitDataForm = new DataForm(Form.TYPE_SUBMIT)
    val vhostField = new FormField("Domain name")
    vhostField.setType(FormField.TYPE_TEXT_SINGLE)
    vhostField.addValue(newDomain)
    submitDataForm.addField(vhostField)

    val enabledField = new FormField("Enabled")
    enabledField.setType(FormField.TYPE_BOOLEAN)
    enabledField.addValue("1")
    submitDataForm.addField(enabledField)

    val anonymousEnabledField = new FormField("Anonymous enabled")
    anonymousEnabledField.setType(FormField.TYPE_BOOLEAN)
    anonymousEnabledField.addValue("1")
    submitDataForm.addField(anonymousEnabledField)

    val inbandRegistration = new FormField("In-band registration")
    inbandRegistration.setType(FormField.TYPE_TEXT_SINGLE)
    inbandRegistration.addValue("1")
    submitDataForm.addField(inbandRegistration)

    val maxUsers = new FormField("Max users")
    maxUsers.setType(FormField.TYPE_TEXT_SINGLE)
    maxUsers.addValue("0")
    submitDataForm.addField(maxUsers)

    val otherParams = new FormField("Other parameters")
    otherParams.setType(FormField.TYPE_TEXT_SINGLE)
    otherParams.addValue("")
    submitDataForm.addField(otherParams)

    val commandMarker = new FormField("command-marker")
    commandMarker.setType(FormField.TYPE_HIDDEN)
    commandMarker.addValue("command-marker")
    submitDataForm.addField(commandMarker)

    val submitForm: Form =  new Form(submitDataForm)


    import org.jivesoftware.smackx.Form
    var answer: Form = setCommand.getForm()
    /*
    var its = answer.getFields
    while(its.hasNext()) {
      val it = its.next()
    }
    */
    setCommand.complete(submitForm)

    answer = setCommand.getForm()
    /*
    its = answer.getFields
    while(its.hasNext()) {
      val it = its.next()
      System.out.println("answer-new["+it.toXML()+"]")
    }
    */

  }

}

object AddHocCommands {

  def apply() = new AddHocCommands()
}