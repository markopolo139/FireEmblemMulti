package pl.ms.fire.emblem.app.services

import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import pl.ms.fire.emblem.app.configuration.security.UserEntity
import pl.ms.fire.emblem.app.entities.AppGameCharacterEntity
import pl.ms.fire.emblem.app.entities.AppPresetEntity
import pl.ms.fire.emblem.app.exceptions.InvalidNumberOfCharactersInPresetException
import pl.ms.fire.emblem.app.exceptions.InvalidStatForCharacterException
import pl.ms.fire.emblem.app.exceptions.PresetDoesNotExistsException
import pl.ms.fire.emblem.app.exceptions.PresetLimitExceededException
import pl.ms.fire.emblem.app.persistence.repositories.PlayerRepository
import pl.ms.fire.emblem.app.persistence.repositories.PresetRepository
import pl.ms.fire.emblem.app.persistence.toAppEntity
import pl.ms.fire.emblem.app.persistence.toEntity

@Service
class PlayerPresetService {

    companion object {
        private val logger = LogManager.getLogger()
        private const val PRESET_LIMIT = 5
        private const val CHARACTER_IN_PRESET = 5
        private const val CHARACTER_STAT_VALUE_LIMIT = 25
    }

    @Autowired
    private lateinit var playerRepository: PlayerRepository

    @Autowired
    private lateinit var presetRepository: PresetRepository

    private val userId: Int
        get() = (SecurityContextHolder.getContext().authentication.principal as UserEntity).id

    fun createPreset(preset: AppPresetEntity) {
        val player = playerRepository.joinFetchPresets(userId)
        if (player.presets.size + 1 > PRESET_LIMIT) {
            logger.debug("Preset limit exceeded (limit $PRESET_LIMIT, given ${player.presets.size + 1}")
            throw PresetLimitExceededException(PRESET_LIMIT)
        }

        validateDataForPreset(preset.gameCharacterEntities)

        val savePreset = preset.toEntity()
        player.presets.add(savePreset)
        savePreset.player = player

        playerRepository.save(player)
    }

    fun createPresets(presets: List<AppPresetEntity>) {
        val player = playerRepository.joinFetchPresets(userId)
        if (player.presets.size + presets.size > PRESET_LIMIT){
            logger.debug("Preset limit exceeded (limit $PRESET_LIMIT, given ${player.presets.size + presets.size}")
            throw PresetLimitExceededException(PRESET_LIMIT)
        }

        presets.forEach{ validateDataForPreset(it.gameCharacterEntities) }

        val savePresets = presets.map {
            it.toEntity().apply { this.player = player }
        }
        player.presets.addAll(savePresets)

        playerRepository.save(player)
    }

    fun deletePreset(id: Int) {
        validatePresetExists(id)
        presetRepository.delete(getAllPresets().elementAt(id).toEntity())

        val player = playerRepository.getById(userId)
        if (player.currentPreset == id) {
            player.currentPreset = 0
            playerRepository.save(player)
        }

    }

    fun deletePresets() {
        presetRepository.deleteByPlayerId(userId)
    }

    fun selectPreset(id: Int) {
        validatePresetExists(id)
        val player = playerRepository.getById(userId).apply {
            currentPreset = id
        }
        playerRepository.save(player)
    }

    fun getAllPresets(): List<AppPresetEntity> =
        playerRepository.joinFetchPresets(userId).presets.map { it.toAppEntity() }


    private fun getUserPresets(): Set<AppPresetEntity> =
        playerRepository.joinFetchPresets(userId).presets.map { it.toAppEntity() }.toSet()

    private fun validatePresetExists(id: Int) {
        if (getUserPresets().elementAtOrNull(id) == null) {
            logger.debug("Preset does not exists with $id id for $userId user id")
            throw PresetDoesNotExistsException(id)
        }
    }

    private fun validateDataForPreset(characterList: Set<AppGameCharacterEntity>) {
        if (characterList.size != CHARACTER_IN_PRESET) {
            logger.debug("Invalid number of characters in preset (limit $CHARACTER_IN_PRESET, given ${characterList.size})")
            throw InvalidNumberOfCharactersInPresetException(CHARACTER_IN_PRESET)
        }

        characterList.forEach { validateCharacter(it) }
    }

    private fun validateCharacter(character: AppGameCharacterEntity) {
        if (character.stats.map { it.value }.sum() > CHARACTER_STAT_VALUE_LIMIT){
            logger.debug("Invalid number of stats for character (limit $CHARACTER_STAT_VALUE_LIMIT)")
            throw InvalidStatForCharacterException(CHARACTER_STAT_VALUE_LIMIT)
        }
    }
}