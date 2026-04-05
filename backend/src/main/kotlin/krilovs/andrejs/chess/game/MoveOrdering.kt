package krilovs.andrejs.chess.game

import krilovs.andrejs.chess.piece.Bishop
import krilovs.andrejs.chess.piece.King
import krilovs.andrejs.chess.piece.Knight
import krilovs.andrejs.chess.piece.Pawn
import krilovs.andrejs.chess.piece.Piece
import krilovs.andrejs.chess.piece.Queen
import krilovs.andrejs.chess.piece.Rook

object MoveOrdering {

  fun order(moves: MutableList<Move>) {
    moves.sortByDescending { score(it) }
  }

  private fun score(m: Move): Int {
    return if (m.captured != null) 1000 + value(m.captured) else 0
  }

  private fun value(p: Piece): Int = when (p) {
    is Pawn -> 100
    is Knight, is Bishop -> 300
    is Rook -> 500
    is Queen -> 900
    is King -> 10000
    else -> 0
  }
}