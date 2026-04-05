package krilovs.andrejs.chess.piece

import krilovs.andrejs.chess.game.Color

class Bishop(color: Color, square: Int) : SlidingPiece(color, square, intArrayOf(9, -9, 7, -7))