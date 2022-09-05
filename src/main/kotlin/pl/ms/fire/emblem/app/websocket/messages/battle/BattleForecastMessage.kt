package pl.ms.fire.emblem.app.websocket.messages.battle

import pl.ms.fire.emblem.app.entities.AppSpotEntity
import pl.ms.fire.emblem.app.websocket.messages.MessageModel
import pl.ms.fire.emblem.app.websocket.messages.models.SpotModel
import pl.ms.fire.emblem.business.values.battle.BattleForecast
import pl.ms.fire.emblem.business.values.board.Spot

class BattleForecastMessage(
    attackerSpot: SpotModel, defenderSpot: SpotModel, battleForecast: BattleForecast
): MessageModel {
    override fun getDescription(): String = "Have details about battle forecast"
}