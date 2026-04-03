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

  fun shift(shift: CoordinatesShift): Coordinates? {
    if (!canShift(shift)) return null

    val file = this.file + shift.fileShift
    val rank = this.rank + shift.rankShift
    return Coordinates(file, rank)
  }

  fun canShift(shift: CoordinatesShift): Boolean {
    val file = this.file + shift.fileShift
    val rank = this.rank + shift.rankShift
    return file in 'a'..'h' && rank in 1..8
  }
}