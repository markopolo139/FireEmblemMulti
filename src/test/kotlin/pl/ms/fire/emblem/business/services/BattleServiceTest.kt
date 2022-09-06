package pl.ms.fire.emblem.business.services

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import pl.ms.fire.emblem.business.entities.CharacterPair
import pl.ms.fire.emblem.business.exceptions.battle.OutOfRangeException
import pl.ms.fire.emblem.business.exceptions.battle.StaffInBattleException
import pl.ms.fire.emblem.business.exceptions.item.NoItemEquippedException
import pl.ms.fire.emblem.business.serices.BattleService
import pl.ms.fire.emblem.business.serices.battle.DefaultBattleCalculator
import pl.ms.fire.emblem.business.serices.battle.MissBattleCalculator
import pl.ms.fire.emblem.business.utlis.Displayable
import pl.ms.fire.emblem.business.values.GameCharacter
import pl.ms.fire.emblem.business.values.battle.BattleForecast
import pl.ms.fire.emblem.business.values.board.Position
import pl.ms.fire.emblem.business.values.board.Spot
import pl.ms.fire.emblem.business.values.board.Terrain
import pl.ms.fire.emblem.business.values.category.AttackCategory
import pl.ms.fire.emblem.business.values.category.WeaponCategory
import pl.ms.fire.emblem.business.values.character.CharacterClass
import pl.ms.fire.emblem.business.values.character.DefensiveSkill
import pl.ms.fire.emblem.business.values.character.OffensiveSkill
import pl.ms.fire.emblem.business.values.character.Stat
import pl.ms.fire.emblem.business.values.items.Item

class BattleServiceTest {

    private val battleService = BattleService()

    @Test
    fun `test exceptions`() {
        val invalidAttacker = GameCharacter(
            "Test", 30, 1,
            mutableListOf(
                Item("Sword", 10, 100, 10, 2, AttackCategory.PHYSICAL, WeaponCategory.SWORD, 1),
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

        val invalidAttackerSpot =  Spot(Position(1,1), Terrain.PLAIN, CharacterPair(invalidAttacker, null))

        val defender = GameCharacter(
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
        val defenderSpot = Spot(Position(1,3), Terrain.PLAIN, CharacterPair(defender, null))

        val attacker = GameCharacter(
            "Test", 30, 0,
            mutableListOf(
                Item("Sword", 10, 100, 10, 2, AttackCategory.PHYSICAL, WeaponCategory.SWORD, 1),
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
            CharacterClass.SWORDMASTER,
            false
        )

        val invalidAttackerPositionSpot =  Spot(Position(4,1), Terrain.PLAIN, CharacterPair(attacker, null))
        val attackerSpot =  Spot(Position(1,1), Terrain.PLAIN, CharacterPair(attacker, null))

        Assertions.assertThrows(OutOfRangeException::class.java) {
            battleService.battle(invalidAttackerPositionSpot, defenderSpot)
        }

        attacker.currentEquippedItem = 5
        Assertions.assertThrows(NoItemEquippedException::class.java) {
            battleService.battle(attackerSpot, defenderSpot)
        }

        Assertions.assertThrows(StaffInBattleException::class.java) {
            battleService.battle(invalidAttackerSpot, defenderSpot)
        }

        attacker.currentEquippedItem = 0
        defender.currentEquippedItem = 5

        Assertions.assertDoesNotThrow {
            battleService.battle(attackerSpot, defenderSpot)
        }
    }

    @Test
    fun `test weapon triangle superior`() {
        val attacker = GameCharacter(
            "Test", 30, 0,
            mutableListOf(
                Item("Sword", 5, 500, 0, 1, AttackCategory.PHYSICAL, WeaponCategory.SWORD, 1),
                Item("Staff", 10, 100, 10, 1, AttackCategory.MAGICAL, WeaponCategory.STAFF, 1),
                Item("Physic", 25, 100, 110, 1, AttackCategory.PHYSICAL, WeaponCategory.SWORD, 1),
                Item("Magical", 5, 100, 5, 2, AttackCategory.MAGICAL, WeaponCategory.TOME, 1),
                Item("Magical", 5, 100, 5, 2, AttackCategory.MAGICAL, WeaponCategory.STAFF, 1),
            ),
            mapOf(
                Stat.HEALTH to 30,
                Stat.STRENGTH to 0,
                Stat.MAGICK to 15,
                Stat.DEFENSE to 5,
                Stat.RESISTANCE to 5,
                Stat.SKILL to -11,
                Stat.LUCK to 18,
                Stat.SPEED to 12
            ),
            CharacterClass.SWORDMASTER,
            false
        )
        val attackerSpot =  Spot(Position(1,1), Terrain.PLAIN, CharacterPair(attacker, null))

        val defender = GameCharacter(
            "Test", 31, 0,
            mutableListOf(
                Item("Axe", 5, -500, 10, 1, AttackCategory.PHYSICAL, WeaponCategory.AXE, 1),
                Item("Staff", 25, 100, 110, 1, AttackCategory.PHYSICAL, WeaponCategory.STAFF, 1),
                Item("Magical", 5, 100, 5, 2, AttackCategory.MAGICAL, WeaponCategory.TOME, 1),
                Item("Magical", 5, 100, 5, 2, AttackCategory.MAGICAL, WeaponCategory.STAFF, 1),
                Item("Lance", 5, 100, 5, 2, AttackCategory.PHYSICAL, WeaponCategory.LANCE, 1),
            ),
            mapOf(
                Stat.HEALTH to 30,
                Stat.STRENGTH to 10,
                Stat.MAGICK to 15,
                Stat.DEFENSE to -7,
                Stat.RESISTANCE to 5,
                Stat.SKILL to -8,
                Stat.LUCK to 18,
                Stat.SPEED to 12
            ),
            CharacterClass.WARRIOR,
            false
        )
        val defenderSpot = Spot(Position(1,2), Terrain.FORREST, CharacterPair(defender, null))

        Assertions.assertDoesNotThrow {
            battleService.battle(attackerSpot, defenderSpot)
        }

        Assertions.assertEquals(5, defender.remainingHealth)
        Assertions.assertEquals(30, attacker.remainingHealth)

    }

    @Test
    fun `test weapon triangle normal`() {
        val attacker = GameCharacter(
            "Test", 30, 0,
            mutableListOf(
                Item("Sword", 5, 500, 0, 1, AttackCategory.PHYSICAL, WeaponCategory.SWORD, 1),
                Item("Staff", 10, 100, 10, 1, AttackCategory.MAGICAL, WeaponCategory.STAFF, 1),
                Item("Physic", 25, 100, 110, 1, AttackCategory.PHYSICAL, WeaponCategory.SWORD, 1),
                Item("Magical", 5, 100, 5, 2, AttackCategory.MAGICAL, WeaponCategory.TOME, 1),
                Item("Magical", 5, 100, 5, 2, AttackCategory.MAGICAL, WeaponCategory.STAFF, 1),
            ),
            mapOf(
                Stat.HEALTH to 30,
                Stat.STRENGTH to 0,
                Stat.MAGICK to 15,
                Stat.DEFENSE to 5,
                Stat.RESISTANCE to 5,
                Stat.SKILL to -11,
                Stat.LUCK to 18,
                Stat.SPEED to 12
            ),
            CharacterClass.SWORDMASTER,
            false
        )
        val attackerSpot =  Spot(Position(1,1), Terrain.PLAIN, CharacterPair(attacker, null))

        val defender = GameCharacter(
            "Test", 31, 0,
            mutableListOf(
                Item("Sword", 5, -500, 10, 1, AttackCategory.PHYSICAL, WeaponCategory.SWORD, 1),
                Item("Staff", 25, 100, 110, 1, AttackCategory.PHYSICAL, WeaponCategory.STAFF, 1),
                Item("Magical", 5, 100, 5, 2, AttackCategory.MAGICAL, WeaponCategory.TOME, 1),
                Item("Magical", 5, 100, 5, 2, AttackCategory.MAGICAL, WeaponCategory.STAFF, 1),
                Item("Lance", 5, 100, 5, 2, AttackCategory.PHYSICAL, WeaponCategory.LANCE, 1),
            ),
            mapOf(
                Stat.HEALTH to 30,
                Stat.STRENGTH to 10,
                Stat.MAGICK to 15,
                Stat.DEFENSE to -7,
                Stat.RESISTANCE to 5,
                Stat.SKILL to -8,
                Stat.LUCK to 18,
                Stat.SPEED to 12
            ),
            CharacterClass.WARRIOR,
            false
        )
        val defenderSpot = Spot(Position(1,2), Terrain.PLAIN, CharacterPair(defender, null))

        Assertions.assertDoesNotThrow {
            battleService.battle(attackerSpot, defenderSpot)
        }

        Assertions.assertEquals(7, defender.remainingHealth)
        Assertions.assertEquals(30, attacker.remainingHealth)

        defender.remainingHealth = 31
        defender.currentEquippedItem = 4

        attackerSpot.standingCharacter?.leadCharacter?.moved = false

        Assertions.assertDoesNotThrow {
            battleService.battle(attackerSpot, defenderSpot)
        }

        Assertions.assertEquals(7, defender.remainingHealth)
    }

    @Test
    fun `test weapon triangle inferior`() {
        val attacker = GameCharacter(
            "Test", 30, 0,
            mutableListOf(
                Item("Sword", 5, 500, 0, 1, AttackCategory.PHYSICAL, WeaponCategory.SWORD, 1),
                Item("Staff", 10, 100, 10, 1, AttackCategory.MAGICAL, WeaponCategory.STAFF, 1),
                Item("Physic", 25, 100, 110, 1, AttackCategory.PHYSICAL, WeaponCategory.SWORD, 1),
                Item("Magical", 5, 100, 5, 2, AttackCategory.MAGICAL, WeaponCategory.TOME, 1),
                Item("Magical", 5, 100, 5, 2, AttackCategory.MAGICAL, WeaponCategory.STAFF, 1),
            ),
            mapOf(
                Stat.HEALTH to 30,
                Stat.STRENGTH to 0,
                Stat.MAGICK to 15,
                Stat.DEFENSE to 5,
                Stat.RESISTANCE to 5,
                Stat.SKILL to -11,
                Stat.LUCK to 18,
                Stat.SPEED to 12
            ),
            CharacterClass.SWORDMASTER,
            false
        )
        val attackerSpot =  Spot(Position(1,1), Terrain.PLAIN, CharacterPair(attacker, null))

        val defender = GameCharacter(
            "Test", 31, 0,
            mutableListOf(
                Item("Lance", 5, -500, 10, 1, AttackCategory.PHYSICAL, WeaponCategory.LANCE, 1),
                Item("Staff", 25, 100, 110, 1, AttackCategory.PHYSICAL, WeaponCategory.STAFF, 1),
                Item("Magical", 5, 100, 5, 2, AttackCategory.MAGICAL, WeaponCategory.TOME, 1),
                Item("Magical", 5, 100, 5, 2, AttackCategory.MAGICAL, WeaponCategory.STAFF, 1),
                Item("Lance", 5, 100, 5, 2, AttackCategory.PHYSICAL, WeaponCategory.LANCE, 1),
            ),
            mapOf(
                Stat.HEALTH to 30,
                Stat.STRENGTH to 10,
                Stat.MAGICK to 15,
                Stat.DEFENSE to -10,
                Stat.RESISTANCE to 5,
                Stat.SKILL to -8,
                Stat.LUCK to 18,
                Stat.SPEED to 12
            ),
            CharacterClass.PALADIN,
            false
        )
        val defenderSpot = Spot(Position(1,2), Terrain.PLAIN, CharacterPair(defender, null))

        Assertions.assertDoesNotThrow {
            battleService.battle(attackerSpot, defenderSpot)
        }

        Assertions.assertEquals(13, defender.remainingHealth)
        Assertions.assertEquals(30, attacker.remainingHealth)
    }

    @Test
    fun `test miss calculator`() {
        val attacker = GameCharacter(
            "Test", 30, 0,
            mutableListOf(
                Item("Sword", 5, 100, 0, 1, AttackCategory.PHYSICAL, WeaponCategory.SWORD, 1),
                Item("Staff", 10, 100, 10, 1, AttackCategory.MAGICAL, WeaponCategory.STAFF, 1),
                Item("Physic", 25, 100, 110, 1, AttackCategory.PHYSICAL, WeaponCategory.SWORD, 1),
                Item("Magical", 5, 100, 5, 2, AttackCategory.MAGICAL, WeaponCategory.TOME, 1),
                Item("Magical", 5, 100, 5, 2, AttackCategory.MAGICAL, WeaponCategory.STAFF, 1),
            ),
            mapOf(
                Stat.HEALTH to 30,
                Stat.STRENGTH to 0,
                Stat.MAGICK to 15,
                Stat.DEFENSE to 5,
                Stat.RESISTANCE to 5,
                Stat.SKILL to -11,
                Stat.LUCK to 18,
                Stat.SPEED to 12
            ),
            CharacterClass.SWORDMASTER,
            false
        )
        val attackerSpot =  Spot(Position(1,1), Terrain.PLAIN, CharacterPair(attacker, null))

        val defender = GameCharacter(
            "Test", 31, 0,
            mutableListOf(
                Item("Sword", 5, -500, 10, 1, AttackCategory.PHYSICAL, WeaponCategory.SWORD, 1),
                Item("Staff", 25, 100, 110, 1, AttackCategory.PHYSICAL, WeaponCategory.STAFF, 1),
                Item("Magical", 5, 100, 5, 2, AttackCategory.MAGICAL, WeaponCategory.TOME, 1),
                Item("Magical", 5, 100, 5, 2, AttackCategory.MAGICAL, WeaponCategory.STAFF, 1),
                Item("Lance", 5, 100, 5, 2, AttackCategory.PHYSICAL, WeaponCategory.LANCE, 1),
            ),
            mapOf(
                Stat.HEALTH to 30,
                Stat.STRENGTH to 10,
                Stat.MAGICK to 15,
                Stat.DEFENSE to -7,
                Stat.RESISTANCE to 5,
                Stat.SKILL to -8,
                Stat.LUCK to 82,
                Stat.SPEED to 12
            ),
            CharacterClass.WARRIOR,
            false
        )
        val defenderSpot = Spot(Position(1,2), Terrain.MOUNTAIN, CharacterPair(defender, null))

        Assertions.assertDoesNotThrow {
            battleService.battle(attackerSpot, defenderSpot)
        }

        Assertions.assertEquals(31, defender.remainingHealth)
        Assertions.assertEquals(30, attacker.remainingHealth)
    }

    @Test
    fun `test death after double attack`() {
        val attacker = GameCharacter(
            "Test", 30, 0,
            mutableListOf(
                Item("Sword", 5, 500, 0, 1, AttackCategory.PHYSICAL, WeaponCategory.SWORD, 1),
                Item("Staff", 10, 100, 10, 1, AttackCategory.MAGICAL, WeaponCategory.STAFF, 1),
                Item("Physic", 25, 100, 110, 1, AttackCategory.PHYSICAL, WeaponCategory.SWORD, 1),
                Item("Magical", 5, 100, 5, 2, AttackCategory.MAGICAL, WeaponCategory.TOME, 1),
                Item("Magical", 5, 100, 5, 2, AttackCategory.MAGICAL, WeaponCategory.STAFF, 1),
            ),
            mapOf(
                Stat.HEALTH to 30,
                Stat.STRENGTH to 0,
                Stat.MAGICK to 15,
                Stat.DEFENSE to 5,
                Stat.RESISTANCE to 5,
                Stat.SKILL to -11,
                Stat.LUCK to 18,
                Stat.SPEED to 12
            ),
            CharacterClass.SWORDMASTER,
            false
        )
        val attackerSpot =  Spot(Position(1,1), Terrain.PLAIN, CharacterPair(attacker, null))

        val defender = GameCharacter(
            "Test", 24, 0,
            mutableListOf(
                Item("Sword", 5, -500, 10, 1, AttackCategory.PHYSICAL, WeaponCategory.SWORD, 1),
                Item("Staff", 25, 100, 110, 1, AttackCategory.PHYSICAL, WeaponCategory.STAFF, 1),
                Item("Magical", 5, 100, 5, 2, AttackCategory.MAGICAL, WeaponCategory.TOME, 1),
                Item("Magical", 5, 100, 5, 2, AttackCategory.MAGICAL, WeaponCategory.STAFF, 1),
                Item("Lance", 5, 100, 5, 2, AttackCategory.PHYSICAL, WeaponCategory.LANCE, 1),
            ),
            mapOf(
                Stat.HEALTH to 30,
                Stat.STRENGTH to 10,
                Stat.MAGICK to 15,
                Stat.DEFENSE to -7,
                Stat.RESISTANCE to 5,
                Stat.SKILL to -8,
                Stat.LUCK to 18,
                Stat.SPEED to 12
            ),
            CharacterClass.WARRIOR,
            false
        )
        val defenderSpot = Spot(Position(1,2), Terrain.PLAIN, CharacterPair(defender, null))

        Assertions.assertDoesNotThrow {
            battleService.battle(attackerSpot, defenderSpot)
        }

        Assertions.assertNull(defenderSpot.standingCharacter)
    }

    @Test
    fun `test default calculator(death of attacker from counter)`() {
        val attacker = GameCharacter(
            "Test", 1, 0,
            mutableListOf(
                Item("Sword", 5, 500, 0, 1, AttackCategory.PHYSICAL, WeaponCategory.SWORD, 1),
                Item("Staff", 10, 100, 10, 1, AttackCategory.MAGICAL, WeaponCategory.STAFF, 1),
                Item("Physic", 25, 100, 110, 1, AttackCategory.PHYSICAL, WeaponCategory.SWORD, 1),
                Item("Magical", 5, 100, 5, 2, AttackCategory.MAGICAL, WeaponCategory.TOME, 1),
                Item("Magical", 5, 100, 5, 2, AttackCategory.MAGICAL, WeaponCategory.STAFF, 1),
            ),
            mapOf(
                Stat.HEALTH to 30,
                Stat.STRENGTH to 0,
                Stat.MAGICK to 15,
                Stat.DEFENSE to 5,
                Stat.RESISTANCE to 5,
                Stat.SKILL to -11,
                Stat.LUCK to 18,
                Stat.SPEED to 12
            ),
            CharacterClass.SWORDMASTER,
            false
        )
        val attackerSpot =  Spot(Position(1,1), Terrain.PLAIN, CharacterPair(attacker, null))

        val defender = GameCharacter(
            "Test", 31, 0,
            mutableListOf(
                Item("Bow", 5, 500, 10, 2, AttackCategory.PHYSICAL, WeaponCategory.BOW, 1),
                Item("Staff", 25, 100, 110, 1, AttackCategory.PHYSICAL, WeaponCategory.STAFF, 1),
                Item("Magical", 5, 100, 5, 2, AttackCategory.MAGICAL, WeaponCategory.TOME, 1),
                Item("Magical", 5, 100, 5, 2, AttackCategory.MAGICAL, WeaponCategory.STAFF, 1),
                Item("Lance", 5, 100, 5, 2, AttackCategory.PHYSICAL, WeaponCategory.LANCE, 1),
            ),
            mapOf(
                Stat.HEALTH to 30,
                Stat.STRENGTH to 10,
                Stat.MAGICK to 15,
                Stat.DEFENSE to -7,
                Stat.RESISTANCE to 5,
                Stat.SKILL to -8,
                Stat.LUCK to 18,
                Stat.SPEED to 12
            ),
            CharacterClass.WARRIOR,
            false
        )
        val defenderSpot = Spot(Position(1,2), Terrain.PLAIN, CharacterPair(defender, null))

        Assertions.assertDoesNotThrow {
            battleService.battle(attackerSpot, defenderSpot)
        }

        Assertions.assertEquals(19, defender.remainingHealth)
        Assertions.assertNull(attackerSpot.standingCharacter)
    }

    @Test
    fun `test battle forecast`() {
        val attacker = GameCharacter(
            "Test", 1, 0,
            mutableListOf(
                Item("Sword", 5, 100, 20, 1, AttackCategory.PHYSICAL, WeaponCategory.SWORD, 1),
                Item("Staff", 10, 100, 10, 1, AttackCategory.MAGICAL, WeaponCategory.STAFF, 1),
                Item("Physic", 25, 100, 110, 1, AttackCategory.PHYSICAL, WeaponCategory.SWORD, 1),
                Item("Magical", 5, 100, 5, 2, AttackCategory.MAGICAL, WeaponCategory.TOME, 1),
                Item("Magical", 5, 100, 5, 2, AttackCategory.MAGICAL, WeaponCategory.STAFF, 1),
            ),
            mapOf(
                Stat.HEALTH to 30,
                Stat.STRENGTH to 0,
                Stat.MAGICK to 15,
                Stat.DEFENSE to 5,
                Stat.RESISTANCE to 5,
                Stat.SKILL to -11,
                Stat.LUCK to 18,
                Stat.SPEED to 12
            ),
            CharacterClass.SWORDMASTER,
            false
        )
        val attackerSpot =  Spot(Position(1,1), Terrain.PLAIN, CharacterPair(attacker, null))

        val defender = GameCharacter(
            "Test", 31, 0,
            mutableListOf(
                Item("Bow", 5, 100, 30, 2, AttackCategory.PHYSICAL, WeaponCategory.BOW, 1),
                Item("Staff", 25, 100, 110, 1, AttackCategory.PHYSICAL, WeaponCategory.STAFF, 1),
                Item("Magical", 5, 100, 5, 2, AttackCategory.MAGICAL, WeaponCategory.TOME, 1),
                Item("Magical", 5, 100, 5, 2, AttackCategory.MAGICAL, WeaponCategory.STAFF, 1),
                Item("Lance", 5, 100, 5, 2, AttackCategory.PHYSICAL, WeaponCategory.LANCE, 1),
            ),
            mapOf(
                Stat.HEALTH to 30,
                Stat.STRENGTH to 10,
                Stat.MAGICK to 15,
                Stat.DEFENSE to -7,
                Stat.RESISTANCE to 5,
                Stat.SKILL to -8,
                Stat.LUCK to 18,
                Stat.SPEED to 12
            ),
            CharacterClass.WARRIOR,
            false
        )
        val defenderSpot = Spot(Position(1,2), Terrain.PLAIN, CharacterPair(defender, null))

        val forecast = battleService.battleForecast(attackerSpot, defenderSpot)

        Assertions.assertEquals(
            BattleForecast(12,72,2,true,16,63,12,false), forecast
        )
    }

    @Test
    fun `test offensive skill calculator`() {
        val attacker = GameCharacter(
            "Test", 30, 0,
            mutableListOf(
                Item("Sword", 5, 500, 0, 1, AttackCategory.PHYSICAL, WeaponCategory.SWORD, 1),
                Item("Staff", 10, 100, 10, 1, AttackCategory.MAGICAL, WeaponCategory.STAFF, 1),
                Item("Physic", 25, 100, 110, 1, AttackCategory.PHYSICAL, WeaponCategory.SWORD, 1),
                Item("Magical", 5, 100, 5, 2, AttackCategory.MAGICAL, WeaponCategory.TOME, 1),
                Item("Magical", 5, 100, 5, 2, AttackCategory.MAGICAL, WeaponCategory.STAFF, 1),
            ),
            mapOf(
                Stat.HEALTH to 30,
                Stat.STRENGTH to 0,
                Stat.MAGICK to 15,
                Stat.DEFENSE to 5,
                Stat.RESISTANCE to 5,
                Stat.SKILL to 189,
                Stat.LUCK to 18,
                Stat.SPEED to 12
            ),
            CharacterClass.SWORDMASTER,
            false
        )
        val attackerSpot =  Spot(Position(1,1), Terrain.PLAIN, CharacterPair(attacker, null))

        val defender = GameCharacter(
            "Test", 30, 0,
            mutableListOf(
                Item("Sword", 5, -500, 10, 1, AttackCategory.PHYSICAL, WeaponCategory.SWORD, 1),
                Item("Staff", 25, 100, 110, 1, AttackCategory.PHYSICAL, WeaponCategory.STAFF, 1),
                Item("Magical", 5, 100, 5, 2, AttackCategory.MAGICAL, WeaponCategory.TOME, 1),
                Item("Magical", 5, 100, 5, 2, AttackCategory.MAGICAL, WeaponCategory.STAFF, 1),
                Item("Lance", 5, 100, 5, 2, AttackCategory.PHYSICAL, WeaponCategory.LANCE, 1),
            ),
            mapOf(
                Stat.HEALTH to 30,
                Stat.STRENGTH to 10,
                Stat.MAGICK to 15,
                Stat.DEFENSE to -7,
                Stat.RESISTANCE to 5,
                Stat.SKILL to -8,
                Stat.LUCK to 18,
                Stat.SPEED to 12
            ),
            CharacterClass.WARRIOR,
            false
        )
        val defenderSpot = Spot(Position(1,2), Terrain.PLAIN, CharacterPair(defender, null))

        val battleCourse = battleService.battle(attackerSpot, defenderSpot)

        Assertions.assertEquals(
            listOf(
                OffensiveSkill.AETHER
            ).map { it.displayName }, battleCourse.map { it.displayName }
        )
    }

    @Test
    fun `test defensive skill calculator`() {
        val attacker = GameCharacter(
            "Test", 30, 0,
            mutableListOf(
                Item("Sword", 5, 500, 0, 1, AttackCategory.PHYSICAL, WeaponCategory.SWORD, 1),
                Item("Staff", 10, 100, 10, 1, AttackCategory.MAGICAL, WeaponCategory.STAFF, 1),
                Item("Physic", 25, 100, 110, 1, AttackCategory.PHYSICAL, WeaponCategory.SWORD, 1),
                Item("Magical", 5, 100, 5, 2, AttackCategory.MAGICAL, WeaponCategory.TOME, 1),
                Item("Magical", 5, 100, 5, 2, AttackCategory.MAGICAL, WeaponCategory.STAFF, 1),
            ),
            mapOf(
                Stat.HEALTH to 30,
                Stat.STRENGTH to 0,
                Stat.MAGICK to 15,
                Stat.DEFENSE to 5,
                Stat.RESISTANCE to 5,
                Stat.SKILL to -11,
                Stat.LUCK to 18,
                Stat.SPEED to 12
            ),
            CharacterClass.SWORDMASTER,
            false
        )
        val attackerSpot =  Spot(Position(1,1), Terrain.PLAIN, CharacterPair(attacker, null))

        val defender = GameCharacter(
            "Test", 16, 0,
            mutableListOf(
                Item("Tome", 5, -500, 10, 1, AttackCategory.MAGICAL, WeaponCategory.TOME, 1),
                Item("Staff", 25, 100, 110, 1, AttackCategory.PHYSICAL, WeaponCategory.STAFF, 1),
                Item("Magical", 5, 100, 5, 2, AttackCategory.MAGICAL, WeaponCategory.TOME, 1),
                Item("Magical", 5, 100, 5, 2, AttackCategory.MAGICAL, WeaponCategory.STAFF, 1),
                Item("Lance", 5, 100, 5, 2, AttackCategory.PHYSICAL, WeaponCategory.LANCE, 1),
            ),
            mapOf(
                Stat.HEALTH to 30,
                Stat.STRENGTH to 10,
                Stat.MAGICK to 15,
                Stat.DEFENSE to -4,
                Stat.RESISTANCE to 5,
                Stat.SKILL to -5,
                Stat.LUCK to 100,
                Stat.SPEED to 12
            ),
            CharacterClass.SAGE,
            false
        )
        val defenderSpot = Spot(Position(1,2), Terrain.PLAIN, CharacterPair(defender, null))

        val battleCourse = battleService.battle(attackerSpot, defenderSpot)

        Assertions.assertEquals(1, defender.remainingHealth)
        Assertions.assertEquals(30, attacker.remainingHealth)

        Assertions.assertEquals(
            listOf(
                DefaultBattleCalculator(), DefensiveSkill.MIRACLE, MissBattleCalculator(),
                DefaultBattleCalculator(), DefensiveSkill.MIRACLE
            ).map { it.displayName }, battleCourse.map { it.displayName }
        )

    }

    @Test
    fun `test death of lead character with support`() {
        val attacker = GameCharacter(
            "Test", 30, 0,
            mutableListOf(
                Item("Sword", 5, 500, 0, 1, AttackCategory.PHYSICAL, WeaponCategory.SWORD, 1),
                Item("Staff", 10, 100, 10, 1, AttackCategory.MAGICAL, WeaponCategory.STAFF, 1),
                Item("Physic", 25, 100, 110, 1, AttackCategory.PHYSICAL, WeaponCategory.SWORD, 1),
                Item("Magical", 5, 100, 5, 2, AttackCategory.MAGICAL, WeaponCategory.TOME, 1),
                Item("Magical", 5, 100, 5, 2, AttackCategory.MAGICAL, WeaponCategory.STAFF, 1),
            ),
            mapOf(
                Stat.HEALTH to 30,
                Stat.STRENGTH to 0,
                Stat.MAGICK to 15,
                Stat.DEFENSE to 5,
                Stat.RESISTANCE to 5,
                Stat.SKILL to -11,
                Stat.LUCK to 18,
                Stat.SPEED to 12
            ),
            CharacterClass.SWORDMASTER,
            false
        )
        val attackerSpot =  Spot(Position(1,1), Terrain.PLAIN, CharacterPair(attacker, null))

        val defender = GameCharacter(
            "Test", 1, 0,
            mutableListOf(
                Item("Sword", 5, -500, 10, 1, AttackCategory.PHYSICAL, WeaponCategory.SWORD, 1),
                Item("Staff", 25, 100, 110, 1, AttackCategory.PHYSICAL, WeaponCategory.STAFF, 1),
                Item("Magical", 5, 100, 5, 2, AttackCategory.MAGICAL, WeaponCategory.TOME, 1),
                Item("Magical", 5, 100, 5, 2, AttackCategory.MAGICAL, WeaponCategory.STAFF, 1),
                Item("Lance", 5, 100, 5, 2, AttackCategory.PHYSICAL, WeaponCategory.LANCE, 1),
            ),
            mapOf(
                Stat.HEALTH to 30,
                Stat.STRENGTH to 10,
                Stat.MAGICK to 15,
                Stat.DEFENSE to -7,
                Stat.RESISTANCE to 5,
                Stat.SKILL to -8,
                Stat.LUCK to 18,
                Stat.SPEED to 12
            ),
            CharacterClass.WARRIOR,
            false
        )
        val defenderSpot = Spot(Position(1,2), Terrain.PLAIN, CharacterPair(defender, attacker))

        Assertions.assertDoesNotThrow {
            battleService.battle(attackerSpot, defenderSpot)
        }

        Assertions.assertNotNull(defenderSpot.standingCharacter)
        Assertions.assertEquals(attacker, defenderSpot.standingCharacter!!.leadCharacter)
    }

}