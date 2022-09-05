package pl.ms.fire.emblem.app.websocket.messages.board

import pl.ms.fire.emblem.app.websocket.messages.models.SpotModel

class StaffHealMessageModel(healerSpot: SpotModel, healedSpot: SpotModel): BoardMessageModel {
    override fun getDescription(): String = "Results of healing with staff"
}