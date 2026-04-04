package krilovs.andrejs.chess.game

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler

class Handler: TextWebSocketHandler() {
  private val mapper = jacksonObjectMapper()
  private val sessions = mutableSetOf<WebSocketSession>()
  private val board = Board().apply { setupDefaultPiecePositions() }

  override fun afterConnectionEstablished(session: WebSocketSession) {
    sessions += session
    session.sendJson("INIT", mapOf("pieces" to board.getPieces(), "turn" to board.currentTurn))
  }

  override fun afterConnectionClosed(session: WebSocketSession, status: org.springframework.web.socket.CloseStatus) {
    sessions -= session
  }

  override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
    val data = mapper.readTree(message.payload)
    val type = data["type"]?.asText() ?: return

    when (type) {
      "GET_MOVES" -> handleGetMoves(session, data)
      "MOVE" -> handleMove(session, data)
    }
  }

  private fun handleGetMoves(session: WebSocketSession, data: JsonNode) {
    val coord = data["from"]?.asText()?.toCoordinates() ?: return

    val moves = board.getPiece(coord)
      ?.getAvailableMoveSquares(board)
      ?.map { "${it.file}${it.rank}" }
      ?: emptyList()

    session.sendJson("MOVES", mapOf("moves" to moves))
  }

  private fun handleMove(session: WebSocketSession, data: JsonNode) {
    val from = data["from"]?.asText()?.toCoordinates() ?: return
    val to = data["to"]?.asText()?.toCoordinates() ?: return
    val result = board.tryMove(from, to) ?: return

    if (result.isNotEmpty()) {
      return session.sendJson(
        "INVALID_MOVE",
        mapOf("availableMoves" to result.map { "${it.file}${it.rank}" })
      )
    }

    broadcast("STATE", mapOf("pieces" to board.getPieces(), "turn" to board.currentTurn.name))
  }

  private fun WebSocketSession.sendJson(type: String, payload: Map<String, Any?>) =
    sendMessage(TextMessage(mapper.writeValueAsString(payload + ("type" to type))))

  private fun broadcast(type: String, payload: Map<String, Any?>) {
    val json = mapper.writeValueAsString(payload + ("type" to type))
    sessions.forEach { it.sendMessage(TextMessage(json)) }
  }

  private fun String.toCoordinates(): Coordinates = Coordinates(this[0], this[1].digitToInt())
}