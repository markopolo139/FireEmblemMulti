package pl.ms.fire.emblem.web.validators

import pl.ms.fire.emblem.business.values.category.AttackCategory
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class AttackCategoryValidatorImpl: ConstraintValidator<AttackCategoryValidator, String> {
    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        return try {
            AttackCategory.valueOf(value ?: "")
            true
        } catch (e: Exception) {
            false
        }
    }
}