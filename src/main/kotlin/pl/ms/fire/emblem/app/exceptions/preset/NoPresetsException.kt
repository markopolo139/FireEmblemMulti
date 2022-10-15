package pl.ms.fire.emblem.app.exceptions.preset

import pl.ms.fire.emblem.app.exceptions.AppException

class NoPresetsException: AppException("Player does not have any presets")