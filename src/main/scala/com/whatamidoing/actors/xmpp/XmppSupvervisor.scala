package com.whatamidoing.actors.xmpp

import akka.actor._

class XmppSupervisor() extends Actor with ActorLogging {


  val system = ActorSystem("whatamidoing-system-xmpp")
  import models.Messages._

  import akka.actor.OneForOneStrategy
  import akka.actor.SupervisorStrategy._

  override val supervisorStrategy =
    OneForOneStrategy(maxNrOfRetries = 3) {
      case _: Exception                =>
        log debug "xmpp problem -- restarting"
        Restart
    }


  val createDomainAdHocCommands = scala.collection.mutable.Map[String, ActorRef]()

  override def receive: Receive = {

    case removeRoom: RemoveXMPPRoomMessage =>
      import com.whatamidoing.actors.xmpp.RemoveXMPPRoom
      val removeRoomAddHocCommand = context.actorOf(RemoveXMPPRoom.props(), "xmpp-remove-"+removeRoom.roomJid)
      removeRoomAddHocCommand ! removeRoom

    case createGroup: CreateXMPPGroupMessage =>
      import com.whatamidoing.actors.xmpp.CreateXMPPGroup
      val createDomainAdHocCommand = context.actorOf(CreateXMPPGroup.props(), "xmpp-creategroup--"+createGroup.roomJid)
      //  createDomainAdHocCommands += createGroup.roomJid -> createDomainAdHocCommand
      createDomainAdHocCommand ! createGroup

    case createRoom: CreateXMPPRoomMessage =>
      import com.whatamidoing.actors.xmpp.CreateRoom
      val createRoomActor = context.actorOf(CreateRoom.props(), "xmpp-create-room-"+createRoom.roomJid)
      createRoomActor !  createRoom

    case createDomain: CreateXMPPDomainMessage =>
      import com.whatamidoing.actors.xmpp.CreateXMPPDomain
      import scala.concurrent.Future
      import scala.concurrent.duration._
      import akka.util.Timeout
      implicit val timeout = Timeout(20 seconds)
      import akka.pattern.ask

      val createDomainAdHocCommand = context.actorOf(CreateXMPPDomain.props(), "xmpp-create-domain-"+createDomain.domain)
      val response: Future[Any] = ask(createDomainAdHocCommand, createDomain).mapTo[Any]
      import scala.concurrent.Await
      var res = Await.result(response, 10 seconds) match {
        case results: Done => {
          results
        }


      }
      sender ! res

  }


}

object XmppSupervisor {

  def props() = Props(classOf[XmppSupervisor])


}