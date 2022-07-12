package pl.ms.fire.emblem.business.values.board

import kotlin.math.abs

data class Position(
    val x: Int,
    val y: Int
) {
    companion object{
        fun checkAbsolutePosition(position1: Position, position2: Position): Int =
            abs(position1.x - position2.x) + abs(position1.y - position2.y)
    }
}