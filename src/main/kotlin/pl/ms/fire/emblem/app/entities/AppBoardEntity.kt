package pl.ms.fire.emblem.app.entities

import pl.ms.fire.emblem.app.configuration.security.UserEntity
import pl.ms.fire.emblem.business.entities.GameBoard
import pl.ms.fire.emblem.business.values.board.Position
import pl.ms.fire.emblem.business.values.board.Spot

class AppBoardEntity(
    val id: Int,
    height: Int,
    width: Int,
    val playerA: UserEntity,
    val playerB: UserEntity?,
    var currentPlayer: UserEntity?,
    spots: MutableMap<Position, Spot>
): GameBoard(spots, height, width)