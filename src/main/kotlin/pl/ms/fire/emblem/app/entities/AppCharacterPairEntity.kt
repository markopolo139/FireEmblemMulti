package pl.ms.fire.emblem.app.entities

import pl.ms.fire.emblem.business.entities.CharacterPair

class AppCharacterPairEntity(
    val id: Int,
    leadCharacter: AppGameCharacterEntity,
    supportCharacter: AppGameCharacterEntity?,
    val spot: AppSpotEntity?
): CharacterPair(leadCharacter, supportCharacter)