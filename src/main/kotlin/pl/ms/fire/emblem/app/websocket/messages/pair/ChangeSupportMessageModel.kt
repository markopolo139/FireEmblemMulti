package pl.ms.fire.emblem.app.websocket.messages.pair

import pl.ms.fire.emblem.app.websocket.messages.models.SpotModel

class ChangeSupportMessageModel(val spot: SpotModel): CharacterManagementMessageModel {
    override fun getDescription(): String = "Contains result of changing support character"
}