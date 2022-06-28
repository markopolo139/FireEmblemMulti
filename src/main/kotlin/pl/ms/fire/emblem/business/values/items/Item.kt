package pl.ms.fire.emblem.business.values.items

import pl.ms.fire.emblem.business.values.category.AttackCategory
import pl.ms.fire.emblem.business.values.category.WeaponCategory

data class Item(
    val name: String,
    val mt: Int,
    val hitPercent: Int,
    val criticalPercent: Int,
    val range: Int,
    val attackCategory: AttackCategory,
    val weaponCategory: WeaponCategory
)