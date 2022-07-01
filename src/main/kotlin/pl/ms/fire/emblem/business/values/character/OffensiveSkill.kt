package pl.ms.fire.emblem.business.values.character

import pl.ms.fire.emblem.business.entities.CharacterPair
import pl.ms.fire.emblem.business.serices.battle.BattleCalculator
import pl.ms.fire.emblem.business.utlis.BattleUtils
import pl.ms.fire.emblem.business.utlis.getStat
import pl.ms.fire.emblem.business.values.category.AttackCategory

enum class OffensiveSkill(
    override val statUsedToCalculation: Stat,
    override val priority: Int,
    override val percentCalc: (Int) -> Int
): Skill, BattleCalculator {
    AETHER(Stat.SKILL, 5, {stat -> stat/2}) {
        override fun calcDmg(playerPair: CharacterPair, enemyPair: CharacterPair): Int =
            SOL.calcDmg(playerPair, enemyPair) + LUNA.calcDmg(playerPair, enemyPair)
    },
    IGNIS(Stat.SKILL, 5, {stat -> stat}) {
        override fun calcDmg(playerPair: CharacterPair, enemyPair: CharacterPair): Int {

            val playerEquippedItem = playerPair.leadCharacter.equipment[playerPair.leadCharacter.currentEquippedItem]
            var opponentDefense = 0
            var playerAttack = playerPair.battleStat.attack

            if (playerEquippedItem.attackCategory == AttackCategory.MAGICAL) {
                opponentDefense = enemyPair.boostedStats.getStat(Stat.RESISTANCE)
                playerAttack += playerPair.boostedStats.getStat(Stat.STRENGTH) / 2
            }
            else {
                opponentDefense = enemyPair.boostedStats.getStat(Stat.DEFENSE)
                playerAttack += playerPair.boostedStats.getStat(Stat.MAGICK) / 2
            }

            return BattleUtils.criticalCheck(
                playerPair.battleStat.critical, enemyPair.boostedStats.getStat(Stat.LUCK),playerAttack - opponentDefense
            )

        }
    },
    LUNA(Stat.SKILL, 3, {stat -> stat}) {
        override fun calcDmg(playerPair: CharacterPair, enemyPair: CharacterPair): Int {

            val playerEquippedItem = playerPair.leadCharacter.equipment[playerPair.leadCharacter.currentEquippedItem]
            val playerAttack = playerPair.battleStat.attack
            val opponentDefense = if (playerEquippedItem.attackCategory == AttackCategory.MAGICAL) {
                enemyPair.boostedStats.getStat(Stat.RESISTANCE) / 2

            } else {
                enemyPair.boostedStats.getStat(Stat.DEFENSE) / 2
            }

            return BattleUtils.criticalCheck(
                playerPair.battleStat.critical, enemyPair.boostedStats.getStat(Stat.LUCK),playerAttack - opponentDefense
            )

        }
    },
    ASTRA(Stat.SKILL, 4, {stat -> stat/2}) {
        override fun calcDmg(playerPair: CharacterPair, enemyPair: CharacterPair): Int {

            val playerEquippedItem = playerPair.leadCharacter.equipment[playerPair.leadCharacter.currentEquippedItem]
            val playerAttack = playerPair.battleStat.attack
            val opponentDefense = if (playerEquippedItem.attackCategory == AttackCategory.MAGICAL) {
                enemyPair.boostedStats.getStat(Stat.RESISTANCE)

            } else {
                enemyPair.boostedStats.getStat(Stat.DEFENSE)
            }

            var dmg = 0
            for (x in 1..5)
                dmg += BattleUtils.criticalCheck(
                    playerPair.battleStat.critical, enemyPair.boostedStats.getStat(Stat.LUCK),
                    (playerAttack - opponentDefense) / 2
                )

            return dmg

        }
    },
    SOL(Stat.SKILL, 3, {stat -> stat}) {
        override fun calcDmg(playerPair: CharacterPair, enemyPair: CharacterPair): Int {

            val playerEquippedItem = playerPair.leadCharacter.equipment[playerPair.leadCharacter.currentEquippedItem]
            val playerAttack = playerPair.battleStat.attack
            val opponentDefense = if (playerEquippedItem.attackCategory == AttackCategory.MAGICAL) {
                enemyPair.boostedStats.getStat(Stat.RESISTANCE)

            } else {
                enemyPair.boostedStats.getStat(Stat.DEFENSE)
            }

            val dmg = BattleUtils.criticalCheck(
                playerPair.battleStat.critical, enemyPair.boostedStats.getStat(Stat.LUCK),playerAttack - opponentDefense
            )

            val heal = dmg / 2

            if ((playerPair.leadCharacter.remainingHealth + heal) > playerPair.boostedStats.getStat(Stat.HEALTH))
                playerPair.leadCharacter.remainingHealth = playerPair.boostedStats.getStat(Stat.HEALTH)
            else
                playerPair.leadCharacter.remainingHealth += heal

            return dmg

        }
    },
    LETHALITY(Stat.SKILL, 5, {stat -> stat/4}) {
        override fun calcDmg(playerPair: CharacterPair, enemyPair: CharacterPair): Int =
            enemyPair.leadCharacter.remainingHealth
    },
    VENGEANCE(Stat.SKILL, 5, {stat -> stat * 2}) {
        override fun calcDmg(playerPair: CharacterPair, enemyPair: CharacterPair): Int {

            val playerEquippedItem = playerPair.leadCharacter.equipment[playerPair.leadCharacter.currentEquippedItem]
            var playerAttack = playerPair.battleStat.attack
            val opponentDefense = if (playerEquippedItem.attackCategory == AttackCategory.MAGICAL) {
                enemyPair.boostedStats.getStat(Stat.RESISTANCE)

            } else {
                enemyPair.boostedStats.getStat(Stat.DEFENSE)
            }

            playerAttack += (playerPair.boostedStats.getStat(Stat.HEALTH) - playerPair.leadCharacter.remainingHealth) / 2

            return BattleUtils.criticalCheck(
                playerPair.battleStat.critical, enemyPair.boostedStats.getStat(Stat.LUCK),playerAttack - opponentDefense
            )

        }
    },;

    override val displayName: String
        get() = name
}