package pl.ms.fire.emblem.web.model.request

import pl.ms.fire.emblem.business.values.character.CharacterClass
import pl.ms.fire.emblem.business.values.character.Stat
import pl.ms.fire.emblem.business.values.items.Item
import pl.ms.fire.emblem.web.model.ItemModel
import pl.ms.fire.emblem.web.model.StatModel
import pl.ms.fire.emblem.web.validators.CharacterClassValidator
import javax.validation.Valid
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

class GameCharacterModel(
    @field:NotNull @field:Min(0) val id: Int,
    @field:NotBlank val name: String,
    @field:NotNull @field:Min(0) val remainingHp: Int,
    @field:NotNull @field:Min(0) val currentEquippedItem: Int,
    @field:NotBlank @field:CharacterClassValidator val characterClass: String,
    @field:NotNull val moved: Boolean,
    @field:NotEmpty val stats: List<@Valid StatModel>,
    val equipment: List<@Valid ItemModel>
)