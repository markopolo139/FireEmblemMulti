package pl.ms.fire.emblem.app.websocket.messages.board

import pl.ms.fire.emblem.business.values.board.Position

class StartTurnMessageModel(positions: List<Position>): BoardMessageModel {
    override fun getDescription(): String = "Start of new turn (set every character moved to false)"
}