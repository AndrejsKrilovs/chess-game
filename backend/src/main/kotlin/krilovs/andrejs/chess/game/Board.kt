package krilovs.andrejs.chess.game

import krilovs.andrejs.chess.piece.Pawn
import krilovs.andrejs.chess.piece.Piece

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
  }
}
