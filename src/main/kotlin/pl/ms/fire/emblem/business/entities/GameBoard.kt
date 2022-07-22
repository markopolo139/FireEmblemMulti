package pl.ms.fire.emblem.business.entities

import pl.ms.fire.emblem.business.exceptions.PositionOutOfBoundsException
import pl.ms.fire.emblem.business.exceptions.board.SpotDoesNotExistsException
import pl.ms.fire.emblem.business.utlis.RandomSingleton
import pl.ms.fire.emblem.business.values.board.Position
import pl.ms.fire.emblem.business.values.board.Spot
import pl.ms.fire.emblem.business.values.board.Terrain

class GameBoard(
    val spots: MutableMap<Position, Spot>,
    val maxHeight: Int,
    val maxWidth: Int
) {

    fun generateField(){
        spots.clear()
        for(x in 1..maxWidth)
            for (y in 1..maxHeight) {
                val position = Position(x,y)
                spots[position] = Spot(position, getRandomTerrain(), null)
            }

    }

    fun getSpot(position: Position): Spot {
        validatePosition(position)

        return spots[position] ?: throw SpotDoesNotExistsException()
    }

    private fun validatePosition(position: Position) {
        if (position.x !in 1..maxWidth || position.y !in 1..maxHeight)
            throw PositionOutOfBoundsException()
    }

    private fun getRandomTerrain() = when(RandomSingleton.random.nextInt(101)) {
        in 0..29 -> Terrain.GRASS
        in 30..59 -> Terrain.PLAIN
        in 60..74 -> Terrain.FORREST
        in 75..79 -> Terrain.MOUNTAIN
        in 80..94 -> Terrain.SAND
        in 95..100 -> Terrain.FORTRESS
        else -> Terrain.PLAIN
    }
}