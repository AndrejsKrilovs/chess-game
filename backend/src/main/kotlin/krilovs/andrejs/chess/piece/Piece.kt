package krilovs.andrejs.chess.piece

import krilovs.andrejs.chess.game.Board
import krilovs.andrejs.chess.game.Color
import krilovs.andrejs.chess.game.Coordinates
import krilovs.andrejs.chess.game.CoordinatesShift

abstract class Piece(
  val color: Color,
  var coordinates: Coordinates
) {
  val type get() = this::class.simpleName ?: "UNKNOWN"

  open fun getAvailableMoveSquares(board: Board): Set<Coordinates> =
    getPieceMoves()
      .mapNotNull { coordinates.shift(it) }
      .filter { isSquareAvailable(it, board) }
      .toSet()

  protected abstract fun getPieceMoves(): Set<CoordinatesShift>

  private fun isSquareAvailable(coord: Coordinates, board: Board): Boolean {
    return board.getPiece(coord) == null || board.getPiece(coord)?.color != color
  }
}