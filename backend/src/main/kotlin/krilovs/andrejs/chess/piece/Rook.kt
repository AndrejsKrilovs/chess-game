package krilovs.andrejs.chess.piece

import krilovs.andrejs.chess.game.Color

class Rook(color: Color, square: Int) : SlidingPiece(color, square, intArrayOf(8, -8, 1, -1))