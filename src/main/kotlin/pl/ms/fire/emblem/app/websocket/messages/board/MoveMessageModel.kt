package pl.ms.fire.emblem.app.websocket.messages.board

import pl.ms.fire.emblem.app.websocket.messages.models.SpotModel

class MoveMessageModel(startSpot: SpotModel, lastSpot: SpotModel): BoardMessageModel {
    override fun getDescription(): String = "Result of moving sequence"
}