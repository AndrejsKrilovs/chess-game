package krilovs.andrejs.chess.piece

import krilovs.andrejs.chess.game.Board
import krilovs.andrejs.chess.game.Color
import krilovs.andrejs.chess.game.Move

class Knight(color: Color, square: Int) : Piece(color, square) {
  private val offsets = intArrayOf(17, 15, 10, 6, -6, -10, -15, -17)

  override fun generateMoves(board: Board, moves: MutableList<Move>) {
    for (offset in offsets) {
      val to = square + offset
      if (!board.isInside(to)) continue

      val df = kotlin.math.abs(board.file(square) - board.file(to))
      val dr = kotlin.math.abs(board.rank(square) - board.rank(to))

      if (df + dr != 3) continue
      addMove(board, moves, to)
    }
  }
}