package pl.ms.fire.emblem.app.websocket.messages.pair

import pl.ms.fire.emblem.app.websocket.messages.models.SpotModel

class TradeSupportMessageModel(pairSpot1: SpotModel, pairSpot2: SpotModel): CharacterManagementMessageModel {
    override fun getDescription(): String = "Contains result of trading support characters"
}