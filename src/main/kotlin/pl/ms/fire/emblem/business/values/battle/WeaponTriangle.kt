package pl.ms.fire.emblem.business.values.battle

enum class WeaponTriangle(
    val hitRateModifier: Int,
    val dmgDealtModifier: Int,
) {
    ADVANTAGE(15, 3),
    NORMAL(0, 0),
    DISADVANTAGE(-15, -3)
}