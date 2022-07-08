package pl.ms.fire.emblem.business.services

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import pl.ms.fire.emblem.business.entities.CharacterPair
import pl.ms.fire.emblem.business.exceptions.NoCharacterOnSpotException
import pl.ms.fire.emblem.business.exceptions.NoSupportCharacterException
import pl.ms.fire.emblem.business.exceptions.SeparatePairException
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
                    Item("Physic", 5, 100, 10, 1, AttackCategory.PHYSICAL, WeaponCategory.SWORD),
                    Item("Physic", 25, 100, 110, 1, AttackCategory.PHYSICAL, WeaponCategory.SWORD),
                    Item("Magical", 5, 100, 5, 2, AttackCategory.MAGICAL, WeaponCategory.TOME),
                    Item("Magical", 5, 100, 5, 2, AttackCategory.MAGICAL, WeaponCategory.STAFF)
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
                    Item("Physic", 5, 100, 10, 1, AttackCategory.PHYSICAL, WeaponCategory.SWORD),
                    Item("Physic", 25, 100, 110, 1, AttackCategory.PHYSICAL, WeaponCategory.SWORD),
                    Item("Magical", 5, 100, 5, 2, AttackCategory.MAGICAL, WeaponCategory.TOME),
                    Item("Magical", 5, 100, 5, 2, AttackCategory.MAGICAL, WeaponCategory.STAFF)
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

        Assertions.assertThrows(NoCharacterOnSpotException::class.java) {
            characterManagementService.joinIntoPair(spot1, spotNull)
        }

        Assertions.assertDoesNotThrow {
            characterManagementService.joinIntoPair(spot1, spot2)
        }

        Assertions.assertNull(spot2.standingCharacter)

        Assertions.assertEquals(
            copy, spot1.standingCharacter
        )

    }

    @Test
    fun `test separate pair function`() {
        val characterPair = CharacterPair(
            GameCharacter(
                "Test", 30, 0,
                mutableListOf(
                    Item("Physic", 5, 100, 10, 1, AttackCategory.PHYSICAL, WeaponCategory.SWORD),
                    Item("Physic", 25, 100, 110, 1, AttackCategory.PHYSICAL, WeaponCategory.SWORD),
                    Item("Magical", 5, 100, 5, 2, AttackCategory.MAGICAL, WeaponCategory.TOME),
                    Item("Magical", 5, 100, 5, 2, AttackCategory.MAGICAL, WeaponCategory.STAFF)
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
                    Item("Physic", 5, 100, 10, 1, AttackCategory.PHYSICAL, WeaponCategory.SWORD),
                    Item("Physic", 25, 100, 110, 1, AttackCategory.PHYSICAL, WeaponCategory.SWORD),
                    Item("Magical", 5, 100, 5, 2, AttackCategory.MAGICAL, WeaponCategory.TOME),
                    Item("Magical", 5, 100, 5, 2, AttackCategory.MAGICAL, WeaponCategory.STAFF)
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

        Assertions.assertThrows(SeparatePairException::class.java) {
            characterManagementService.separatePair(spot1, spot2)
        }

        Assertions.assertThrows(NoSupportCharacterException::class.java) {
            characterManagementService.separatePair(spot1, spotNull)
        }

        spot1.standingCharacter = copy

        Assertions.assertDoesNotThrow {
            characterManagementService.separatePair(spot1, spotNull)
        }

        Assertions.assertEquals(separate, spotNull.standingCharacter)

    }

}