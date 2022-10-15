package pl.ms.fire.emblem.app.entities

import pl.ms.fire.emblem.business.entities.CharacterPair

class AppCharacterPairEntity(
    val id: Int,
    leadCharacter: AppGameCharacterEntity,
    supportCharacter: AppGameCharacterEntity?,
    var spot: AppSpotEntity?
): CharacterPair(leadCharacter, supportCharacter) {

    fun deepCopy(): AppCharacterPairEntity =
        AppCharacterPairEntity(
            id, leadCharacter as AppGameCharacterEntity, supportCharacter as AppGameCharacterEntity?, spot
        )
}