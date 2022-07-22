package pl.ms.fire.emblem.business.exceptions.character

import pl.ms.fire.emblem.business.exceptions.BusinessException

class PairAlreadyHaveSupportException: BusinessException("Pair can't join with another unit, support already exists")