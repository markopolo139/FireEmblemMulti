package pl.ms.fire.emblem.app.exceptions.board

import pl.ms.fire.emblem.app.exceptions.AppException

class NotCurrentTurnException: AppException("Not current player")