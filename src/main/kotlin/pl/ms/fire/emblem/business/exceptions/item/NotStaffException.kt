package pl.ms.fire.emblem.business.exceptions.item

import pl.ms.fire.emblem.business.exceptions.BusinessException

class NotStaffException: BusinessException("Selected item is not a staff")
