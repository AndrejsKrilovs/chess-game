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
  private val board = arrayOfNulls<Piece>(64)

  fun getPiece(cord: Coordinates): Piece? = board[index(cord)]

  fun getPieces(): Collection<Piece> {
    val result = ArrayList<Piece>()
    for (piece in board) {
      if (piece != null) result.add(piece)
    }
    return result
  }

  fun loadFromFEN(fen: String) {
    board.fill(null)

    val parts = fen.split(" ")
    val boardPart = parts[0]
    val turnPart = parts[1]

    var rank = 8
    var file = 'a'

    for (char in boardPart) {
      when {
        char == '/' -> {
          rank--
          file = 'a'
        }
        char.isDigit() -> {
          file = (file.code + char.digitToInt()).toChar()
        }
        else -> {
          val color = if (char.isUpperCase()) Color.WHITE else Color.BLACK
          val cord = Coordinates(file, rank)
          val piece = createPiece(char.lowercaseChar(), color, cord)

          board[index(cord)] = piece
          file++
        }
      }
    }

    currentTurn = if (turnPart == "w") Color.WHITE else Color.BLACK
  }

  fun toFEN(): String {
    val sb = StringBuilder()

    for (rank in 8 downTo 1) {
      var empty = 0

      for (file in 'a'..'h') {
        val piece = board[index(Coordinates(file, rank))]

        if (piece == null) {
          empty++
        } else {
          if (empty > 0) {
            sb.append(empty)
            empty = 0
          }

          sb.append(pieceToChar(piece))
        }
      }

      if (empty > 0) sb.append(empty)
      if (rank > 1) sb.append("/")
    }

    sb.append(" ")
    sb.append(if (currentTurn == Color.WHITE) "w" else "b")
    sb.append(" -")
    sb.append(" -")
    sb.append(" 0 1")
    return sb.toString()
  }

  fun tryMove(from: Coordinates, to: Coordinates): Set<Coordinates>? {
    val piece = getPiece(from) ?: return null
    if (piece.color != currentTurn) return emptySet()

    val validMoves = getSafeMoves(from)
    if (to !in validMoves) return validMoves

    val fromIdx = index(from)
    val toIdx = index(to)
    val move = Move(
      from = fromIdx,
      to = toIdx,
      piece = board[fromIdx] ?: return emptySet(),
      captured = board[toIdx]
    )

    makeMove(move)
    return emptySet()
  }

  fun isSquareUnderAttack(coord: Coordinates, byColor: Color): Boolean {
    for (piece in board) {
      if (piece == null || piece.color != byColor) continue
      if (coord in piece.getAttackedSquares(this)) return true
    }
    return false
  }

  fun getSafeMoves(coord: Coordinates): Set<Coordinates> =
    getPiece(coord)
      ?.getAvailableMoveSquares(this)
      ?.filter { isMoveSafe(coord, it) }
      ?.toSet()
      ?: emptySet()

  fun getGameState(color: Color): GameState {
    val inCheck = isCheck(color)
    val hasMoves = hasAnyValidMove(color)

    return when {
      inCheck && !hasMoves -> GameState.CHECKMATE
      !inCheck && !hasMoves -> GameState.STALEMATE
      inCheck -> GameState.CHECK
      else -> GameState.NORMAL
    }
  }

  fun makeMove(move: Move) {
    board[move.to] = move.piece
    board[move.from] = null
    move.piece.coordinates = toCoordinates(move.to)
    currentTurn = currentTurn.opposite()
  }

  fun unmakeMove(move: Move) {
    currentTurn = currentTurn.opposite()
    board[move.from] = move.piece
    board[move.to] = move.captured
    move.piece.coordinates = toCoordinates(move.from)
    move.captured?.coordinates = toCoordinates(move.to)
  }

  private fun isMoveSafe(from: Coordinates, to: Coordinates): Boolean {
    val move = Move(
      from = index(from),
      to = index(to),
      piece = board[index(from)] ?: return false,
      captured = board[index(to)]
    )

    makeMove(move)

    val safe = !isSquareUnderAttack(
      findKing(move.piece.color),
      move.piece.color.opposite()
    )

    unmakeMove(move)
    return safe
  }

  private fun findKing(color: Color): Coordinates {
    for (piece in board) {
      if (piece is King && piece.color == color) {
        return piece.coordinates
      }
    }
    error("King not found")
  }

  private fun isCheck(color: Color): Boolean =
    isSquareUnderAttack(findKing(color), color.opposite())

  private fun hasAnyValidMove(color: Color): Boolean {
    for (i in board.indices) {
      val piece = board[i] ?: continue
      if (piece.color != color) continue

      val from = toCoordinates(i)
      for (move in piece.getAvailableMoveSquares(this)) {
        if (isMoveSafe(from, move)) return true
      }
    }
    return false
  }

  private fun createPiece(
    type: Char,
    color: Color,
    cord: Coordinates
  ): Piece {
    return when (type) {
      'p' -> Pawn(color, cord)
      'r' -> Rook(color, cord)
      'n' -> Knight(color, cord)
      'b' -> Bishop(color, cord)
      'q' -> Queen(color, cord)
      'k' -> King(color, cord)
      else -> throw IllegalArgumentException("Unknown piece: $type")
    }
  }

  private fun pieceToChar(piece: Piece): Char {
    val char = when (piece) {
      is Pawn -> 'p'
      is Rook -> 'r'
      is Knight -> 'n'
      is Bishop -> 'b'
      is Queen -> 'q'
      is King -> 'k'
      else -> throw IllegalArgumentException("Unknown piece")
    }

    return if (piece.color == Color.WHITE) char.uppercaseChar() else char
  }

  private fun index(coord: Coordinates): Int {
    val file = coord.file - 'a'
    val rank = coord.rank - 1
    return rank * 8 + file
  }

  private fun toCoordinates(index: Int): Coordinates {
    val file = 'a' + (index % 8)
    val rank = (index / 8) + 1
    return Coordinates(file, rank)
  }
}