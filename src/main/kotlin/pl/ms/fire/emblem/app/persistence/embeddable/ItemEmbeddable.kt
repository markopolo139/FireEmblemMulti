package pl.ms.fire.emblem.app.persistence.embeddable

import pl.ms.fire.emblem.business.values.category.AttackCategory
import pl.ms.fire.emblem.business.values.category.WeaponCategory
import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.EnumType
import javax.persistence.Enumerated

@Embeddable
class ItemEmbeddable(
    @Column(name = "name", nullable = false, insertable = true, updatable = true)
    val name: String,

    @Column(name = "mt", nullable = false, insertable = true, updatable = true)
    val mt: Int,

    @Column(name = "hit_percent", nullable = false, insertable = true, updatable = true)
    val hitPercent: Int,

    @Column(name = "critical_percent", nullable = false, insertable = true, updatable = true)
    val criticalPercent: Int,

    @Column(name = "range", nullable = false, insertable = true)
    val range: Int,

    @Column(name = "attack_category", nullable = false, insertable = true, updatable = true) @Enumerated(EnumType.STRING)
    val attackCategory: AttackCategory,

    @Column(name = "weapon_category", nullable = false, insertable = true, updatable = true) @Enumerated(EnumType.STRING)
    val weaponCategory: WeaponCategory,

    @Column(name = "weight", nullable = false, insertable = true, updatable = true)
    val weight: Int,

) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ItemEmbeddable

        if (name != other.name) return false
        if (mt != other.mt) return false
        if (hitPercent != other.hitPercent) return false
        if (criticalPercent != other.criticalPercent) return false
        if (range != other.range) return false
        if (attackCategory != other.attackCategory) return false
        if (weaponCategory != other.weaponCategory) return false
        if (weight != other.weight) return false

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
        result = 31 * result + weight
        return result
    }
}