package krilovs.andrejs.chess.game

class BoardUtils {
  fun getDiagonalCoordinatesInRange(source: Coordinates, destination: Coordinates): List<Coordinates> {
    val fileStep = (destination.file.code - source.file.code).coerceIn(-1, 1)
    val rankStep = (destination.rank - source.rank).coerceIn(-1, 1)

    return generateSequence(source) { it.shift(CoordinatesShift(fileStep, rankStep)) }
      .drop(1)
      .takeWhile { it != destination }
      .toList()
  }

  fun getVerticalCoordinatesInRange(source: Coordinates, destination: Coordinates): List<Coordinates> {
    val step = (destination.rank - source.rank).coerceIn(-1, 1)

    return generateSequence(source) { it.shift(CoordinatesShift(0, step)) }
      .drop(1)
      .takeWhile { it != destination }
      .toList()
  }

  fun getHorizontalCoordinatesInRange(source: Coordinates, destination: Coordinates): List<Coordinates> {
    val step = (destination.file.code - source.file.code).coerceIn(-1, 1)

    return generateSequence(source) { it.shift(CoordinatesShift(step, 0)) }
      .drop(1)
      .takeWhile { it != destination }
      .toList()
  }
}