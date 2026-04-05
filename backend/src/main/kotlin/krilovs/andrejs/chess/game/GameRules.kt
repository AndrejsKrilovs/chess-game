package krilovs.andrejs.chess.game

import krilovs.andrejs.chess.piece.King

class GameRules(private val board: Board, private val attackService: AttackService) {

  fun isMoveSafe(move: Move): Boolean {
    board.makeMove(move)
    val kingSq = findKing(move.piece.color)
    val safe = !attackService.isSquareUnderAttack(kingSq, move.piece.color.opposite())
    board.unmakeMove(move)
    return safe
  }

  fun getGameState(color: Color): GameState {
    val kingSq = findKing(color)
    val inCheck = attackService.isSquareUnderAttack(kingSq, color.opposite())
    val hasMoves = board.generateMoves().any { isMoveSafe(it) }

    return when {
      inCheck && !hasMoves -> GameState.CHECKMATE
      !inCheck && !hasMoves -> GameState.STALEMATE
      inCheck -> GameState.CHECK
      else -> GameState.NORMAL
    }
  }

  private fun findKing(color: Color): Int {
    return board.getPieces()
      .first { it is King && it.color == color }
      .square
  }
}