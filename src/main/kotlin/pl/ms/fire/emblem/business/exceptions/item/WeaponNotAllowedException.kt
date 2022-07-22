package pl.ms.fire.emblem.business.exceptions.item

import pl.ms.fire.emblem.business.exceptions.BusinessException

class WeaponNotAllowedException: BusinessException("Weapon can't be used")