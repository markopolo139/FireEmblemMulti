package pl.ms.fire.emblem.app.exceptions.board

import pl.ms.fire.emblem.app.exceptions.AppException

class SpotDoesNotExistsException: AppException("Given position does not lead to spot")