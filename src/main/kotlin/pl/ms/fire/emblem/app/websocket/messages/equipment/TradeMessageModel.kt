package pl.ms.fire.emblem.app.websocket.messages.equipment

import pl.ms.fire.emblem.app.websocket.messages.models.SpotModel

class TradeMessageModel(characterSpot: SpotModel, tradeWithSpot: SpotModel): EquipmentMessageModel {
    override fun getDescription(): String = "Contains result of trading equipment"
}