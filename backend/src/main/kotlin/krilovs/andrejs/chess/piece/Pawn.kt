package krilovs.andrejs.chess.piece

import krilovs.andrejs.chess.game.Board
import krilovs.andrejs.chess.game.Color
import krilovs.andrejs.chess.game.Move

class Pawn(color: Color, square: Int) : Piece(color, square) {

  override fun generateMoves(board: Board, moves: MutableList<Move>) {
    val dir = if (color == Color.WHITE) 8 else -8
    val startRank = if (color == Color.WHITE) 1 else 6

    val oneStep = square + dir
    if (board.isInside(oneStep) && board.getPiece(oneStep) == null) {
      moves.add(Move(square, oneStep, this, null))

      val rank = board.rank(square)
      val twoStep = square + dir * 2
      if (rank == startRank && board.isInside(twoStep) && board.getPiece(twoStep) == null) {
        moves.add(Move(square, twoStep, this, null))
      }
    }

    val attacks = intArrayOf(dir + 1, dir - 1)

    for (offset in attacks) {
      val to = square + offset
      if (!board.isInside(to)) continue

      if (kotlin.math.abs(board.file(square) - board.file(to)) != 1) continue

      val target = board.getPiece(to)
      if (target != null && target.color != color) {
        moves.add(Move(square, to, this, target))
      }
    }
  }
}