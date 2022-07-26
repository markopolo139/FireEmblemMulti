package pl.ms.fire.emblem.business.values

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import pl.ms.fire.emblem.business.entities.CharacterPair
import pl.ms.fire.emblem.business.values.category.AttackCategory
import pl.ms.fire.emblem.business.values.category.WeaponCategory
import pl.ms.fire.emblem.business.values.character.BattleStat
import pl.ms.fire.emblem.business.values.character.CharacterClass
import pl.ms.fire.emblem.business.values.character.Stat
import pl.ms.fire.emblem.business.values.items.Item

class BattleStatTest {

    @Test
    fun `battle stat calculation test`() {
        val characterPair = CharacterPair(
            GameCharacter(
                "Test", 30, 0,
                mutableListOf(
                    Item("Physic", 5, 100, 10, 1, AttackCategory.PHYSICAL, WeaponCategory.SWORD, 1),
                    Item("Magical", 5, 100, 5, 2, AttackCategory.MAGICAL, WeaponCategory.TOME, 2),
                    Item("Magical", 5, 100, 5, 2, AttackCategory.MAGICAL, WeaponCategory.STAFF, 3)
                ),
                mapOf(
                    Stat.HEALTH to 30,
                    Stat.STRENGTH to 10,
                    Stat.MAGICK to 15,
                    Stat.DEFENSE to 5,
                    Stat.RESISTANCE to 5,
                    Stat.SKILL to 10,
                    Stat.LUCK to 10,
                    Stat.SPEED to 12
                ),
                CharacterClass.DARK_KNIGHT,
                false
            ), null
        )

        Assertions.assertEquals(BattleStat(19, 129, 30, 18, 16), characterPair.battleStat)

        characterPair.leadCharacter.currentEquippedItem = 1
        characterPair.updateBattleStat()
        Assertions.assertEquals(BattleStat(25, 129, 30, 13, 15), characterPair.battleStat)

        characterPair.leadCharacter.currentEquippedItem = 2
        characterPair.updateBattleStat()
        Assertions.assertEquals(BattleStat(0, 0, 30, 0, 14), characterPair.battleStat)

    }

}