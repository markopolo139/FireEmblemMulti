package pl.ms.fire.emblem.app.websocket.messages.board

import pl.ms.fire.emblem.app.websocket.messages.models.SpotModel

class StaffHealMessageModel(val healerSpot: SpotModel, val healedSpot: SpotModel): BoardMessageModel