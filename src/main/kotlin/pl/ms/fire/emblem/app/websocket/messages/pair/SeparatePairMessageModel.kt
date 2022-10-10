package pl.ms.fire.emblem.app.websocket.messages.pair

import pl.ms.fire.emblem.app.websocket.messages.models.SpotModel

class SeparatePairMessageModel(val separatePair: SpotModel, val newPair: SpotModel): CharacterManagementMessageModel {
    override fun getDescription(): String = "Contains result of separating pair"
}