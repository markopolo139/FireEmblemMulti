package pl.ms.fire.emblem.web.model

import pl.ms.fire.emblem.app.entities.AppBoardConfiguration
import pl.ms.fire.emblem.app.entities.AppCharacterPairEntity
import pl.ms.fire.emblem.app.entities.AppGameCharacterEntity
import pl.ms.fire.emblem.app.entities.AppSpotEntity
import pl.ms.fire.emblem.app.websocket.messages.models.CharacterPairModel
import pl.ms.fire.emblem.app.websocket.messages.models.GameCharacterModel
import pl.ms.fire.emblem.app.websocket.messages.models.SpotModel
import pl.ms.fire.emblem.app.websocket.messages.models.toModel
import pl.ms.fire.emblem.business.values.board.Position
import pl.ms.fire.emblem.business.values.category.AttackCategory
import pl.ms.fire.emblem.business.values.category.WeaponCategory
import pl.ms.fire.emblem.business.values.character.CharacterClass
import pl.ms.fire.emblem.business.values.character.Stat
import pl.ms.fire.emblem.business.values.items.Item
import pl.ms.fire.emblem.web.model.request.PositionModel
import pl.ms.fire.emblem.web.model.request.WebGameCharacterModel
import pl.ms.fire.emblem.web.model.response.BoardConfigurationModel
import pl.ms.fire.emblem.web.model.response.CharacterPairResponse
import pl.ms.fire.emblem.web.model.response.SpotResponse

fun ItemModel.toApp() =
    Item(name, mt, hitPercent, criticalPercent, range, AttackCategory.valueOf(attackCategory),
        WeaponCategory.valueOf(weaponCategory), weight)

fun Item.toWeb() =
    ItemModel(name, mt, hitPercent, criticalPercent, range, attackCategory.name, weaponCategory.name, weight)

fun List<StatModel>.toMap() = associate { Stat.valueOf(it.stat) to it.value }

fun WebGameCharacterModel.toAppModel() =
    GameCharacterModel(
        id, presetId, name, remainingHp, currentEquippedItem, CharacterClass.valueOf(characterClass), moved, stats.toMap(),
        equipment.map { it.toApp() }.toMutableList()
    )

fun WebGameCharacterModel.toEntityNoPreset() =
    AppGameCharacterEntity(
        id, null, name, remainingHp, currentEquippedItem, CharacterClass.valueOf(characterClass), moved,
        stats.associate { Stat.valueOf(it.stat) to it.value }, equipment.map { it.toApp() }.toMutableList()
    )

fun AppGameCharacterEntity.toWebModel() =
    WebGameCharacterModel(
        id, preset?.id!!, name, remainingHealth, currentEquippedItem, characterClass.name, moved,
        stats.map { StatModel(it.key.name, it.value) }, equipment.map { it.toWeb() }
    )

fun Position.toModel() = PositionModel(x,y)

fun PositionModel.toBusiness() = Position(x,y)

fun AppCharacterPairEntity.toWebModel() =
    CharacterPairResponse(
        (leadCharacter as AppGameCharacterEntity).toWebModel(),
        (supportCharacter as? AppGameCharacterEntity)?.toWebModel(),
    )

fun AppSpotEntity.toWebModel() =
    SpotResponse(position.toModel(), terrain, (standingCharacter as? AppCharacterPairEntity)?.toWebModel())

fun AppBoardConfiguration.toModel() = BoardConfigurationModel(opponentUsername, height, width)