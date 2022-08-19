package pl.ms.fire.emblem.app.websocket.messages.models

import pl.ms.fire.emblem.app.entities.AppGameCharacterEntity

fun AppGameCharacterEntity.toModel(): GameCharacterModel = GameCharacterModel(
    id, name, remainingHealth, currentEquippedItem, characterClass, moved, stats, equipment
)
