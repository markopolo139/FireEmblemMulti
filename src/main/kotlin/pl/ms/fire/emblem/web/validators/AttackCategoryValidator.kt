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
@Constraint(validatedBy = [AttackCategoryValidatorImpl::class])
annotation class AttackCategoryValidator(
    val message: String = "Invalid attack category",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
