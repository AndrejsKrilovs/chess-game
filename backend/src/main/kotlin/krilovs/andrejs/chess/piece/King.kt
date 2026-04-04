package krilovs.andrejs.chess.piece

import krilovs.andrejs.chess.game.Board
import krilovs.andrejs.chess.game.Color
import krilovs.andrejs.chess.game.Coordinates
import krilovs.andrejs.chess.game.CoordinatesShift

class King(color: Color, coordinates: Coordinates) : Piece(color, coordinates) {
  override fun getPieceMoves(): Set<CoordinatesShift> = buildSet {
    (-1..1).filter { it != 0 }.forEach {
      add(CoordinatesShift(it, it))
      add(CoordinatesShift(it, -it))
      add(CoordinatesShift(it, 0))
      add(CoordinatesShift(0, it))
    }
  }

  override fun isSquareAvailable(coord: Coordinates, board: Board): Boolean {
    return super.isSquareAvailable(coord, board) && !board.isSquareUnderAttack(coord,color.opposite())
  }

  override fun getAttackedSquares(board: Board): Set<Coordinates> =
    getPieceMoves()
      .mapNotNull { coordinates.shift(it) }
      .toSet()
}