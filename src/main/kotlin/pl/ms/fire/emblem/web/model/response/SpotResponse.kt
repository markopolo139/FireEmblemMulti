package pl.ms.fire.emblem.web.model.response

import pl.ms.fire.emblem.app.websocket.messages.models.CharacterPairModel
import pl.ms.fire.emblem.business.values.board.Position
import pl.ms.fire.emblem.business.values.board.Terrain
import pl.ms.fire.emblem.web.model.request.PositionModel

class SpotResponse(
    val position: PositionModel,
    val terrain: Terrain,
    val standingCharacter: CharacterPairResponse?,
)