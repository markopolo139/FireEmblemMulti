package pl.ms.fire.emblem.web.model

import pl.ms.fire.emblem.business.values.category.AttackCategory
import pl.ms.fire.emblem.business.values.category.WeaponCategory
import pl.ms.fire.emblem.business.values.character.CharacterClass
import pl.ms.fire.emblem.business.values.character.Stat
import pl.ms.fire.emblem.business.values.items.Item
import pl.ms.fire.emblem.web.model.request.GameCharacterModel

typealias AppCharacterModel = pl.ms.fire.emblem.app.websocket.messages.models.GameCharacterModel

fun ItemModel.toApp() =
    Item(name, mt, hitPercent, criticalPercent, range, AttackCategory.valueOf(attackCategory),
        WeaponCategory.valueOf(weaponCategory), weight)

fun List<StatModel>.toMap() = associate { Stat.valueOf(it.stat) to it.value }

fun GameCharacterModel.toAppModel() =
    AppCharacterModel(
        id, name, remainingHp, currentEquippedItem, CharacterClass.valueOf(characterClass), moved, stats.toMap(),
        equipment.map { it.toApp() }.toMutableList()
    )