package krilovs.andrejs.chess.piece

class Queen(color: Color, square: Int) : SlidingPiece(color, square, intArrayOf(8, -8, 1, -1, 9, -9, 7, -7))