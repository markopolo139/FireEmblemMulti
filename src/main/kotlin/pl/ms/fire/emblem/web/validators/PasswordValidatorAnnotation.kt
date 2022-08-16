package pl.ms.fire.emblem.web.validators

import pl.ms.fire.emblem.web.validators.PasswordValidatorImpl
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@Target(
    AnnotationTarget.FIELD,
    AnnotationTarget.TYPE_PARAMETER,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.TYPE,
    AnnotationTarget.CLASS,
    AnnotationTarget.ANNOTATION_CLASS,
    AnnotationTarget.CONSTRUCTOR
)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [PasswordValidatorImpl::class])
annotation class PasswordValidatorAnnotation(
    val message: String = "Invalid password (minLength - 5, no whitespaces," +
            " username can't be used as password, 1 uppercase, digit and special character)",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
