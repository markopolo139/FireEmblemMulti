package pl.ms.fire.emblem.app.exceptions

class PresetLimitExceededException(limit: Int): AppException("Preset limit exceeded (limit $limit)")