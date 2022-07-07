package pl.ms.fire.emblem.business.values

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import pl.ms.fire.emblem.business.entities.CharacterPair
import pl.ms.fire.emblem.business.values.category.AttackCategory
import pl.ms.fire.emblem.business.values.category.WeaponCategory
import pl.ms.fire.emblem.business.values.character.CharacterClass
import pl.ms.fire.emblem.business.values.character.OffensiveSkill
import pl.ms.fire.emblem.business.values.character.Stat
import pl.ms.fire.emblem.business.values.items.Item

class OffensiveSkillsTest {

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
        ), null
    )
    private val enemyPair = CharacterPair(
        GameCharacter(
            "Test", 55, 0,
            mutableListOf(
                Item("Physic", 5, 100, 10, 1, AttackCategory.PHYSICAL, WeaponCategory.SWORD),
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
        ), null
    )

    @Test
    fun `Dmg calculator test`() {
        Assertions.assertEquals(15, OffensiveSkill.IGNIS.calcDmg(characterPair, enemyPair))
        Assertions.assertEquals(12, OffensiveSkill.LUNA.calcDmg(characterPair, enemyPair))
        Assertions.assertEquals(10, OffensiveSkill.ASTRA.calcDmg(characterPair, enemyPair))
        Assertions.assertEquals(17, OffensiveSkill.VENGEANCE.calcDmg(characterPair, enemyPair))
        Assertions.assertEquals(55, OffensiveSkill.LETHALITY.calcDmg(characterPair, enemyPair))
        Assertions.assertEquals(5, OffensiveSkill.SOL.calcDmg(characterPair, enemyPair))

        Assertions.assertEquals(32, characterPair.leadCharacter.remainingHealth)
    }

    @Test
    fun `critical and sol heal test`() {
        characterPair.leadCharacter.currentEquippedItem = 1
        characterPair.updateBattleStat()

        Assertions.assertEquals(75, OffensiveSkill.SOL.calcDmg(characterPair, enemyPair))

        Assertions.assertEquals(55, characterPair.leadCharacter.remainingHealth)
    }
}