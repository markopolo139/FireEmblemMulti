package pl.ms.fire.emblem.app.entities

import pl.ms.fire.emblem.app.configuration.security.UserEntity

class AppPresetEntity(
    val id: Int,
    val player: UserEntity,
    val gameCharacterEntity: AppGameCharacterEntity
)