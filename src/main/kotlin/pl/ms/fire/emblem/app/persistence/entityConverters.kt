package pl.ms.fire.emblem.app.persistence

import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import pl.ms.fire.emblem.app.configuration.security.UserEntity
import pl.ms.fire.emblem.app.entities.*
import pl.ms.fire.emblem.app.persistence.embeddable.ItemEmbeddable
import pl.ms.fire.emblem.app.persistence.embeddable.StatEmbeddable
import pl.ms.fire.emblem.app.persistence.entities.*
import pl.ms.fire.emblem.business.values.board.Position
import pl.ms.fire.emblem.business.values.items.Item

fun UserDetails.toUserEntity(id: Int, email: String, preset: Int) =
    UserEntity(id, username, password, email, authorities, preset)

fun PlayerEntity.toUserEntity() =
    User.builder()
        .username(username)
        .password(password)
        .roles(*roles.toTypedArray())
        .build().toUserEntity(id, email, currentPreset)

fun UserEntity.toPlayerEntity() =
    PlayerEntity(id, username, password, email, null, currentPreset, mutableSetOf(), mutableSetOf())

fun ItemEmbeddable.toBusiness() =
    Item(name, mt, hitPercent, criticalPercent, range, attackCategory, weaponCategory, weight)

fun Item.toEntity() =
    ItemEmbeddable(name, mt, hitPercent, criticalPercent, range, attackCategory, weaponCategory, weight)

fun GameCharacterEntity.toAppEntity(preset: AppPresetEntity?) =
    AppGameCharacterEntity(id, preset, name, remainingHp, currentEquippedItem, characterClass, moved,
        stats.associate { it.stat to it.value }, items.map { it.toBusiness() }.toMutableList())

fun AppGameCharacterEntity.toEntity(preset: PlayerPresetEntity?) =
    GameCharacterEntity(id, preset, name, remainingHealth, currentEquippedItem, characterClass, moved,
        stats.map { StatEmbeddable(it.key, it.value) }.toMutableSet(), equipment.map { it.toEntity() }.toMutableSet())

fun GameCharacterEntity.toAppEntity() =
    AppGameCharacterEntity(id, preset?.toSimpleAppEntity(), name, remainingHp, currentEquippedItem, characterClass, moved,
        stats.associate { it.stat to it.value }, items.map { it.toBusiness() }.toMutableList())

fun AppGameCharacterEntity.toEntity() =
    GameCharacterEntity(id, preset?.toSimpleEntity(), name, remainingHealth, currentEquippedItem, characterClass, moved,
        stats.map { StatEmbeddable(it.key, it.value) }.toMutableSet(), equipment.map { it.toEntity() }.toMutableSet())

fun AppPresetEntity.toEntity(): PlayerPresetEntity {
    val preset = PlayerPresetEntity(id, player?.toPlayerEntity(), mutableSetOf())
    preset.gameCharacters.addAll(gameCharacterEntities.map { it.toEntity(preset) })
    return preset
}

fun PlayerPresetEntity.toAppEntity(): AppPresetEntity {
    val preset = AppPresetEntity(id, player?.toUserEntity(), mutableSetOf())
    preset.gameCharacterEntities.addAll(gameCharacters.map { it.toAppEntity(preset) })
    return preset
}

fun AppPresetEntity.toSimpleEntity() =
    PlayerPresetEntity(id, player?.toPlayerEntity(), mutableSetOf())


fun PlayerPresetEntity.toSimpleAppEntity() =
    AppPresetEntity(id, player?.toUserEntity(), mutableSetOf())

fun AppSpotEntity.toEntity(): SpotEntity {
    val spot = SpotEntity(id, board?.toSimpleEntity(), position.x, position.y, terrain, (standingCharacter as? AppCharacterPairEntity)?.toEntity())
    spot.characterPair?.spot = spot
    return spot
}

fun SpotEntity.toAppEntity(): AppSpotEntity {
    val spot = AppSpotEntity(id, board?.toSimpleAppEntity(), Position(x, y), terrain, characterPair?.toAppEntity())
    (spot.standingCharacter as? AppCharacterPairEntity)?.spot = spot
    return spot
}

fun AppSpotEntity.toSimpleEntity() =
    SpotEntity(id, board?.toSimpleEntity(), position.x, position.y, terrain, null)


fun SpotEntity.toSimpleAppEntity() =
    AppSpotEntity(id, board?.toSimpleAppEntity(), Position(x, y), terrain, null)

fun CharacterPairEntity.toAppEntity(): AppCharacterPairEntity {
    val pair = AppCharacterPairEntity(id, leadCharacter.toAppEntity(), supportCharacter?.toAppEntity(), spot?.toSimpleAppEntity())
    pair.spot?.standingCharacter = pair
    return pair
}

fun AppCharacterPairEntity.toEntity(): CharacterPairEntity {
    val pair = CharacterPairEntity(
        id, (leadCharacter as AppGameCharacterEntity).toEntity(),
        (supportCharacter as? AppGameCharacterEntity)?.toEntity(), spot?.toSimpleEntity()
    )
    pair.spot?.characterPair = pair
    return pair
}


fun BoardEntity.toSimpleAppEntity() =
    AppBoardEntity(id, height, width, playerA.toUserEntity(), playerB?.toUserEntity(), currentPlayer?.toUserEntity(), mutableMapOf())

fun AppBoardEntity.toSimpleEntity() =
    BoardEntity(id, maxWidth, maxHeight, playerA.toPlayerEntity(), playerB?.toPlayerEntity(), currentPlayer?.toPlayerEntity(), mutableSetOf())

fun BoardEntity.toAppEntity() =
    AppBoardEntity(id, height, width, playerA.toUserEntity(), playerB?.toUserEntity(), currentPlayer?.toUserEntity(),
        spots.associate { Position(it.x, it.y) to it.toAppEntity() }.toMutableMap())