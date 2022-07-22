package pl.ms.fire.emblem.business.exceptions.battle

import pl.ms.fire.emblem.business.exceptions.BusinessException
import pl.ms.fire.emblem.business.values.category.WeaponCategory

class NotAllowedWeaponCategoryException(
    category: WeaponCategory
) : BusinessException("Selected character can't use $category")