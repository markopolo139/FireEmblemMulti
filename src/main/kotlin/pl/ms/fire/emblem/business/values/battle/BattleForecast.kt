package pl.ms.fire.emblem.business.values.battle

data class BattleForecast(
    val attackerDmg: Int,
    val attackerHit: Int,
    val attackerCrit: Int,
    val attackerDoubleAttack: Boolean,
    val defenderDmg: Int,
    val defenderHit: Int,
    val defenderCrit: Int,
    val defenderDoubleAttack: Boolean,
)