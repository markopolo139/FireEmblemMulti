package pl.ms.fire.emblem.app.websocket.messages.models

import pl.ms.fire.emblem.app.entities.AppCharacterPairEntity
import pl.ms.fire.emblem.app.entities.AppGameCharacterEntity
import pl.ms.fire.emblem.app.entities.AppSpotEntity

fun AppGameCharacterEntity.toModel(): GameCharacterModel = GameCharacterModel(
    id, preset?.id!!, name, remainingHealth, currentEquippedItem, characterClass, moved, stats, equipment
)

fun AppCharacterPairEntity.toModel(): CharacterPairModel =
    CharacterPairModel(
        (leadCharacter as AppGameCharacterEntity).toModel(), (supportCharacter as AppGameCharacterEntity).toModel()
    )

fun AppSpotEntity.toModel(): SpotModel =
    SpotModel(position, terrain, (standingCharacter as? AppCharacterPairEntity)?.toModel())