package pl.ms.fire.emblem.web.model.response

import pl.ms.fire.emblem.web.model.request.WebGameCharacterModel

class CharacterPairResponse(
    val leadCharacter: WebGameCharacterModel,
    val supportCharacter: WebGameCharacterModel?
)