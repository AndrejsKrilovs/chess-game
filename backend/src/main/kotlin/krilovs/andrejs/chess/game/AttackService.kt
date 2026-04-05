package krilovs.andrejs.chess.game

import krilovs.andrejs.chess.piece.Pawn

class AttackService(private val board: Board) {

  fun isSquareUnderAttack(square: Int, byColor: Color): Boolean {
    val temp = ArrayList<Move>(32)

    for (piece in board.getPieces()) {
      if (piece.color != byColor) continue
      temp.clear()

      when (piece) {
        is Pawn -> generatePawnAttacks(piece, temp)
        else -> piece.generateMoves(board, temp)
      }
      for (m in temp) {
        if (m.to == square) return true
      }
    }

    return false
  }

  private fun generatePawnAttacks(pawn: Pawn, moves: MutableList<Move>) {
    val dir = if (pawn.color == Color.WHITE) 8 else -8
    val attacks = intArrayOf(dir + 1, dir - 1)

    for (offset in attacks) {
      val to = pawn.square + offset
      if (!board.isInside(to)) continue
      if (kotlin.math.abs(board.file(pawn.square) - board.file(to)) != 1) continue
      moves.add(Move(pawn.square, to, pawn, null))
    }
  }
}