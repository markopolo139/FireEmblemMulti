package pl.ms.fire.emblem.app.entities

import pl.ms.fire.emblem.app.persistence.entities.PlayerPresetEntity
import pl.ms.fire.emblem.business.values.GameCharacter
import pl.ms.fire.emblem.business.values.character.CharacterClass
import pl.ms.fire.emblem.business.values.character.Stat
import pl.ms.fire.emblem.business.values.items.Item

class AppGameCharacterEntity(
    val id: Int,
    val preset: AppPresetEntity?,
    name: String,
    remainingHp: Int,
    currentEquippedItem: Int,
    characterClass: CharacterClass,
    moved: Boolean,
    stats: Map<Stat, Int>,
    equipment: MutableList<Item>
): GameCharacter(name, remainingHp, currentEquippedItem, equipment, stats, characterClass, moved)