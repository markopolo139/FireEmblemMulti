package pl.ms.fire.emblem.app.exceptions.preset

import pl.ms.fire.emblem.app.exceptions.AppException

class PresetLimitExceededException(limit: Int): AppException("Preset limit exceeded (limit $limit)")