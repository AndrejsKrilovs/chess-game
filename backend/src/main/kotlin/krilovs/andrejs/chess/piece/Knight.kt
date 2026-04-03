package krilovs.andrejs.chess.piece

import krilovs.andrejs.chess.game.Color
import krilovs.andrejs.chess.game.Coordinates
import krilovs.andrejs.chess.game.CoordinatesShift

class Knight(color: Color, coordinates: Coordinates) : Piece(color, coordinates) {
  override fun getPieceMoves(): Set<CoordinatesShift> =
    setOf(
      CoordinatesShift(1, 2),
      CoordinatesShift(2, 1),
      CoordinatesShift(2, -1),
      CoordinatesShift(1, -2),
      CoordinatesShift(-1, -2),
      CoordinatesShift(-2, -1),
      CoordinatesShift(-2, 1),
      CoordinatesShift(-1, 2)
    )
}