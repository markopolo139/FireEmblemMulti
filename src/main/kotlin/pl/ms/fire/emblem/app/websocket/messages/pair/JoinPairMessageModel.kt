package pl.ms.fire.emblem.app.websocket.messages.pair

import pl.ms.fire.emblem.app.websocket.messages.models.SpotModel

class JoinPairMessageModel(val pairSpot: SpotModel, val emptySpot: SpotModel): CharacterManagementMessageModel