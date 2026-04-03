package krilovs.andrejs.chess.game

import krilovs.andrejs.chess.piece.Bishop
import krilovs.andrejs.chess.piece.King
import krilovs.andrejs.chess.piece.Knight
import krilovs.andrejs.chess.piece.Pawn
import krilovs.andrejs.chess.piece.Piece
import krilovs.andrejs.chess.piece.Queen
import krilovs.andrejs.chess.piece.Rook

class Board {
  val pieces: MutableMap<Coordinates, Piece> = mutableMapOf()
  fun setupDefaultPiecePositions() {
    ('a'..'h').forEach {
      placeLine(::Pawn, Color.WHITE, 2, it)
      placeLine(::Pawn, Color.BLACK, 7, it)
    }

    placeLine(::Rook, Color.WHITE, 1, 'a', 'h')
    placeLine(::Rook, Color.BLACK, 8, 'a', 'h')

    placeLine(::Knight, Color.WHITE, 1, 'b', 'g')
    placeLine(::Knight, Color.BLACK, 8, 'b', 'g')

    placeLine(::Bishop, Color.WHITE, 1, 'c', 'f')
    placeLine(::Bishop, Color.BLACK, 8, 'c', 'f')

    placeLine(::Queen, Color.WHITE, 1, 'd')
    placeLine(::Queen, Color.BLACK, 8, 'd')

    placeLine(::King, Color.WHITE, 1, 'e')
    placeLine(::King, Color.BLACK, 8, 'e')
  }

  private fun placeLine(
    factory: (Color, Coordinates) -> Piece,
    color: Color,
    rank: Int,
    vararg files: Char
  ) {
    files.forEach { file ->
      val piece = factory(color, Coordinates(file, rank))
      pieces[piece.coordinates] = piece
    }
  }
}