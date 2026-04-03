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
        "pieces" to board.pieces.values,
        "availableMoves" to board.pieces[Coordinates('b', 8)]?.getAvailableMoveSquares(board)
      )
    )

    session.sendMessage(TextMessage(payload))
  }

  override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
    val data = mapper.readTree(message.payload)

    if (data["type"].asText() == "GET_MOVES") {
      val from = data["from"].asText()
      val coord = Coordinates(from[0], from[1].digitToInt())
      val moves = board.pieces[coord]?.getAvailableMoveSquares(board)?.map { "${it.file}${it.rank}" }

      val payload = mapper.writeValueAsString(
        mapOf(
          "type" to "MOVES",
          "moves" to moves
        )
      )

      session.sendMessage(TextMessage(payload))
    }
  }
}