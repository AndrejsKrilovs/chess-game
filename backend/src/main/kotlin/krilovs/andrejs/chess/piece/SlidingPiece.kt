package krilovs.andrejs.chess.piece

import krilovs.andrejs.chess.game.*

abstract class SlidingPiece(
  color: Color,
  coordinates: Coordinates,
  private val slidingType: SlidingType
): Piece(color, coordinates) {

  override fun isSquareAvailable(coord: Coordinates, board: Board): Boolean {
    val dx = (coord.file.code - coordinates.file.code).coerceIn(-1, 1)
    val dy = (coord.rank - coordinates.rank).coerceIn(-1, 1)
    val direction = CoordinatesShift(dx, dy)
    val isStraight = coordinates.file == coord.file || coordinates.rank == coord.rank
    val isDiagonal =
      kotlin.math.abs(coord.file.code - coordinates.file.code) == kotlin.math.abs(coord.rank - coordinates.rank)

    val validDirection = when (slidingType) {
      SlidingType.DIAGONAL -> isDiagonal
      SlidingType.STRAIGHT -> isStraight
      SlidingType.BOTH -> isStraight || isDiagonal
    }

    if (!validDirection) return false

    return getCoordinatesInRange(coordinates, coord, direction)
      .none { board.getPiece(it) != null } && super.isSquareAvailable(coord, board)
  }

  private fun getCoordinatesInRange(
    source: Coordinates,
    destination: Coordinates,
    direction: CoordinatesShift
  ): List<Coordinates> {
    return generateSequence(source) { it.shift(direction) }
      .drop(1)
      .takeWhile { it != destination }
      .toList()
  }
}