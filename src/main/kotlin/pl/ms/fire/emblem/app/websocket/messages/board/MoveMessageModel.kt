package pl.ms.fire.emblem.app.websocket.messages.board

import pl.ms.fire.emblem.app.websocket.messages.models.SpotModel

class MoveMessageModel(val startSpot: SpotModel,val lastSpot: SpotModel): BoardMessageModel