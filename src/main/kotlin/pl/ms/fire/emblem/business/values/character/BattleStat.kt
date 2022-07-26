package pl.ms.fire.emblem.business.values.character

import pl.ms.fire.emblem.business.entities.CharacterPair
import pl.ms.fire.emblem.business.utlis.getStat
import pl.ms.fire.emblem.business.values.category.AttackCategory
import pl.ms.fire.emblem.business.values.category.WeaponCategory

class BattleStat(
    var attack: Int,
    var hitRate: Int,
    var avoid: Int,
    var critical: Int,
    var attackSpeed: Int
) {

    fun updateBattleStats(characterPair: CharacterPair) {

        val currentEquippedWeapon =
            characterPair.leadCharacter.equipment.getOrNull(characterPair.leadCharacter.currentEquippedItem)

        val stats = characterPair.boostedStats

        avoid = (stats.getStat(Stat.SPEED) * 3 + stats.getStat(Stat.LUCK)) / 2
        attackSpeed = stats.getStat(Stat.SPEED) - (currentEquippedWeapon?.weight ?: 0)

        if ((currentEquippedWeapon == null) || currentEquippedWeapon.weaponCategory == WeaponCategory.STAFF) {
            attack = 0
            hitRate = 0
            critical = 0
        }
        else {
            attack = currentEquippedWeapon.mt + if (currentEquippedWeapon.attackCategory == AttackCategory.PHYSICAL)
                stats.getStat(Stat.STRENGTH) else stats.getStat(Stat.MAGICK)
            hitRate = currentEquippedWeapon.hitPercent + ((stats.getStat(Stat.SKILL) * 3 + stats.getStat(Stat.LUCK)) / 2)
            critical = currentEquippedWeapon.criticalPercent + (stats.getStat(Stat.SKILL) / 2)
        }

    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BattleStat

        if (attack != other.attack) return false
        if (hitRate != other.hitRate) return false
        if (avoid != other.avoid) return false
        if (critical != other.critical) return false

        return true
    }

    override fun hashCode(): Int {
        var result = attack
        result = 31 * result + hitRate
        result = 31 * result + avoid
        result = 31 * result + critical
        return result
    }
}