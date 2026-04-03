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

  private fun setPiece(piece: Piece, coordinates: Coordinates) {
    piece.coordinates = coordinates
    pieces[coordinates] = piece
  }

  fun setupDefaultPiecePositions() {
    for (file in 'a'..'h') {
      setPiece(Pawn(Color.WHITE, Coordinates(file, 2)), Coordinates(file, 2))
      setPiece(Pawn(Color.BLACK, Coordinates(file, 7)), Coordinates(file, 7))
    }

    setPiece(Rook(Color.WHITE, Coordinates('a', 1)), Coordinates('a', 1))
    setPiece(Rook(Color.WHITE, Coordinates('h', 1)), Coordinates('h', 1))
    setPiece(Rook(Color.BLACK, Coordinates('a', 8)), Coordinates('a', 8))
    setPiece(Rook(Color.BLACK, Coordinates('h', 8)), Coordinates('h', 8))

    setPiece(Knight(Color.WHITE, Coordinates('b', 1)), Coordinates('b', 1))
    setPiece(Knight(Color.WHITE, Coordinates('g', 1)), Coordinates('g', 1))
    setPiece(Knight(Color.BLACK, Coordinates('b', 8)), Coordinates('b', 8))
    setPiece(Knight(Color.BLACK, Coordinates('g', 8)), Coordinates('g', 8))

    setPiece(Bishop(Color.WHITE, Coordinates('c', 1)), Coordinates('c', 1))
    setPiece(Bishop(Color.WHITE, Coordinates('f', 1)), Coordinates('f', 1))
    setPiece(Bishop(Color.BLACK, Coordinates('c', 8)), Coordinates('c', 8))
    setPiece(Bishop(Color.BLACK, Coordinates('f', 8)), Coordinates('f', 8))

    setPiece(Queen(Color.WHITE, Coordinates('d', 1)), Coordinates('d', 1))
    setPiece(Queen(Color.BLACK, Coordinates('d', 8)), Coordinates('d', 8))

    setPiece(King(Color.WHITE, Coordinates('e', 1)), Coordinates('e', 1))
    setPiece(King(Color.BLACK, Coordinates('e', 8)), Coordinates('e', 8))
  }
}
