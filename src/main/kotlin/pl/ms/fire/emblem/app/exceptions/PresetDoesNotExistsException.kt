package pl.ms.fire.emblem.app.exceptions

class PresetDoesNotExistsException(id: Int): AppException("Preset of $id id for current user does not exists")