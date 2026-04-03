package krilovs.andrejs.chess.piece

import krilovs.andrejs.chess.game.Board
import krilovs.andrejs.chess.game.Color
import krilovs.andrejs.chess.game.Coordinates
import krilovs.andrejs.chess.game.CoordinatesShift

class Pawn(color: Color, coordinates: Coordinates) : Piece(color, coordinates) {
  override fun getPieceMoves(): Set<CoordinatesShift> {
    val dir = if (color == Color.WHITE) 1 else -1
    val startRank = if (color == Color.WHITE) 2 else 7

    return buildSet {
      add(CoordinatesShift(0, dir))
      if (coordinates.rank == startRank) {
        add(CoordinatesShift(0, 2 * dir))
      }
    }
  }

  override fun getAvailableMoveSquares(board: Board): Set<Coordinates> {
    val result = mutableSetOf<Coordinates>()
    val dir = if (color == Color.WHITE) 1 else -1
    val startRank = if (color == Color.WHITE) 2 else 7
    val oneStep = coordinates.shift(CoordinatesShift(0, dir))

    if (oneStep != null && board.getPiece(oneStep) == null) {
      result.add(oneStep)
      val twoStep = coordinates.shift(CoordinatesShift(0, 2 * dir))
      if (coordinates.rank == startRank &&
        twoStep != null &&
        board.getPiece(twoStep) == null
      ) {
        result.add(twoStep)
      }
    }

    listOf(1, -1).forEach { dx ->
      val target = coordinates.shift(CoordinatesShift(dx, dir)) ?: return@forEach
      val piece = board.getPiece(target)

      if (piece != null && piece.color != color) {
        result.add(target)
      }
    }

    return result
  }
}