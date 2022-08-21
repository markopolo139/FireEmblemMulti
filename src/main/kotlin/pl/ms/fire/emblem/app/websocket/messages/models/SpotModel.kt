package pl.ms.fire.emblem.app.websocket.messages.models

import pl.ms.fire.emblem.business.values.board.Position
import pl.ms.fire.emblem.business.values.board.Terrain


class SpotModel(
    val position: Position,
    val terrain: Terrain,
    val standingCharacter: CharacterPairModel?,
)