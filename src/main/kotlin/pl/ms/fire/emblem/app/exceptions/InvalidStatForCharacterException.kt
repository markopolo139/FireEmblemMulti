package pl.ms.fire.emblem.app.exceptions

class InvalidStatForCharacterException(statLimit: Int): AppException("Invalid stats for character (limit $statLimit")