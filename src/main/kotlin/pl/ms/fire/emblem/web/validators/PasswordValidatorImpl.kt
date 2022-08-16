package pl.ms.fire.emblem.web.validators

import org.passay.*
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class PasswordValidatorImpl: ConstraintValidator<PasswordValidatorAnnotation, String> {
    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        val passwordValidator = PasswordValidator(
            listOf(
                LengthRule(5, Int.MAX_VALUE),
                WhitespaceRule(),
                UsernameRule(true,true),
                CharacterRule(EnglishCharacterData.UpperCase, 1),
                CharacterRule(EnglishCharacterData.Digit, 1),
                CharacterRule(EnglishCharacterData.Special, 1)
            )
        )

        val ruleResult = passwordValidator.validate(PasswordData(value ?: ""))

        return ruleResult.isValid

    }
}