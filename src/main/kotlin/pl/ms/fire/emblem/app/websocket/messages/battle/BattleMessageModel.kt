package pl.ms.fire.emblem.app.websocket.messages.battle

import pl.ms.fire.emblem.app.entities.AppSpotEntity
import pl.ms.fire.emblem.app.websocket.messages.MessageModel
import pl.ms.fire.emblem.app.websocket.messages.models.SpotModel
import pl.ms.fire.emblem.business.utlis.Displayable

class BattleMessageModel(
    val attackerSpot: SpotModel, val defenderSpot: SpotModel, val battleCourse: List<String>
): MessageModel {
    override fun getDescription(): String = "Have all info about battle"
}