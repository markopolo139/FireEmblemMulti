package pl.ms.fire.emblem.business.values

import pl.ms.fire.emblem.business.values.character.CharacterClass
import pl.ms.fire.emblem.business.values.character.Skill
import pl.ms.fire.emblem.business.values.character.Stat
import pl.ms.fire.emblem.business.values.items.Item

class GameCharacter(
    val name: String,
    var remainingHealth: Int,
    var currentEquippedItem: Int,
    val equipment: List<Item>,
    val stats: Map<Stat, Int>,
    val skills: List<Skill>,
    val characterClass: CharacterClass,
    val moved: Boolean
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GameCharacter

        if (name != other.name) return false
        if (remainingHealth != other.remainingHealth) return false
        if (currentEquippedItem != other.currentEquippedItem) return false
        if (equipment != other.equipment) return false
        if (stats != other.stats) return false
        if (skills != other.skills) return false
        if (characterClass != other.characterClass) return false
        if (moved != other.moved) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + remainingHealth
        result = 31 * result + currentEquippedItem
        result = 31 * result + equipment.hashCode()
        result = 31 * result + stats.hashCode()
        result = 31 * result + skills.hashCode()
        result = 31 * result + characterClass.hashCode()
        result = 31 * result + moved.hashCode()
        return result
    }
}