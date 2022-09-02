package pl.ms.fire.emblem.web.controllers

import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import pl.ms.fire.emblem.business.values.board.Position
import pl.ms.fire.emblem.business.values.category.AttackCategory
import pl.ms.fire.emblem.business.values.category.WeaponCategory
import pl.ms.fire.emblem.business.values.character.CharacterClass
import pl.ms.fire.emblem.business.values.character.Stat
import pl.ms.fire.emblem.web.model.ItemModel
import pl.ms.fire.emblem.web.model.StatModel
import pl.ms.fire.emblem.web.model.request.WebGameCharacterModel

@RestController
@CrossOrigin
class Test {

    @GetMapping("/api/v1/test")
    fun test() =
        mapOf(
            1 to Position(2,5),
            2 to Position(2,6),
        )

}