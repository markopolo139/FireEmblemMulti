package pl.ms.fire.emblem.web.model

import pl.ms.fire.emblem.app.entities.AppCharacterPairEntity
import pl.ms.fire.emblem.app.entities.AppGameCharacterEntity
import pl.ms.fire.emblem.app.entities.AppSpotEntity
import pl.ms.fire.emblem.app.websocket.messages.models.CharacterPairModel
import pl.ms.fire.emblem.app.websocket.messages.models.GameCharacterModel
import pl.ms.fire.emblem.app.websocket.messages.models.SpotModel
import pl.ms.fire.emblem.app.websocket.messages.models.toModel
import pl.ms.fire.emblem.business.values.category.AttackCategory
import pl.ms.fire.emblem.business.values.category.WeaponCategory
import pl.ms.fire.emblem.business.values.character.CharacterClass
import pl.ms.fire.emblem.business.values.character.Stat
import pl.ms.fire.emblem.business.values.items.Item
import pl.ms.fire.emblem.web.model.request.RequestGameCharacterModel

fun ItemModel.toApp() =
    Item(name, mt, hitPercent, criticalPercent, range, AttackCategory.valueOf(attackCategory),
        WeaponCategory.valueOf(weaponCategory), weight)

fun List<StatModel>.toMap() = associate { Stat.valueOf(it.stat) to it.value }

fun RequestGameCharacterModel.toAppModel() =
    GameCharacterModel(
        id, presetId, name, remainingHp, currentEquippedItem, CharacterClass.valueOf(characterClass), moved, stats.toMap(),
        equipment.map { it.toApp() }.toMutableList()
    )

fun RequestGameCharacterModel.toEntityNoPreset() =
    AppGameCharacterEntity(
        id, null, name, remainingHp, currentEquippedItem, CharacterClass.valueOf(characterClass), moved,
        stats.associate { Stat.valueOf(it.stat) to it.value }, equipment.map { it.toApp() }.toMutableList()
    )

fun AppCharacterPairEntity.toModel() =
    CharacterPairModel((leadCharacter as AppGameCharacterEntity).toModel(), (supportCharacter as? AppGameCharacterEntity)?.toModel())

fun AppSpotEntity.toModel() =
    SpotModel(position, terrain, (standingCharacter as? AppCharacterPairEntity)?.toModel())