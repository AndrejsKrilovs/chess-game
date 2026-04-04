package krilovs.andrejs.chess.game

enum class Color {
  BLACK,
  WHITE;

  fun opposite(): Color {
    return if (this == WHITE) BLACK else WHITE
  }
}