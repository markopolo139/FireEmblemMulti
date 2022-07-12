package pl.ms.fire.emblem.business.values.items

import pl.ms.fire.emblem.business.values.category.AttackCategory
import pl.ms.fire.emblem.business.values.category.WeaponCategory

open class Item(
    val name: String,
    val mt: Int,
    val hitPercent: Int,
    val criticalPercent: Int,
    val range: Int,
    val attackCategory: AttackCategory,
    val weaponCategory: WeaponCategory
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Item

        if (name != other.name) return false
        if (mt != other.mt) return false
        if (hitPercent != other.hitPercent) return false
        if (criticalPercent != other.criticalPercent) return false
        if (range != other.range) return false
        if (attackCategory != other.attackCategory) return false
        if (weaponCategory != other.weaponCategory) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + mt
        result = 31 * result + hitPercent
        result = 31 * result + criticalPercent
        result = 31 * result + range
        result = 31 * result + attackCategory.hashCode()
        result = 31 * result + weaponCategory.hashCode()
        return result
    }
}