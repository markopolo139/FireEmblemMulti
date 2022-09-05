package pl.ms.fire.emblem.app.websocket.messages.board

import pl.ms.fire.emblem.business.values.board.Position

class WaitMessageModel(val position: Position): BoardMessageModel {
    override fun getDescription(): String = "End of turn (set true for character on $position"
}