package krilovs.andrejs.chess.piece

import krilovs.andrejs.chess.game.Board

class Pawn(color: Color, square: Int) : Piece(color, square) {
  override fun generateAvailableMoves(board: Board): Set<Int> {
    return emptySet()
  }
}