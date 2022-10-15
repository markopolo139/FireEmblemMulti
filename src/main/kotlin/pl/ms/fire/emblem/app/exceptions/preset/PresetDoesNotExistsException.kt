package pl.ms.fire.emblem.app.exceptions.preset

import pl.ms.fire.emblem.app.exceptions.AppException

class PresetDoesNotExistsException(id: Int): AppException("Preset of $id id for current user does not exists")