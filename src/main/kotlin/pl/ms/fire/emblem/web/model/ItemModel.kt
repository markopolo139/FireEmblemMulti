package pl.ms.fire.emblem.web.model

import org.hibernate.validator.constraints.Length
import pl.ms.fire.emblem.business.values.category.AttackCategory
import pl.ms.fire.emblem.business.values.category.WeaponCategory
import pl.ms.fire.emblem.web.validators.AttackCategoryValidator
import pl.ms.fire.emblem.web.validators.StatValidator
import pl.ms.fire.emblem.web.validators.WeaponCategoryValidator
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

class ItemModel(
    @field:NotBlank val name: String,
    @field:NotNull @field:Min(0) val mt: Int,
    @field:NotNull @field:Min(0) val hitPercent: Int,
    @field:NotNull @field:Min(0) val criticalPercent: Int,
    @field:NotNull @field:Min(1) @field:Max(3) val range: Int,
    @field:NotBlank @field:AttackCategoryValidator val attackCategory: String,
    @field:NotBlank @field:WeaponCategoryValidator val weaponCategory: String,
    @field:NotNull @field:Min(0) val weight: Int
)