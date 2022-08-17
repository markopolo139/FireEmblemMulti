package pl.ms.fire.emblem.app.entities

import pl.ms.fire.emblem.business.values.board.Position
import pl.ms.fire.emblem.business.values.board.Spot
import pl.ms.fire.emblem.business.values.board.Terrain

class AppSpotEntity(
    val id: Int,
    var board: AppBoardEntity?,
    position: Position,
    terrain: Terrain,
    standingCharacter: AppCharacterPairEntity?
): Spot(position, terrain, standingCharacter)