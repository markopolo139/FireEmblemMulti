package pl.ms.fire.emblem.business.exceptions

import pl.ms.fire.emblem.business.values.category.WeaponCategory

class NotAllowedWeaponCategoryException(
    category: WeaponCategory
) : BusinessException("Selected character can't use $category")