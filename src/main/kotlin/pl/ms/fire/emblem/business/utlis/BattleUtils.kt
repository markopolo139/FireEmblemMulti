package pl.ms.fire.emblem.business.utlis

import pl.ms.fire.emblem.business.entities.CharacterPair
import pl.ms.fire.emblem.business.values.character.Stat

object BattleUtils {
    fun criticalCheck(percent: Int, enemyLuck: Int, dmgDealt: Int) =
        if (RandomSingleton.random.nextInt(1, 101) <= (percent - enemyLuck)) dmgDealt * 3 else dmgDealt

    fun hitCheck(hitPercent: Int, enemyAvo: Int) =
        RandomSingleton.random.nextInt(1, 101) <= (hitPercent - enemyAvo)

    fun skillCheck(percent: Int) = RandomSingleton.random.nextInt(1, 101) <= percent

    fun isDoubleAttack(attackerPair: CharacterPair, defenderPair: CharacterPair): Boolean =
        attackerPair.battleStat.attackSpeed - defenderPair.battleStat.attackSpeed >= 5

}