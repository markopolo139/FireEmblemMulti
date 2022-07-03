package pl.ms.fire.emblem.business.values

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import pl.ms.fire.emblem.business.values.character.DefensiveSkill

class DefensiveSkillsTest {

    @Test
    fun `damage modifiers test`() {
        Assertions.assertEquals(54, DefensiveSkill.MIRACLE.dmgModifier(75, 55))
        Assertions.assertEquals(50, DefensiveSkill.MIRACLE.dmgModifier(50, 55))
        Assertions.assertEquals(10, DefensiveSkill.AEGIS.dmgModifier(20, 55))
    }
}