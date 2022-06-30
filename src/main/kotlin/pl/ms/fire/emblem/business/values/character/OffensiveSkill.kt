package pl.ms.fire.emblem.business.values.character

import pl.ms.fire.emblem.business.entities.CharacterPair
import pl.ms.fire.emblem.business.serices.battle.BattleCalculator

enum class OffensiveSkill(
    override val statUsedToCalculation: Stat,
    override val priority: Int,
    override val percentCalc: (Int) -> Int
): Skill, BattleCalculator {
    AETHER(Stat.SKILL, 5, {stat -> stat/2}) {
        override fun calcDmg(playerPair: CharacterPair, enemyPair: CharacterPair): Int {
            TODO("Not yet implemented")
        }
    },
    IGNIS(Stat.SKILL, 5, {stat -> stat}) {
        override fun calcDmg(playerPair: CharacterPair, enemyPair: CharacterPair): Int {
            TODO("Not yet implemented")
        }
    },
    LUNA(Stat.SKILL, 3, {stat -> stat}) {
        override fun calcDmg(playerPair: CharacterPair, enemyPair: CharacterPair): Int {
            TODO("Not yet implemented")
        }
    },
    ASTRA(Stat.SKILL, 4, {stat -> stat/2}) {
        override fun calcDmg(playerPair: CharacterPair, enemyPair: CharacterPair): Int {
            TODO("Not yet implemented")
        }
    },
    SOL(Stat.SKILL, 3, {stat -> stat}) {
        override fun calcDmg(playerPair: CharacterPair, enemyPair: CharacterPair): Int {
            TODO("Not yet implemented")
        }
    },
    LETHALITY(Stat.SKILL, 5, {stat -> stat/4}) {
        override fun calcDmg(playerPair: CharacterPair, enemyPair: CharacterPair): Int {
            TODO("Not yet implemented")
        }
    },
    VENGEANCE(Stat.SKILL, 5, {stat -> stat * 2}) {
        override fun calcDmg(playerPair: CharacterPair, enemyPair: CharacterPair): Int {
            TODO("Not yet implemented")
        }
    },;

    override val displayName: String
        get() = name
}