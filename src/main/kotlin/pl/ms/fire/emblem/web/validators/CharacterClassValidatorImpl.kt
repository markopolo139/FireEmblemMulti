package pl.ms.fire.emblem.web.validators

import pl.ms.fire.emblem.business.values.character.CharacterClass
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class CharacterClassValidatorImpl: ConstraintValidator<CharacterClassValidator, String> {
    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        return try {
            CharacterClass.valueOf(value ?: "")
            true
        }
        catch(e: Exception) {
            false
        }
    }
}