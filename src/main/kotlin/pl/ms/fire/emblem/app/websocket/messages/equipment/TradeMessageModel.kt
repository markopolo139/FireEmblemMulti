package pl.ms.fire.emblem.app.websocket.messages.equipment

import pl.ms.fire.emblem.app.websocket.messages.models.SpotModel

class TradeMessageModel(val characterSpot: SpotModel, val tradeWithSpot: SpotModel): EquipmentMessageModel