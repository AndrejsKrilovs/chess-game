package krilovs.andrejs.chess.piece

import krilovs.andrejs.chess.game.Board
import krilovs.andrejs.chess.game.BoardUtils
import krilovs.andrejs.chess.game.Color
import krilovs.andrejs.chess.game.Coordinates
import krilovs.andrejs.chess.game.CoordinatesShift

class Bishop(color: Color, coordinates: Coordinates) : Piece(color, coordinates) {
  override fun getPieceMoves(): Set<CoordinatesShift> {
    return buildSet {
      (-7 .. 7).filter { it != 0 }.forEach {
        add(CoordinatesShift(it, it))
        add(CoordinatesShift(it, -it))
      }
    }
  }

  override fun isSquareAvailable(coord: Coordinates, board: Board): Boolean {
    return super.isSquareAvailable(coord, board) &&
      BoardUtils()
        .getDiagonalCoordinatesInRange(coordinates, coord)
        .none { board.getPiece(it) != null }
  }
}