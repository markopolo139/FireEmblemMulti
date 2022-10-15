package pl.ms.fire.emblem.app.exceptions.token

import pl.ms.fire.emblem.app.exceptions.AppException

class InvalidWebSocketTokenException: AppException("Given invalid token to websocket")