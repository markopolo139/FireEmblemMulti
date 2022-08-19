package pl.ms.fire.emblem.web.model

import org.hibernate.validator.constraints.Length
import pl.ms.fire.emblem.web.validators.StatValidator
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

class StatModel(
    @field:NotBlank @field:StatValidator val stat: String,
    @field:NotNull @field:Min(0) val value: Int
)