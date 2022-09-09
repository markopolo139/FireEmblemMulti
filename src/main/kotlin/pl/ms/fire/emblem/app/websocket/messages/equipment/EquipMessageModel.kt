package pl.ms.fire.emblem.app.websocket.messages.equipment

import pl.ms.fire.emblem.app.websocket.messages.models.SpotModel

class EquipMessageModel(characterSpot: SpotModel): EquipmentMessageModel {
    override fun getDescription(): String = "Contains result of equipping new weapon"
}