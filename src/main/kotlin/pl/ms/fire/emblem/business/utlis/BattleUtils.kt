package pl.ms.fire.emblem.business.utlis

object BattleUtils {
    fun criticalCheck(percent: Int, enemyLuck: Int, dmgDealt: Int) =
        if (RandomSingleton.random.nextInt(1, 101) <= (percent - enemyLuck)) dmgDealt * 3 else dmgDealt
}