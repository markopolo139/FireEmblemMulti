package pl.ms.fire.emblem.app.exceptions.board

import pl.ms.fire.emblem.app.exceptions.AppException

class NotPlayersPairException: AppException("Selected pair that not belong to player")