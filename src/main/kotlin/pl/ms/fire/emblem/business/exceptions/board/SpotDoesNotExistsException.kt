package pl.ms.fire.emblem.business.exceptions.board

import pl.ms.fire.emblem.business.exceptions.BusinessException

class SpotDoesNotExistsException: BusinessException("Spot with given position does not exists")