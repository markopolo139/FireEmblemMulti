package pl.ms.fire.emblem.app.websocket.messages.models

class CharacterPairModel(
    val leadCharacter: GameCharacterModel,
    val supportCharacter: GameCharacterModel?,
)