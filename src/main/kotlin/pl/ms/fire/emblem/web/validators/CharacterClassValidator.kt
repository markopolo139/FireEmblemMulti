package pl.ms.fire.emblem.web.validators

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
@Constraint(validatedBy = [CharacterClassValidatorImpl::class])
annotation class CharacterClassValidator(
    val message: String = "Invalid character class",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)