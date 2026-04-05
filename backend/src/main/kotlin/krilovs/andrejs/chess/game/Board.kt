package krilovs.andrejs.chess.game

import krilovs.andrejs.chess.piece.Bishop
import krilovs.andrejs.chess.piece.King
import krilovs.andrejs.chess.piece.Knight
import krilovs.andrejs.chess.piece.Pawn
import krilovs.andrejs.chess.piece.Piece
import krilovs.andrejs.chess.piece.Queen
import krilovs.andrejs.chess.piece.Rook

class Board {
  var currentTurn = Color.WHITE
  private val board = arrayOfNulls<Piece>(64)

  fun getPiece(square: Int): Piece? = board[square]
  fun getPieces(): List<Piece> = board.filterNotNull()

  fun loadFromFEN(fen: String) {
    board.fill(null)
    val (boardPart, turnPart) = fen.split(" ")
    var rank = 7
    var file = 0

    for (char in boardPart) {
      when {
        char == '/' -> {
          rank--
          file = 0
        }
        char.isDigit() -> file += char.digitToInt()
        else -> {
          val color = if (char.isUpperCase()) Color.WHITE else Color.BLACK
          val square = rank * 8 + file

          board[square] = createPiece(char.lowercaseChar(), color, square)
          file++
        }
      }
    }

    currentTurn = if (turnPart == "w") Color.WHITE else Color.BLACK
  }

  fun toFEN(): String {
    return buildString {
      for (rank in 7 downTo 0) {
        var empty = 0

        for (file in 0..7) {
          val piece = board[rank * 8 + file]
          if (piece == null) {
            empty++
          }
          else {
            if (empty > 0) {
              append(empty)
              empty = 0
            }
            append(pieceToChar(piece))
          }
        }

        if (empty > 0) append(empty)
        if (rank > 0) append("/")
      }

      append(" ")
      append(if (currentTurn == Color.WHITE) "w" else "b")
      append(" - - 0 1")
    }
  }

  fun makeMove(move: Move) {
    board[move.to] = move.piece
    board[move.from] = null
    move.piece.square = move.to
    currentTurn = currentTurn.opposite()
  }

  fun unmakeMove(move: Move) {
    currentTurn = currentTurn.opposite()
    board[move.from] = move.piece
    board[move.to] = move.captured
    move.piece.square = move.from
    move.captured?.square = move.to
  }

  fun generateMoves(): List<Move> {
    val moves = ArrayList<Move>(64)

    for (piece in board) {
      if (piece?.color != currentTurn) continue
      piece.generateMoves(this, moves)
    }

    return moves
  }

  fun generateMovesForSquare(square: Int): List<Move> {
    val piece = board[square] ?: return emptyList()
    if (piece.color != currentTurn) return emptyList()

    val moves = ArrayList<Move>(16)
    piece.generateMoves(this, moves)
    return moves
  }

  fun file(sq: Int) = sq % 8
  fun rank(sq: Int) = sq / 8
  fun isInside(sq: Int) = sq in 0..63

  private fun createPiece(type: Char, color: Color, square: Int): Piece =
    when (type) {
      'p' -> Pawn(color, square)
      'r' -> Rook(color, square)
      'n' -> Knight(color, square)
      'b' -> Bishop(color, square)
      'q' -> Queen(color, square)
      'k' -> King(color, square)
      else -> error("Unknown piece: $type")
    }

  private fun pieceToChar(piece: Piece): Char {
    val c = when (piece) {
      is Pawn -> 'p'
      is Rook -> 'r'
      is Knight -> 'n'
      is Bishop -> 'b'
      is Queen -> 'q'
      is King -> 'k'
      else -> error("Unknown piece")
    }
    return if (piece.color == Color.WHITE) c.uppercaseChar() else c
  }
}