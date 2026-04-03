package krilovs.andrejs.chess.piece

import krilovs.andrejs.chess.game.Board
import krilovs.andrejs.chess.game.BoardUtils
import krilovs.andrejs.chess.game.Color
import krilovs.andrejs.chess.game.Coordinates
import krilovs.andrejs.chess.game.CoordinatesShift

class Rook(color: Color, coordinates: Coordinates) : Piece(color, coordinates) {
  override fun getPieceMoves(): Set<CoordinatesShift> {
    return buildSet {
      (-7 .. 7).filter { it != 0 }.forEach {
        add(CoordinatesShift(it, 0))
        add(CoordinatesShift(0, it))
      }
    }
  }

  override fun isSquareAvailable(coord: Coordinates, board: Board): Boolean {
    return super.isSquareAvailable(coord, board) &&
      BoardUtils()
        .getVerticalCoordinatesInRange(coordinates, coord)
        .none { board.getPiece(it) != null } &&
      BoardUtils()
        .getHorizontalCoordinatesInRange(coordinates, coord)
        .none { board.getPiece(it) != null }
  }
}