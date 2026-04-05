package krilovs.andrejs.chess.game

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import krilovs.andrejs.chess.piece.Piece
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler

class Handler : TextWebSocketHandler() {
  private val mapper = jacksonObjectMapper()
  private val sessions = mutableSetOf<WebSocketSession>()

  private val board = Board().apply {
    loadFromFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w - - 0 1")
  }

  private val attackService = AttackService(board)
  private val rules = GameRules(board, attackService)

  override fun afterConnectionEstablished(session: WebSocketSession) {
    sessions += session
    session.sendJson("INIT", mapOf(
      "pieces" to board.getPieces().map { it.toDto() },
      "turn" to board.currentTurn
    ))
  }

  override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
    sessions -= session
  }

  override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
    val data = mapper.readTree(message.payload)
    when (data["type"]?.asText()) {
      "GET_MOVES" -> handleGetMoves(session, data)
      "MOVE" -> handleMove(session, data)
    }
  }

  private fun handleGetMoves(session: WebSocketSession, data: JsonNode) {
    try {
      val from = data["from"]?.asText()?.toSquare() ?: return
      val moves = board.generateMovesForSquare(from)
        .filter { rules.isMoveSafe(it) }
        .map { it.to.toCoord() }

      session.sendJson("MOVES", mapOf("moves" to moves))
    }
    catch (e: Exception) {
      e.printStackTrace()
    }
  }

  private fun handleMove(session: WebSocketSession, data: JsonNode) {
    val from = data["from"]?.asText()?.toSquare() ?: return
    val to = data["to"]?.asText()?.toSquare() ?: return
    val moves = board.generateMovesForSquare(from)

    val move = moves.firstOrNull { it.to == to && rules.isMoveSafe(it) }
      ?: return session.sendJson(
        "INVALID_MOVE",
        mapOf("availableMoves" to moves
          .filter { rules.isMoveSafe(it) }
          .map { it.to.toCoord() })
      )

    board.makeMove(move)
    broadcast(
      "STATE",
      mapOf(
        "pieces" to board.getPieces().map { it.toDto() },
        "turn" to board.currentTurn.name,
        "state" to rules.getGameState(board.currentTurn).name
      )
    )
  }

  private fun Piece.toDto() = mapOf(
    "type" to type,
    "color" to color.name,
    "coordinates" to mapOf(
      "file" to ('a' + (square % 8)).toString(),
      "rank" to (square / 8) + 1
    )
  )

  private fun Int.toCoord(): String {
    val file = 'a' + (this % 8)
    val rank = (this / 8) + 1
    return "$file$rank"
  }

  private fun String.toSquare(): Int {
    val file = this[0] - 'a'
    val rank = this[1].digitToInt() - 1
    return rank * 8 + file
  }

  private fun WebSocketSession.sendJson(type: String, payload: Map<String, Any?>) {
    sendMessage(TextMessage(mapper.writeValueAsString(payload + ("type" to type))))
  }

  private fun broadcast(type: String, payload: Map<String, Any?>) {
    val json = mapper.writeValueAsString(payload + ("type" to type))
    sessions.forEach { it.sendMessage(TextMessage(json)) }
  }
}