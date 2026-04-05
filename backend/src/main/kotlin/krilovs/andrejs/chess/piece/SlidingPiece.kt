package krilovs.andrejs.chess.piece

import krilovs.andrejs.chess.game.Board
import krilovs.andrejs.chess.game.Color
import krilovs.andrejs.chess.game.Move

abstract class SlidingPiece(
  color: Color,
  square: Int,
  private val directions: IntArray
) : Piece(color, square) {

  override fun generateMoves(board: Board, moves: MutableList<Move>) {
    for (dir in directions) {
      var from = square

      while (true) {
        val to = from + dir
        if (!board.isInside(to)) break
        if (kotlin.math.abs(board.file(to) - board.file(from)) > 1) break

        val target = board.getPiece(to)
        if (target == null) {
          moves.add(Move(square, to, this, null))
        }
        else {
          if (target.color != color) {
            moves.add(Move(square, to, this, target))
          }
          break
        }

        from = to
      }
    }
  }
}