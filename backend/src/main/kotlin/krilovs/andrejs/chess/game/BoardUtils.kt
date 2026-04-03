package krilovs.andrejs.chess.game

class BoardUtils {
  fun getDiagonalCoordinatesInRange(source: Coordinates, destination: Coordinates): List<Coordinates> {
    val fileShift = if (source.file.code < destination.file.code) 1 else -1
    val rankShift = if (source.rank < destination.rank) 1 else -1

    var current = source
    return buildList {
      while (true) {
        val next = current.shift(CoordinatesShift(fileShift, rankShift)) ?: break
        if (next == destination) break
        add(next)
        current = next
      }
    }
  }
}