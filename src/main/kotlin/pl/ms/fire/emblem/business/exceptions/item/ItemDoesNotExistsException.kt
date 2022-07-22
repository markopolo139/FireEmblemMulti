package pl.ms.fire.emblem.business.exceptions.item

import pl.ms.fire.emblem.business.exceptions.BusinessException

class ItemDoesNotExistsException: BusinessException("Selected item does not exists")