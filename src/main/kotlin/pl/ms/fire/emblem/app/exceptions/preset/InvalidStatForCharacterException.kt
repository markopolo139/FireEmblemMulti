package pl.ms.fire.emblem.app.exceptions.preset

import pl.ms.fire.emblem.app.exceptions.AppException

class InvalidStatForCharacterException(statLimit: Int): AppException("Invalid stats for character (limit $statLimit")