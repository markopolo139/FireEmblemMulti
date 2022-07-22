package pl.ms.fire.emblem.business.exceptions.character

import pl.ms.fire.emblem.business.exceptions.BusinessException

class NoSupportCharacterException: BusinessException("Pair does not have support character")