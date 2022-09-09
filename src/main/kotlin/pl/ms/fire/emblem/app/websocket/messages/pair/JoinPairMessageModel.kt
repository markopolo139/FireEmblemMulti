package pl.ms.fire.emblem.app.websocket.messages.pair

import pl.ms.fire.emblem.app.websocket.messages.models.SpotModel

class JoinPairMessageModel(pairSpot: SpotModel, emptySpot: SpotModel): CharacterManagementMessageModel {
    override fun getDescription(): String = "Contains result of joining into pair"
}