package com.whatamidoing.services.xmpp


import org.jivesoftware._
import smack._

import smackx._
import commands._
import smackx.packet.DataForm

class ConfigRoomAddHocCommands {


      def configDefaultRoom(conn: XMPPConnection)  = {
      	  import com.whatamidoing.services.xmpp.AddVhostAdHocCommand
     	  val jid ="muc@my"
     	  val node = "default-room-config"
     	  val setCommand = new AddVhostAdHocCommand(conn,node,jid)

     	  import org.jivesoftware.smackx.FormField
     	  import org.jivesoftware.smackx.Form
     	  import org.jivesoftware.smackx.packet.DataForm
     	  import org.jivesoftware.smackx.Form

         val submitDataForm = new DataForm(Form.TYPE_SUBMIT)

	     val rn = new FormField("muc#roomconfig_roomname")
       	     rn.setType(FormField.TYPE_TEXT_SINGLE)
	     rn.addValue("room9")
	     rn.setLabel("Natural-Language Room Name")
	     submitDataForm.addField(rn)

	     val rd = new FormField("muc#roomconfig_roomdesc")
	     rd.setLabel("Short Description of Room")
	     rd.setType(FormField.TYPE_TEXT_SINGLE)
	     rd.addValue("room description")
	     submitDataForm.addField(rd)


	     val persistRoom = new FormField("muc#roomconfig_persistentroom")
	     persistRoom.addValue("1")
	     persistRoom.setLabel("Make Room Persistent?")
	     persistRoom.setType(FormField.TYPE_BOOLEAN)
	     submitDataForm.addField(persistRoom)

	     val pr = new FormField("muc#roomconfig_publicroom")
	     pr.addValue("0")
	     pr.setLabel("Make Room Publicly Searchable?")	
	     pr.setType(FormField.TYPE_BOOLEAN)
	     submitDataForm.addField(pr)

	     val mr = new FormField("muc#roomconfig_moderatedroom")
    	     mr.addValue("0")
	     mr.setLabel("Make Room Moderated?")
	     mr.setType(FormField.TYPE_BOOLEAN)
	     submitDataForm.addField(mr)

	     val mo = new FormField("muc#roomconfig_membersonly")
	     mo.addValue("0")
	     mo.setLabel("Make Room Members Only?")
	     mo.setType(FormField.TYPE_BOOLEAN)
	     submitDataForm.addField(mo)

	     val ppr = new FormField("muc#roomconfig_passwordprotectedroom")
	     ppr.addValue("0")
	     ppr.setLabel("Password Required to Enter?")
	     ppr.setType(FormField.TYPE_BOOLEAN)
	     submitDataForm.addField(ppr)

	     val rs = new FormField("muc#roomconfig_roomsecret")
	     rs.addValue("caldronburn")
	     rs.setLabel("Password")
	     rs.setType(FormField.TYPE_TEXT_SINGLE)
	     submitDataForm.addField(rs)

	     val anonymity = new FormField("muc#roomconfig_anonymity")
	     anonymity.setLabel("Room anonymity level:")
	     anonymity.addValue("fullanonymous")
	     anonymity.setType(FormField.TYPE_TEXT_SINGLE)

	     val cs = new FormField("muc#roomconfig_changesubject")
	     cs.addValue("0")
	     cs.setLabel("Allow Occupants to Change Subject?")
	     cs.setType(FormField.TYPE_BOOLEAN)
	     submitDataForm.addField(cs)

	     val el = new FormField("muc#roomconfig_enablelogging")
	     el.setLabel("Enable Public Logging?")
	     el.setType(FormField.TYPE_BOOLEAN)
	     el.addValue("0")
	     submitDataForm.addField(el)

	     val lf = new FormField("logging_format")
	     lf.setType(FormField.TYPE_TEXT_SINGLE)
	     lf.setLabel("Logging format")
	     lf.addValue("html")

	     val mu = new FormField("muc#roomconfig_maxusers")
	     mu.addValue("0")
	     submitDataForm.addField(mu)

	     val hf = new FormField("muc#maxhistoryfetch")
	     hf.setLabel("Maximum Number of History Messages Returned by Room")
	     hf.setType(FormField.TYPE_TEXT_SINGLE)
	     hf.addValue("50")
	     submitDataForm.addField(hf)

	     val whois = new FormField("muc#roomconfig_whois")
	     whois.addValue("moderators")
	     submitDataForm.addField(whois)

	     val radmins = new FormField("muc#roomconfig_roomadmins")
	     hf.addValue("admin@my")
	     submitDataForm.addField(radmins)

      	     val submitForm: Form =  new Form(submitDataForm)
  
      	     setCommand.execute()
      	     import org.jivesoftware.smackx.Form
      	     var answer: Form = setCommand.getForm()
      	     var its = answer.getFields
      	     while(its.hasNext()) {
       	        val it = its.next()
       	     	System.out.println("answer["+it.toXML()+"]")
      	     }
    	     setCommand.complete(submitForm)
      }

      def createRoom(conn: XMPPConnection)  = {
      	  import com.whatamidoing.services.xmpp.AddVhostAdHocCommand
     	  val jid ="muc@my"
     	  val node = "default-room-config"
     	  val setCommand = new AddVhostAdHocCommand(conn,node,jid)

     	  import org.jivesoftware.smackx.FormField
     	  import org.jivesoftware.smackx.Form
     	  import org.jivesoftware.smackx.packet.DataForm
     	  import org.jivesoftware.smackx.Form

         val submitDataForm = new DataForm(Form.TYPE_SUBMIT)

	     val rn = new FormField("muc#roomconfig_roomname")
       	     rn.setType(FormField.TYPE_TEXT_SINGLE)
	     rn.addValue("room9")
	     rn.setLabel("Natural-Language Room Name")
	     submitDataForm.addField(rn)

	     val rd = new FormField("muc#roomconfig_roomdesc")
	     rd.setLabel("Short Description of Room")
	     rd.setType(FormField.TYPE_TEXT_SINGLE)
	     rd.addValue("room description")
	     submitDataForm.addField(rd)


	     val persistRoom = new FormField("muc#roomconfig_persistentroom")
	     persistRoom.addValue("1")
	     persistRoom.setLabel("Make Room Persistent?")
	     persistRoom.setType(FormField.TYPE_BOOLEAN)
	     submitDataForm.addField(persistRoom)

	     val pr = new FormField("muc#roomconfig_publicroom")
	     pr.addValue("0")
	     pr.setLabel("Make Room Publicly Searchable?")	
	     pr.setType(FormField.TYPE_BOOLEAN)
	     submitDataForm.addField(pr)

	     val mr = new FormField("muc#roomconfig_moderatedroom")
    	     mr.addValue("0")
	     mr.setLabel("Make Room Moderated?")
	     mr.setType(FormField.TYPE_BOOLEAN)
	     submitDataForm.addField(mr)

	     val mo = new FormField("muc#roomconfig_membersonly")
	     mo.addValue("0")
	     mo.setLabel("Make Room Members Only?")
	     mo.setType(FormField.TYPE_BOOLEAN)
	     submitDataForm.addField(mo)

	     val ppr = new FormField("muc#roomconfig_passwordprotectedroom")
	     ppr.addValue("0")
	     ppr.setLabel("Password Required to Enter?")
	     ppr.setType(FormField.TYPE_BOOLEAN)
	     submitDataForm.addField(ppr)

	     val rs = new FormField("muc#roomconfig_roomsecret")
	     rs.addValue("caldronburn")
	     rs.setLabel("Password")
	     rs.setType(FormField.TYPE_TEXT_SINGLE)
	     submitDataForm.addField(rs)

	     val anonymity = new FormField("muc#roomconfig_anonymity")
	     anonymity.setLabel("Room anonymity level:")
	     anonymity.addValue("fullanonymous")
	     anonymity.setType(FormField.TYPE_TEXT_SINGLE)

	     val cs = new FormField("muc#roomconfig_changesubject")
	     cs.addValue("0")
	     cs.setLabel("Allow Occupants to Change Subject?")
	     cs.setType(FormField.TYPE_BOOLEAN)
	     submitDataForm.addField(cs)

	     val el = new FormField("muc#roomconfig_enablelogging")
	     el.setLabel("Enable Public Logging?")
	     el.setType(FormField.TYPE_BOOLEAN)
	     el.addValue("0")
	     submitDataForm.addField(el)

	     val lf = new FormField("logging_format")
	     lf.setType(FormField.TYPE_TEXT_SINGLE)
	     lf.setLabel("Logging format")
	     lf.addValue("html")

	     val mu = new FormField("muc#roomconfig_maxusers")
	     mu.addValue("0")
	     submitDataForm.addField(mu)

	     val hf = new FormField("muc#maxhistoryfetch")
	     hf.setLabel("Maximum Number of History Messages Returned by Room")
	     hf.setType(FormField.TYPE_TEXT_SINGLE)
	     hf.addValue("50")
	     submitDataForm.addField(hf)

	     val whois = new FormField("muc#roomconfig_whois")
	     whois.addValue("moderators")
	     submitDataForm.addField(whois)

	     val radmins = new FormField("muc#roomconfig_roomadmins")
	     hf.addValue("admin@my")
	     submitDataForm.addField(radmins)

      	     val submitForm: Form =  new Form(submitDataForm)
  
      	     setCommand.execute()
      	     import org.jivesoftware.smackx.Form
      	     var answer: Form = setCommand.getForm()
      	     var its = answer.getFields
      	     while(its.hasNext()) {
       	        val it = its.next()
       	     	System.out.println("answer["+it.toXML()+"]")
      	     }
    	     setCommand.complete(submitForm)
      }

      def createRoom(conn: XMPPConnection, roomJid: String, roomName: String, ownerJid: String)  = {
          System.out.println("creating room")
      	  import com.whatamidoing.services.xmpp.AddVhostAdHocCommand
     	  val jid ="muc@my"
     	  val node = "add-new-room"
     	  val setCommand = new AddVhostAdHocCommand(conn,node,jid)

     	  import org.jivesoftware.smackx.FormField
     	  import org.jivesoftware.smackx.Form
     	  import org.jivesoftware.smackx.packet.DataForm
     	  import org.jivesoftware.smackx.Form

         val submitDataForm = new DataForm(Form.TYPE_SUBMIT)

	     val oJid = new FormField("owner-jid")
       	     oJid.setType(FormField.TYPE_TEXT_SINGLE)
	     oJid.addValue(ownerJid)
	     oJid.setLabel("Owner Jid")
	     submitDataForm.addField(oJid)

	     val rmJid = new FormField("room-jid")
       	     rmJid.setType(FormField.TYPE_TEXT_SINGLE)
	     rmJid.addValue(roomJid)
	     rmJid.setLabel("Room Jid")
	     submitDataForm.addField(rmJid)

	     val rn = new FormField("muc#roomconfig_roomname")
       	     rn.setType(FormField.TYPE_TEXT_SINGLE)
	     rn.addValue(roomName)
	     rn.setLabel("Natural-Language Room Name")
	     submitDataForm.addField(rn)

	     val rd = new FormField("muc#roomconfig_roomdesc")
	     rd.setLabel("Short Description of Room")
	     rd.setType(FormField.TYPE_TEXT_SINGLE)
	     rd.addValue("room description")
	     submitDataForm.addField(rd)

	     val persistRoom = new FormField("muc#roomconfig_persistentroom")
	     persistRoom.addValue("1")
	     persistRoom.setLabel("Make Room Persistent?")
	     persistRoom.setType(FormField.TYPE_BOOLEAN)
	     submitDataForm.addField(persistRoom)

	     val pr = new FormField("muc#roomconfig_publicroom")
	     pr.addValue("0")
	     pr.setLabel("Make Room Publicly Searchable?")	
	     pr.setType(FormField.TYPE_BOOLEAN)
	     submitDataForm.addField(pr)

	     val mr = new FormField("muc#roomconfig_moderatedroom")
    	     mr.addValue("0")
	     mr.setLabel("Make Room Moderated?")
	     mr.setType(FormField.TYPE_BOOLEAN)
	     submitDataForm.addField(mr)

	     val mo = new FormField("muc#roomconfig_membersonly")
	     mo.addValue("0")
	     mo.setLabel("Make Room Members Only?")
	     mo.setType(FormField.TYPE_BOOLEAN)
	     submitDataForm.addField(mo)

	     val ppr = new FormField("muc#roomconfig_passwordprotectedroom")
	     ppr.addValue("0")
	     ppr.setLabel("Password Required to Enter?")
	     ppr.setType(FormField.TYPE_BOOLEAN)
	     submitDataForm.addField(ppr)

	     val rs = new FormField("muc#roomconfig_roomsecret")
	     rs.addValue("caldronburn")
	     rs.setLabel("Password")
	     rs.setType(FormField.TYPE_TEXT_SINGLE)
	     submitDataForm.addField(rs)

	     val anonymity = new FormField("muc#roomconfig_anonymity")
	     anonymity.setLabel("Room anonymity level:")
	     anonymity.addValue("fullanonymous")
	     anonymity.setType(FormField.TYPE_TEXT_SINGLE)

	     val cs = new FormField("muc#roomconfig_changesubject")
	     cs.addValue("0")
	     cs.setLabel("Allow Occupants to Change Subject?")
	     cs.setType(FormField.TYPE_BOOLEAN)
	     submitDataForm.addField(cs)

	     val el = new FormField("muc#roomconfig_enablelogging")
	     el.setLabel("Enable Public Logging?")
	     el.setType(FormField.TYPE_BOOLEAN)
	     el.addValue("0")
	     submitDataForm.addField(el)

	     val lf = new FormField("logging_format")
	     lf.setType(FormField.TYPE_TEXT_SINGLE)
	     lf.setLabel("Logging format")
	     lf.addValue("html")

	     val mu = new FormField("muc#roomconfig_maxusers")
	     mu.addValue("0")
	     submitDataForm.addField(mu)

	     val hf = new FormField("muc#maxhistoryfetch")
	     hf.setLabel("Maximum Number of History Messages Returned by Room")
	     hf.setType(FormField.TYPE_TEXT_SINGLE)
	     hf.addValue("50")
	     submitDataForm.addField(hf)

	     val whois = new FormField("muc#roomconfig_whois")
	     whois.addValue("moderators")
	     submitDataForm.addField(whois)

	     val radmins = new FormField("muc#roomconfig_roomadmins")
	     hf.addValue("admin@my")
	     submitDataForm.addField(radmins)

      	     val submitForm: Form =  new Form(submitDataForm)
  
      	     setCommand.execute()
      	     import org.jivesoftware.smackx.Form
      	     var answer: Form = setCommand.getForm()
      	     var its = answer.getFields
      	     while(its.hasNext()) {
       	        val it = its.next()
       	     	System.out.println("answer["+it.toXML()+"]")
      	     }
    	     setCommand.complete(submitForm)
      }


}