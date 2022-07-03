package pl.ms.fire.emblem.business.entities

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import pl.ms.fire.emblem.business.exceptions.PositionOutOfBoundsException
import pl.ms.fire.emblem.business.values.board.Position

class GameBoardTest {

    @Test
    fun `generate field test`() {

        val gameBoard = GameBoard(mutableMapOf(), 10,15)
        gameBoard.generateField()

        Assertions.assertTrue(gameBoard.spots.isNotEmpty())


    }

    @Test
    fun `position validation test`() {

        val gameBoard = GameBoard(mutableMapOf(), 10,10)
        Assertions.assertThrows(PositionOutOfBoundsException::class.java) {
            gameBoard.getSpot(Position(-1,-1))
        }

        Assertions.assertThrows(PositionOutOfBoundsException::class.java) {
            gameBoard.getSpot(Position(11,0))
        }

        Assertions.assertThrows(PositionOutOfBoundsException::class.java) {
            gameBoard.getSpot(Position(2,-1))
        }
    }
}