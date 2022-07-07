package pl.ms.fire.emblem.business.entities

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import pl.ms.fire.emblem.business.exceptions.NoSupportCharacterException
import pl.ms.fire.emblem.business.exceptions.PairAlreadyHaveSupportException
import pl.ms.fire.emblem.business.values.GameCharacter
import pl.ms.fire.emblem.business.values.category.AttackCategory
import pl.ms.fire.emblem.business.values.category.WeaponCategory
import pl.ms.fire.emblem.business.values.character.BattleStat
import pl.ms.fire.emblem.business.values.character.CharacterClass
import pl.ms.fire.emblem.business.values.character.Stat
import pl.ms.fire.emblem.business.values.items.Item

class CharacterPairTest {

    private val characterPair = CharacterPair(
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

    @Test
    fun `test calculation of boosted stats and battle stats`() {
        Assertions.assertEquals(
            mapOf(
                Stat.HEALTH to 55,
                Stat.STRENGTH to 18,
                Stat.MAGICK to 26,
                Stat.DEFENSE to 18,
                Stat.RESISTANCE to 14,
                Stat.SKILL to 22,
                Stat.LUCK to 20,
                Stat.SPEED to 21
            ), characterPair.boostedStats
        )

        Assertions.assertEquals(
            BattleStat(23, 153, 51, 26), characterPair.battleStat
        )
    }

    @Test
    fun `test change with support function`() {

        characterPair.changeWithSupport()

        Assertions.assertEquals(
            mapOf(
                Stat.HEALTH to 55,
                Stat.STRENGTH to 18,
                Stat.MAGICK to 29,
                Stat.DEFENSE to 23,
                Stat.RESISTANCE to 17,
                Stat.SKILL to 25,
                Stat.LUCK to 14,
                Stat.SPEED to 19
            ), characterPair.boostedStats
        )

        Assertions.assertEquals(
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
            ), characterPair.leadCharacter
        )

        Assertions.assertEquals(
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
            ), characterPair.supportCharacter
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

        val separate = characterPair.separatePair()

        Assertions.assertNull(characterPair.supportCharacter)
        Assertions.assertNull(separate.supportCharacter)

        Assertions.assertThrows(NoSupportCharacterException::class.java) {
            separate.separatePair()
        }

        Assertions.assertEquals(
            mapOf(
                Stat.HEALTH to 55,
                Stat.STRENGTH to 14,
                Stat.MAGICK to 20,
                Stat.DEFENSE to 14,
                Stat.RESISTANCE to 10,
                Stat.SKILL to 16,
                Stat.LUCK to 18,
                Stat.SPEED to 17
            ), characterPair.boostedStats
        )

        Assertions.assertEquals(
            mapOf(
                Stat.HEALTH to 55,
                Stat.STRENGTH to 14,
                Stat.MAGICK to 25,
                Stat.DEFENSE to 19,
                Stat.RESISTANCE to 15,
                Stat.SKILL to 21,
                Stat.LUCK to 10,
                Stat.SPEED to 15
            ), separate.boostedStats
        )

    }

    @Test
    fun `test join with another character function`() {

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

        Assertions.assertThrows(PairAlreadyHaveSupportException::class.java) {
            copy.joinWithAnotherCharacter(separate)
        }

        val joinPair = characterPair.joinWithAnotherCharacter(separate)

        Assertions.assertEquals(
            mapOf(
                Stat.HEALTH to 55,
                Stat.STRENGTH to 18,
                Stat.MAGICK to 26,
                Stat.DEFENSE to 18,
                Stat.RESISTANCE to 14,
                Stat.SKILL to 22,
                Stat.LUCK to 20,
                Stat.SPEED to 21
            ), joinPair.boostedStats
        )
    }

    @Test
    fun `test trade support character function`() {
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

        val secondPair = CharacterPair(
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
                    Stat.STRENGTH to 20,
                    Stat.MAGICK to 10,
                    Stat.DEFENSE to 20,
                    Stat.RESISTANCE to 20,
                    Stat.SKILL to 15,
                    Stat.LUCK to 10,
                    Stat.SPEED to 16
                ),
                CharacterClass.DARK_KNIGHT,
                false
            )
        )

        Assertions.assertNotEquals(
            BattleStat(23, 153, 51, 26), secondPair.battleStat
        )

        characterPair.tradeSupportCharacter(secondPair)

        Assertions.assertEquals(
            mapOf(
                Stat.HEALTH to 55,
                Stat.STRENGTH to 20,
                Stat.MAGICK to 24,
                Stat.DEFENSE to 20,
                Stat.RESISTANCE to 16,
                Stat.SKILL to 22,
                Stat.LUCK to 20,
                Stat.SPEED to 23
            ), characterPair.boostedStats
        )

        Assertions.assertEquals(
            mapOf(
                Stat.HEALTH to 55,
                Stat.STRENGTH to 18,
                Stat.MAGICK to 26,
                Stat.DEFENSE to 18,
                Stat.RESISTANCE to 14,
                Stat.SKILL to 22,
                Stat.LUCK to 20,
                Stat.SPEED to 21
            ), secondPair.boostedStats
        )

        Assertions.assertEquals(
            BattleStat(23, 153, 51, 26), secondPair.battleStat
        )
    }

    @Test
    fun `test dead of lead character function`() {
        val deadPair = characterPair.deadOfLeadCharacter()

        Assertions.assertNotNull(deadPair)

        Assertions.assertEquals(
            mapOf(
                Stat.HEALTH to 55,
                Stat.STRENGTH to 14,
                Stat.MAGICK to 25,
                Stat.DEFENSE to 19,
                Stat.RESISTANCE to 15,
                Stat.SKILL to 21,
                Stat.LUCK to 10,
                Stat.SPEED to 15
            ), deadPair?.boostedStats
        )

        Assertions.assertNull(deadPair?.deadOfLeadCharacter())
    }

}