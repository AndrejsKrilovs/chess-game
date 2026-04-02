package krilovs.andrejs.chess.game

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler

class Handler: TextWebSocketHandler() {
  private val sessions = mutableListOf<WebSocketSession>()
  private val mapper = jacksonObjectMapper()
  private val board = Board().apply { setupDefaultPiecePositions() }

  override fun afterConnectionEstablished(session: WebSocketSession) {
    sessions.add(session)
    val payload = mapper.writeValueAsString(
      mapOf(
        "type" to "INIT",
        "pieces" to board.pieces
      )
    )

    session.sendMessage(TextMessage(payload))
  }
}