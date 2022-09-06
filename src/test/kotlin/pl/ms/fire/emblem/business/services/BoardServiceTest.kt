package pl.ms.fire.emblem.business.services

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import pl.ms.fire.emblem.business.entities.CharacterPair
import pl.ms.fire.emblem.business.entities.GameBoard
import pl.ms.fire.emblem.business.exceptions.battle.NotAllowedWeaponCategoryException
import pl.ms.fire.emblem.business.exceptions.battle.OutOfRangeException
import pl.ms.fire.emblem.business.exceptions.board.NotEnoughMovementException
import pl.ms.fire.emblem.business.exceptions.board.PairOnRouteException
import pl.ms.fire.emblem.business.exceptions.board.RouteNotConstantException
import pl.ms.fire.emblem.business.exceptions.item.NotStaffException
import pl.ms.fire.emblem.business.serices.BoardService
import pl.ms.fire.emblem.business.utlis.getStat
import pl.ms.fire.emblem.business.values.GameCharacter
import pl.ms.fire.emblem.business.values.board.Position
import pl.ms.fire.emblem.business.values.board.Spot
import pl.ms.fire.emblem.business.values.board.Terrain
import pl.ms.fire.emblem.business.values.category.AttackCategory
import pl.ms.fire.emblem.business.values.category.WeaponCategory
import pl.ms.fire.emblem.business.values.character.CharacterClass
import pl.ms.fire.emblem.business.values.character.Stat
import pl.ms.fire.emblem.business.values.items.Item

class BoardServiceTest {

    private val boardService = BoardService()

    @Test
    fun `wait turn test`(){

        val gameCharacter = GameCharacter(
            "Test", 30, 0,
            mutableListOf(
                Item("Sword", 5, 100, 10, 1, AttackCategory.PHYSICAL, WeaponCategory.SWORD, 1),
                Item("Physic", 25, 100, 110, 1, AttackCategory.PHYSICAL, WeaponCategory.SWORD, 1),
                Item("Magical", 5, 100, 5, 2, AttackCategory.MAGICAL, WeaponCategory.TOME, 1),
                Item("Magical", 5, 100, 5, 2, AttackCategory.MAGICAL, WeaponCategory.STAFF, 1),
                Item("Lance", 5, 100, 5, 2, AttackCategory.PHYSICAL, WeaponCategory.LANCE, 1),
            ),
            mapOf(
                Stat.HEALTH to 30,
                Stat.STRENGTH to 10,
                Stat.MAGICK to 15,
                Stat.DEFENSE to 5,
                Stat.RESISTANCE to 5,
                Stat.SKILL to 10,
                Stat.LUCK to 18,
                Stat.SPEED to 12
            ),
            CharacterClass.DARK_KNIGHT,
            false
        )

        boardService.waitTurn(gameCharacter)

        Assertions.assertTrue(gameCharacter.moved)

    }

    @Test
    fun `staff heal test`() {

        val staffUser = GameCharacter(
            "Test", 30, 0,
            mutableListOf(
                Item("Staff", 30, 100, 10, 1, AttackCategory.MAGICAL, WeaponCategory.STAFF, 1),
                Item("Staff", 10, 100, 10, 1, AttackCategory.MAGICAL, WeaponCategory.STAFF, 1),
                Item("Physic", 25, 100, 110, 1, AttackCategory.PHYSICAL, WeaponCategory.SWORD, 1),
                Item("Magical", 5, 100, 5, 2, AttackCategory.MAGICAL, WeaponCategory.TOME, 1),
                Item("Magical", 5, 100, 5, 2, AttackCategory.MAGICAL, WeaponCategory.STAFF, 1),
            ),
            mapOf(
                Stat.HEALTH to 30,
                Stat.STRENGTH to 10,
                Stat.MAGICK to 15,
                Stat.DEFENSE to 5,
                Stat.RESISTANCE to 5,
                Stat.SKILL to 10,
                Stat.LUCK to 18,
                Stat.SPEED to 12
            ),
            CharacterClass.VALKYRIE,
            false
        )

        val staffSpot =  Spot(Position(1,1), Terrain.FORREST, CharacterPair(staffUser, null))
        val invalidStaffSpot =  Spot(Position(4,1), Terrain.FORREST, CharacterPair(staffUser, null))

        val healedCharacter = GameCharacter(
            "Test", 30, 0,
            mutableListOf(
                Item("Sword", 5, 100, 10, 1, AttackCategory.PHYSICAL, WeaponCategory.SWORD, 1),
                Item("Staff", 25, 100, 110, 1, AttackCategory.PHYSICAL, WeaponCategory.STAFF, 1),
                Item("Magical", 5, 100, 5, 2, AttackCategory.MAGICAL, WeaponCategory.TOME, 1),
                Item("Magical", 5, 100, 5, 2, AttackCategory.MAGICAL, WeaponCategory.STAFF, 1),
                Item("Lance", 5, 100, 5, 2, AttackCategory.PHYSICAL, WeaponCategory.LANCE, 1),
            ),
            mapOf(
                Stat.HEALTH to 30,
                Stat.STRENGTH to 10,
                Stat.MAGICK to 15,
                Stat.DEFENSE to 5,
                Stat.RESISTANCE to 5,
                Stat.SKILL to 10,
                Stat.LUCK to 18,
                Stat.SPEED to 12
            ),
            CharacterClass.DARK_KNIGHT,
            false
        )

        val healedSpot = Spot(Position(1,2), Terrain.FORREST, CharacterPair(healedCharacter, null))

        Assertions.assertThrows(NotStaffException::class.java) {
            boardService.staffHeal(healedSpot, staffSpot)
        }

        healedCharacter.currentEquippedItem = 1
        Assertions.assertThrows(NotAllowedWeaponCategoryException::class.java) {
            boardService.staffHeal(healedSpot, staffSpot)
        }

        Assertions.assertThrows(OutOfRangeException::class.java) {
            boardService.staffHeal(invalidStaffSpot, healedSpot)
        }

        Assertions.assertDoesNotThrow {
            boardService.staffHeal(staffSpot, healedSpot)
        }

        Assertions.assertEquals(healedCharacter.combinedStat.getStat(Stat.HEALTH), healedCharacter.remainingHealth)

        Assertions.assertTrue(staffUser.moved)

        staffUser.moved = false
        staffUser.currentEquippedItem = 1
        healedCharacter.remainingHealth = 30

        Assertions.assertDoesNotThrow {
            boardService.staffHeal(staffSpot, healedSpot)
        }

        Assertions.assertEquals(40, healedCharacter.remainingHealth)

    }

    @Test
    fun `move pair test`() {
        val gameCharacter = GameCharacter(
            "Test", 30, 0,
            mutableListOf(
                Item("Lance", 30, 100, 10, 1, AttackCategory.MAGICAL, WeaponCategory.LANCE, 1),
                Item("Staff", 10, 100, 10, 1, AttackCategory.MAGICAL, WeaponCategory.STAFF, 1),
                Item("Physic", 25, 100, 110, 1, AttackCategory.PHYSICAL, WeaponCategory.SWORD, 1),
                Item("Magical", 5, 100, 5, 2, AttackCategory.MAGICAL, WeaponCategory.TOME, 1),
                Item("Magical", 5, 100, 5, 2, AttackCategory.MAGICAL, WeaponCategory.STAFF, 1),
            ),
            mapOf(
                Stat.HEALTH to 30,
                Stat.STRENGTH to 10,
                Stat.MAGICK to 15,
                Stat.DEFENSE to 5,
                Stat.RESISTANCE to 5,
                Stat.SKILL to 10,
                Stat.LUCK to 18,
                Stat.SPEED to 12
            ),
            CharacterClass.GENERAL,
            false
        )

        val startSpot =  Spot(Position(1,1), Terrain.FORREST, CharacterPair(gameCharacter, null))

        val route = linkedSetOf(
            Position(1,2),
            Position(2,2),
            Position(2,3),
            Position(3,3),
            Position(3,4),
        )

        val forrestRoute = linkedSetOf(
            Position(1,2),
            Position(2,2),
            Position(2,3),
        )

        val sandRoute = linkedSetOf(
            Position(1,2),
            Position(2,2),
            Position(2,3),
        )

        val tooFarForrestRoute = linkedSetOf(
            Position(1,2),
            Position(2,2),
            Position(2,3),
            Position(3,3),
        )

        val tooFarSandRoute = linkedSetOf(
            Position(1,2),
            Position(2,2),
            Position(2,3),
            Position(3,3),
        )

        val tooFarRoute = linkedSetOf(
            Position(1,2),
            Position(2,2),
            Position(2,3),
            Position(3,3),
            Position(3,4),
            Position(4,4),
        )

        val notConstantRoute = linkedSetOf(
            Position(1,2),
            Position(2,2),
            Position(2,3),
            Position(3,3),
            Position(4,4),
        )

        val gameBoard = GameBoard(mutableMapOf(
            Position(1,1) to startSpot,
            Position(1,2) to Spot(Position(1,2), Terrain.PLAIN, null),
            Position(2,2) to Spot(Position(2,2), Terrain.PLAIN, null),
            Position(2,3) to Spot(Position(2,3), Terrain.PLAIN, null),
            Position(3,3) to Spot(Position(3,3), Terrain.PLAIN, null),
            Position(3,4) to Spot(Position(3,4), Terrain.PLAIN, null),
            Position(4,4) to Spot(Position(4,4), Terrain.PLAIN, null),
        ),10,10)

        val characterOnSpotBoard = GameBoard(mutableMapOf(
            Position(1,1) to startSpot,
            Position(1,2) to Spot(Position(1,2), Terrain.PLAIN, null),
            Position(2,2) to Spot(Position(2,2), Terrain.PLAIN, null),
            Position(2,3) to Spot(Position(2,3), Terrain.PLAIN, CharacterPair(gameCharacter, null)),
            Position(3,3) to Spot(Position(3,3), Terrain.PLAIN, null),
            Position(3,4) to Spot(Position(3,4), Terrain.PLAIN, null),
            Position(4,4) to Spot(Position(4,4), Terrain.PLAIN, null),
        ),10,10)

        val forrestBoard = GameBoard(mutableMapOf(
            Position(1,1) to startSpot,
            Position(1,2) to Spot(Position(1,2), Terrain.FORREST, null),
            Position(2,2) to Spot(Position(2,2), Terrain.FORREST, null),
            Position(2,3) to Spot(Position(2,3), Terrain.PLAIN, null),
            Position(3,3) to Spot(Position(3,3), Terrain.PLAIN, null),
            Position(3,4) to Spot(Position(3,4), Terrain.PLAIN, null),
            Position(4,4) to Spot(Position(4,4), Terrain.PLAIN, null),
        ),10,10)

        val sandBoard = GameBoard(mutableMapOf(
            Position(1,1) to startSpot,
            Position(1,2) to Spot(Position(1,2), Terrain.SAND, null),
            Position(2,2) to Spot(Position(2,2), Terrain.PLAIN, null),
            Position(2,3) to Spot(Position(2,3), Terrain.PLAIN, null),
            Position(3,3) to Spot(Position(3,3), Terrain.PLAIN, null),
            Position(3,4) to Spot(Position(3,4), Terrain.PLAIN, null),
            Position(4,4) to Spot(Position(4,4), Terrain.PLAIN, null),
        ),10,10)

        Assertions.assertThrows(NotEnoughMovementException::class.java) {
            boardService.movePair(startSpot, tooFarRoute, gameBoard)
        }
        Assertions.assertThrows(NotEnoughMovementException::class.java) {
            boardService.movePair(startSpot, tooFarForrestRoute, forrestBoard)
        }
        Assertions.assertThrows(NotEnoughMovementException::class.java) {
            boardService.movePair(startSpot, tooFarSandRoute, sandBoard)
        }

        Assertions.assertThrows(PairOnRouteException::class.java) {
            boardService.movePair(startSpot, route, characterOnSpotBoard)
        }

        Assertions.assertThrows(RouteNotConstantException::class.java) {
            boardService.movePair(startSpot, notConstantRoute, gameBoard)
        }

        Assertions.assertDoesNotThrow {
            boardService.movePair(startSpot, route, gameBoard)
        }
        Assertions.assertNull(startSpot.standingCharacter)
        Assertions.assertEquals(gameCharacter, gameBoard.getSpot(route.last()).standingCharacter!!.leadCharacter)

        startSpot.standingCharacter = CharacterPair(gameCharacter, null)
        gameBoard.getSpot(route.last()).standingCharacter = null
        Assertions.assertDoesNotThrow {
            boardService.movePair(startSpot, sandRoute, sandBoard)
        }
        Assertions.assertNull(startSpot.standingCharacter)
        Assertions.assertEquals(gameCharacter, sandBoard.getSpot(sandRoute.last()).standingCharacter!!.leadCharacter)

        startSpot.standingCharacter = CharacterPair(gameCharacter, null)
        gameBoard.getSpot(sandRoute.last()).standingCharacter = null
        Assertions.assertDoesNotThrow {
            boardService.movePair(startSpot, forrestRoute, forrestBoard)
        }
        Assertions.assertNull(startSpot.standingCharacter)
        Assertions.assertEquals(gameCharacter, forrestBoard.getSpot(forrestRoute.last()).standingCharacter!!.leadCharacter)
    }
}