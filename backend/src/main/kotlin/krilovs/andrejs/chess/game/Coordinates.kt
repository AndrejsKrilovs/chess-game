package krilovs.andrejs.chess.game

data class Coordinates(
  val file: Char,
  val rank: Int
) {

  init {
    require(this.file.lowercaseChar() in 'a'..'h') {
      "File must be between A and H"
    }
    require(rank in 1..8) {
      "Rank must be between 1 and 8"
    }
  }
}