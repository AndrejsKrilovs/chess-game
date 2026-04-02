package krilovs.andrejs.chess.piece

import krilovs.andrejs.chess.game.Color
import krilovs.andrejs.chess.game.Coordinates

abstract class Piece(
  val color: Color,
  var coordinates: Coordinates
) {
  val type get() = this::class.simpleName ?: "UNKNOWN"
}