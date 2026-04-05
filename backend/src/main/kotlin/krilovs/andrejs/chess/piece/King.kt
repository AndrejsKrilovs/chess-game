package krilovs.andrejs.chess.piece

import krilovs.andrejs.chess.game.Board
import krilovs.andrejs.chess.game.Color
import krilovs.andrejs.chess.game.Move

class King(color: Color, square: Int) : Piece(color, square) {
  private val offsets = intArrayOf(8, -8, 1, -1, 9, -9, 7, -7)

  override fun generateMoves(board: Board, moves: MutableList<Move>) {
    for (offset in offsets) {
      val to = square + offset
      if (!board.isInside(to)) continue

      if (kotlin.math.abs(board.file(square) - board.file(to)) > 1) continue
      addMove(board, moves, to)
    }
  }
}