package krilovs.andrejs.chess.piece

import krilovs.andrejs.chess.game.Color
import krilovs.andrejs.chess.game.Coordinates
import krilovs.andrejs.chess.game.CoordinatesShift

class Rook(color: Color, coordinates: Coordinates) : Piece(color, coordinates) {
  override fun getPieceMoves(): Set<CoordinatesShift> {
    TODO("Not yet implemented")
  }
}