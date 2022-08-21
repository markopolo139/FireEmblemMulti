package pl.ms.fire.emblem.web.validators

import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class CharacterClassValidatorImpl: ConstraintValidator<CharacterClassValidator, String> {
    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        return try {
            CharacterClass.valueOf(valie ?: "")
            true
        }
        catch(e: Exception) {
            false
        }
    }
}