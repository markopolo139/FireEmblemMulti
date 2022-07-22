package pl.ms.fire.emblem.business.exceptions.item

import pl.ms.fire.emblem.business.exceptions.BusinessException

class EquipmentLimitExceededException: BusinessException("Exceeded limit in equipment")