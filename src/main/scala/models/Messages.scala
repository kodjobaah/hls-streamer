package models

import play.api.libs.json.JsValue

object Messages {
  
    case class PerformReadOperation(f: () => Neo4jResult)
    case class ReadOperationResult(val result: Neo4jResult)

    case class PerformOperation(f: () => Neo4jResult)
    case class WriteOperationResult(val result: Neo4jResult)
    
    case class RTMPMessage(val message: String)
    case class RTMPCreateStream(val token: String, val streamId: String = "")
    case class StopVideo()

    case class EncodeFrame(frame: String)
    case class EndTransmission()
    case class ProblemsEncoding()

    case class CreateXMPPGroupMessage(val roomJid: String ="", val token: String ="")
    case class CreateXMPPDomainMessage(val domain: String ="")
    case class CreateXMPPRoomMessage(val roomJid: String = "")
    case class RemoveXMPPRoomMessage(val roomJid: String = "")
    case class Done(val status: Boolean = false)


}