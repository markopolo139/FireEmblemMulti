package pl.ms.fire.emblem.app.websocket.messages.models

import pl.ms.fire.emblem.app.entities.AppPresetEntity
import pl.ms.fire.emblem.business.values.character.CharacterClass
import pl.ms.fire.emblem.business.values.character.Stat
import pl.ms.fire.emblem.business.values.items.Item

class GameCharacterModel(
    val id: Int,
    val presetId: Int,
    val name: String,
    val remainingHp: Int,
    val currentEquippedItem: Int,
    val characterClass: CharacterClass,
    val moved: Boolean,
    val stats: Map<Stat, Int>,
    val equipment: MutableList<Item>
)