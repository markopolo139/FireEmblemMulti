package pl.ms.fire.emblem.business.serices.battle

import pl.ms.fire.emblem.business.entities.CharacterPair
import pl.ms.fire.emblem.business.utlis.BattleUtils
import pl.ms.fire.emblem.business.utlis.Displayable
import pl.ms.fire.emblem.business.utlis.getStat
import pl.ms.fire.emblem.business.values.category.AttackCategory
import pl.ms.fire.emblem.business.values.character.Stat

class DefaultBattleCalculator: BattleCalculator, Displayable {
    override fun calcDmg(playerPair: CharacterPair, enemyPair: CharacterPair): Int {

        val playerEquippedItem = playerPair.leadCharacter.equipment[playerPair.leadCharacter.currentEquippedItem]
        val playerAttack = playerPair.battleStat.attack
        val opponentDefense = if (playerEquippedItem.attackCategory == AttackCategory.MAGICAL) {
            enemyPair.boostedStats.getStat(Stat.RESISTANCE)
        } else {
            enemyPair.boostedStats.getStat(Stat.DEFENSE)
        }

        return BattleUtils.criticalCheck(
            playerPair.battleStat.critical, enemyPair.boostedStats.getStat(Stat.LUCK),playerAttack - opponentDefense
        )

    }

    override val displayName: String
        get() = "Default battle"
}