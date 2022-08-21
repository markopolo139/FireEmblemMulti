package pl.ms.fire.emblem.web.validators

import pl.ms.fire.emblem.business.values.character.Stat
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class StatValidatorImpl: ConstraintValidator<StatValidator, String> {
    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        return try {
            Stat.valueOf(value ?: "")
            true
        }
        catch (e: Exception) {
            false
        }
    }
}