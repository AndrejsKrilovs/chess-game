package krilovs.andrejs.chess.piece

import krilovs.andrejs.chess.game.Board
import krilovs.andrejs.chess.game.Color
import krilovs.andrejs.chess.game.Move

abstract class Piece(val color: Color, var square: Int) {
  val type: String get() = this::class.simpleName ?: "UNKNOWN"
  abstract fun generateMoves(board: Board, moves: MutableList<Move>)

  protected fun addMove(board: Board, moves: MutableList<Move>, to: Int) {
    val target = board.getPiece(to)
    if (target == null || target.color != color) {
      moves.add(Move(square, to, this, target))
    }
  }
}