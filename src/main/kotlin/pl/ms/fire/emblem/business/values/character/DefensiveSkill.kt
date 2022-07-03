package pl.ms.fire.emblem.business.values.character

import pl.ms.fire.emblem.business.entities.CharacterPair

enum class DefensiveSkill(
    override val statUsedToCalculation: Stat,
    override val priority: Int,
    override val percentCalc: (Int) -> Int
): Skill {
    AEGIS(Stat.SKILL, 5, {stat -> stat}) {
        override fun dmgModifier(dmgDealt: Int, hpOfDefensiveUnit: Int): Int = dmgDealt/2
    },
    MIRACLE(Stat.LUCK, 5, {stat -> stat}) {
        override fun dmgModifier(dmgDealt: Int, hpOfDefensiveUnit: Int): Int =
            if (dmgDealt >= hpOfDefensiveUnit) hpOfDefensiveUnit - 1 else dmgDealt
    };

    abstract fun dmgModifier(dmgDealt: Int, hpOfDefensiveUnit: Int): Int

    override val displayName: String
        get() = name
}