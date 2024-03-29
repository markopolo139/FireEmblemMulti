package pl.ms.fire.emblem.business.services

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import pl.ms.fire.emblem.business.entities.CharacterPair
import pl.ms.fire.emblem.business.exceptions.battle.OutOfRangeException
import pl.ms.fire.emblem.business.exceptions.character.NoCharacterOnSpotException
import pl.ms.fire.emblem.business.exceptions.character.NoSupportCharacterException
import pl.ms.fire.emblem.business.exceptions.character.SeparatePairException
import pl.ms.fire.emblem.business.serices.CharacterManagementService
import pl.ms.fire.emblem.business.values.GameCharacter
import pl.ms.fire.emblem.business.values.board.Position
import pl.ms.fire.emblem.business.values.board.Spot
import pl.ms.fire.emblem.business.values.board.Terrain
import pl.ms.fire.emblem.business.values.category.AttackCategory
import pl.ms.fire.emblem.business.values.category.WeaponCategory
import pl.ms.fire.emblem.business.values.character.CharacterClass
import pl.ms.fire.emblem.business.values.character.Stat
import pl.ms.fire.emblem.business.values.items.Item

class CharacterManagementTest {

    private val characterManagementService = CharacterManagementService()

    @Test
    fun `test join into pair function`() {
        val characterPair = CharacterPair(
            GameCharacter(
                "Test", 30, 0,
                mutableListOf(
                    Item("Physic", 5, 100, 10, 1, AttackCategory.PHYSICAL, WeaponCategory.SWORD, 1),
                    Item("Physic", 25, 100, 110, 1, AttackCategory.PHYSICAL, WeaponCategory.SWORD, 1),
                    Item("Magical", 5, 100, 5, 2, AttackCategory.MAGICAL, WeaponCategory.TOME, 1),
                    Item("Magical", 5, 100, 5, 2, AttackCategory.MAGICAL, WeaponCategory.STAFF, 1)
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
            ),
            GameCharacter(
                "Test", 30, 0,
                mutableListOf(
                    Item("Physic", 5, 100, 10, 1, AttackCategory.PHYSICAL, WeaponCategory.SWORD, 1),
                    Item("Physic", 25, 100, 110, 1, AttackCategory.PHYSICAL, WeaponCategory.SWORD, 1),
                    Item("Magical", 5, 100, 5, 2, AttackCategory.MAGICAL, WeaponCategory.TOME, 1),
                    Item("Magical", 5, 100, 5, 2, AttackCategory.MAGICAL, WeaponCategory.STAFF, 1)
                ),
                mapOf(
                    Stat.HEALTH to 30,
                    Stat.STRENGTH to 10,
                    Stat.MAGICK to 20,
                    Stat.DEFENSE to 10,
                    Stat.RESISTANCE to 10,
                    Stat.SKILL to 15,
                    Stat.LUCK to 10,
                    Stat.SPEED to 10
                ),
                CharacterClass.DARK_KNIGHT,
                false
            )
        )

        val copy = CharacterPair(characterPair.supportCharacter!!, characterPair.leadCharacter)
        val separate = characterPair.separatePair()

        val spot1 = Spot(Position(1,1), Terrain.PLAIN, characterPair)
        val spot2 = Spot(Position(1,1), Terrain.PLAIN, separate)
        val spotNull = Spot(Position(1,1), Terrain.PLAIN, null)

        Assertions.assertThrows(NoCharacterOnSpotException::class.java) {
            characterManagementService.joinIntoPair(spot1, spotNull)
        }

        Assertions.assertDoesNotThrow {
            characterManagementService.joinIntoPair(spot1, spot2)
        }

        Assertions.assertNull(spot1.standingCharacter)

        Assertions.assertEquals(
            copy, spot2.standingCharacter
        )

    }

    @Test
    fun `test separate pair function`() {
        val characterPair = CharacterPair(
            GameCharacter(
                "Test", 30, 0,
                mutableListOf(
                    Item("Physic", 5, 100, 10, 1, AttackCategory.PHYSICAL, WeaponCategory.SWORD, 1),
                    Item("Physic", 25, 100, 110, 1, AttackCategory.PHYSICAL, WeaponCategory.SWORD, 1),
                    Item("Magical", 5, 100, 5, 2, AttackCategory.MAGICAL, WeaponCategory.TOME, 1),
                    Item("Magical", 5, 100, 5, 2, AttackCategory.MAGICAL, WeaponCategory.STAFF, 1)
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
            ),
            GameCharacter(
                "Test", 30, 0,
                mutableListOf(
                    Item("Physic", 5, 100, 10, 1, AttackCategory.PHYSICAL, WeaponCategory.SWORD, 1),
                    Item("Physic", 25, 100, 110, 1, AttackCategory.PHYSICAL, WeaponCategory.SWORD, 1),
                    Item("Magical", 5, 100, 5, 2, AttackCategory.MAGICAL, WeaponCategory.TOME, 1),
                    Item("Magical", 5, 100, 5, 2, AttackCategory.MAGICAL, WeaponCategory.STAFF, 1)
                ),
                mapOf(
                    Stat.HEALTH to 30,
                    Stat.STRENGTH to 10,
                    Stat.MAGICK to 20,
                    Stat.DEFENSE to 10,
                    Stat.RESISTANCE to 10,
                    Stat.SKILL to 15,
                    Stat.LUCK to 10,
                    Stat.SPEED to 10
                ),
                CharacterClass.DARK_KNIGHT,
                false
            )
        )

        val copy = CharacterPair(characterPair.leadCharacter, characterPair.supportCharacter)
        val separate = characterPair.separatePair()

        val spot1 = Spot(Position(1,1), Terrain.PLAIN, characterPair)
        val spot2 = Spot(Position(1,1), Terrain.PLAIN, separate)
        val spotNull = Spot(Position(1,1), Terrain.PLAIN, null)
        val spotNoCharacterOnSpot= Spot(Position(1,2), Terrain.PLAIN, null)

        Assertions.assertThrows(SeparatePairException::class.java) {
            characterManagementService.separatePair(spot1, spot2)
        }

        Assertions.assertThrows(OutOfRangeException::class.java) {
            characterManagementService.separatePair(spot1, spotNull)
        }

        Assertions.assertThrows(NoSupportCharacterException::class.java) {
            characterManagementService.separatePair(spot1, spotNoCharacterOnSpot)
        }

        spot1.standingCharacter = copy

        Assertions.assertDoesNotThrow {
            characterManagementService.separatePair(spot1, spotNoCharacterOnSpot)
        }

        Assertions.assertEquals(separate, spotNoCharacterOnSpot.standingCharacter)

    }

}