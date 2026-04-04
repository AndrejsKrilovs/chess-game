package krilovs.andrejs.chess.game

import krilovs.andrejs.chess.piece.Bishop
import krilovs.andrejs.chess.piece.King
import krilovs.andrejs.chess.piece.Knight
import krilovs.andrejs.chess.piece.Pawn
import krilovs.andrejs.chess.piece.Piece
import krilovs.andrejs.chess.piece.Queen
import krilovs.andrejs.chess.piece.Rook

class Board {
  var currentTurn: Color = Color.WHITE
  private val pieces: MutableMap<Coordinates, Piece> = mutableMapOf()

  fun getPieces(): Collection<Piece> = pieces.values
  fun getPiece(coord: Coordinates): Piece? = pieces[coord]
  fun setupDefaultPiecePositions() {
    pieces.clear()

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

  fun move(from: Coordinates, to: Coordinates) {
    val piece = pieces.remove(from) ?: return
    pieces.remove(to)
    piece.coordinates = to
    pieces[to] = piece
  }

  fun tryMove(from: Coordinates, to: Coordinates): Set<Coordinates>? {
    val piece = getPiece(from) ?: return null
    if (piece.color != currentTurn) return emptySet()

    val moves = piece.getAvailableMoveSquares(this)
    if (to !in moves) return moves
    move(from, to)

    currentTurn = if (currentTurn == Color.WHITE) Color.BLACK else Color.WHITE
    return emptySet()
  }

  fun isSquareUnderAttack(coord: Coordinates, byColor: Color): Boolean {
    return pieces.values
      .filter { it.color == byColor }
      .any { it.getAttackedSquares(this).contains(coord) }
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