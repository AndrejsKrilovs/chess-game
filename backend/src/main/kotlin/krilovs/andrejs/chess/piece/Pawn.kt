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
      add(CoordinatesShift(1, dir))
      add(CoordinatesShift(-1, dir))
      if (coordinates.rank == startRank) {
        add(CoordinatesShift(0, 2 * dir))
      }
    }
  }

  override fun isSquareAvailable(coord: Coordinates, board: Board): Boolean {
    val target = board.getPiece(coord)
    return if (coordinates.file == coord.file) { target == null } else { target?.color != color }
  }

  override fun getAttackedSquares(board: Board): Set<Coordinates> {
    val dir = if (color == Color.WHITE) 1 else -1
    return listOf(1, -1)
      .mapNotNull { coordinates.shift(CoordinatesShift(it, dir)) }
      .toSet()
  }
}