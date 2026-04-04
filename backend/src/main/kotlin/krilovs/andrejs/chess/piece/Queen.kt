package krilovs.andrejs.chess.piece

import krilovs.andrejs.chess.game.Color
import krilovs.andrejs.chess.game.Coordinates
import krilovs.andrejs.chess.game.CoordinatesShift
import krilovs.andrejs.chess.game.SlidingType

class Queen(color: Color, coordinates: Coordinates) :
  SlidingPiece(color, coordinates, SlidingType.BOTH) {

  override fun getPieceMoves() = buildSet {
    (-7..7).filter { it != 0 }.forEach {
      add(CoordinatesShift(it, it))
      add(CoordinatesShift(it, -it))
      add(CoordinatesShift(it, 0))
      add(CoordinatesShift(0, it))
    }
  }
}