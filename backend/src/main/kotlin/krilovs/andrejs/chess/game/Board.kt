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
  fun getPiece(coord: Coordinates): Piece? = pieces[coord]
  fun setupDefaultPiecePositions() {
    for (file in 'a'..'h') {
      placePawns(Pawn(Color.WHITE, Coordinates(file, 2)))
      placePawns(Pawn(Color.BLACK, Coordinates(file, 7)))
    }

    placeLine(Rook::class, Color.WHITE, 1, 'a', 'h')
    placeLine(Rook::class, Color.BLACK, 8, 'a', 'h')

    placeLine(Knight::class, Color.WHITE, 1, 'b', 'g')
    placeLine(Knight::class, Color.BLACK, 8, 'b', 'g')

    placeLine(Bishop::class, Color.WHITE, 1, 'c', 'f')
    placeLine(Bishop::class, Color.BLACK, 8, 'c', 'f')

    placePawns(Queen(Color.WHITE, Coordinates('d', 1)))
    placePawns(Queen(Color.BLACK, Coordinates('d', 8)))

    placePawns(King(Color.WHITE, Coordinates('e', 1)))
    placePawns(King(Color.BLACK, Coordinates('e', 8)))
  }

  private fun placePawns(piece: Piece) {
    pieces[piece.coordinates] = piece
  }

  private fun placeLine(
    clazz: kotlin.reflect.KClass<out Piece>,
    color: Color,
    rank: Int,
    vararg files: Char
  ) {
    for (file in files) {
      val piece = when (clazz) {
        Rook::class -> Rook(color, Coordinates(file, rank))
        Knight::class -> Knight(color, Coordinates(file, rank))
        Bishop::class -> Bishop(color, Coordinates(file, rank))
        else -> error("Unsupported piece")
      }
      placePawns(piece)
    }
  }
}