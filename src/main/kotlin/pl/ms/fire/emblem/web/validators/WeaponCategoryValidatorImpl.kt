package pl.ms.fire.emblem.web.validators

import pl.ms.fire.emblem.business.values.category.WeaponCategory
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class WeaponCategoryValidatorImpl: ConstraintValidator<WeaponCategoryValidator, String> {
    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        return try {
            WeaponCategory.valueOf(value ?: "")
            true
        }
        catch (e: Exception) {
            false
        }
    }
}