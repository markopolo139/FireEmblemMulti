package pl.ms.fire.emblem.app.services

import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import pl.ms.fire.emblem.app.configuration.security.UserEntity
import pl.ms.fire.emblem.app.entities.AppCharacterPairEntity
import pl.ms.fire.emblem.app.entities.AppGameCharacterEntity
import pl.ms.fire.emblem.app.entities.AppSpotEntity
import pl.ms.fire.emblem.app.exceptions.InvalidPositionException
import pl.ms.fire.emblem.app.persistence.repositories.SpotRepository
import pl.ms.fire.emblem.app.persistence.toAppEntity
import pl.ms.fire.emblem.app.persistence.toEntity
import pl.ms.fire.emblem.app.websocket.messages.models.toModel
import pl.ms.fire.emblem.app.websocket.messages.pair.ChangeSupportMessageModel
import pl.ms.fire.emblem.app.websocket.messages.pair.JoinPairMessageModel
import pl.ms.fire.emblem.app.websocket.messages.pair.SeparatePairMessageModel
import pl.ms.fire.emblem.app.websocket.messages.pair.TradeSupportMessageModel
import pl.ms.fire.emblem.business.exceptions.CharacterMovedException
import pl.ms.fire.emblem.business.exceptions.character.NoCharacterOnSpotException
import pl.ms.fire.emblem.business.exceptions.character.NoSupportCharacterException
import pl.ms.fire.emblem.business.exceptions.character.PairAlreadyHaveSupportException
import pl.ms.fire.emblem.business.exceptions.character.SeparatePairException
import pl.ms.fire.emblem.business.serices.CharacterManagementService
import pl.ms.fire.emblem.business.values.board.Position

@Service
class CharacterManagementInteractor {

    companion object {
        private val logger = LogManager.getLogger()
    }

    @Autowired
    private lateinit var characterService: CharacterManagementService

    @Autowired
    private lateinit var serviceUtils: ServiceUtils

    @Autowired
    private lateinit var spotRepository: SpotRepository

    @Autowired
    private lateinit var simpMessagingTemplate: SimpMessagingTemplate

    private val userId: Int
        get() = (SecurityContextHolder.getContext().authentication.principal as UserEntity).id

    fun joinIntoPair(pairPosition: Position, joinToPosition: Position) {
        serviceUtils.validateCurrentTurn()

        val pairSpot = spotRepository.getByBoardIdAndXAndY(
            serviceUtils.getBoardId(), pairPosition.x, pairPosition.y
        ).orElseThrow {
            logger.debug("Invalid position for join into pair");
            InvalidPositionException()
        }.toAppEntity()
        serviceUtils.validateCorrectPair((pairSpot.standingCharacter as? AppCharacterPairEntity))

        val joinToSpot = spotRepository.getByBoardIdAndXAndY(
            serviceUtils.getBoardId(), joinToPosition.x, joinToPosition.y
        ).orElseThrow {
            logger.debug("Invalid position for join into pair")
            InvalidPositionException()
        }.toAppEntity()
        serviceUtils.validateCorrectPair((joinToSpot.standingCharacter as? AppCharacterPairEntity))

        try {
            characterService.joinIntoPair(pairSpot, joinToSpot)
        }
        catch(e: NoCharacterOnSpotException) {
            logger.debug("No character on spot")
            throw e
        }
        catch(e: CharacterMovedException) {
            logger.debug("Selected character that moved")
            throw e
        }
        catch (e: PairAlreadyHaveSupportException) {
            logger.debug("Selected pair already have support")
            throw e
        }
        catch (e: Exception) {
            logger.debug("Unexpected exception ${e.message}")
            throw e
        }

        spotRepository.saveAll(listOf(pairSpot.toEntity(), joinToSpot.toEntity()))
        simpMessagingTemplate.convertAndSend(
            "/topic/board-${serviceUtils.getBoardId()}/pair/join",
            JoinPairMessageModel(joinToSpot.toModel(), pairSpot.toModel())
        )
    }

    fun changeSupport(pairPosition: Position) {
        serviceUtils.validateCurrentTurn()

        val pairSpot = spotRepository.getByBoardIdAndXAndY(
            serviceUtils.getBoardId(), pairPosition.x, pairPosition.y
        ).orElseThrow {
            logger.debug("Invalid position for change support")
            InvalidPositionException()
        }.toAppEntity()
        serviceUtils.validateCorrectPair((pairSpot.standingCharacter as? AppCharacterPairEntity))

        try {
            characterService.changeSupport(pairSpot)
        }
        catch(e: NoCharacterOnSpotException) {
            logger.debug("No character on spot")
            throw e
        }
        catch(e: CharacterMovedException) {
            logger.debug("Selected character that moved")
            throw e
        }
        catch (e: NoSupportCharacterException) {
            logger.debug("Selected pair does not have support")
            throw e
        }
        catch (e: Exception) {
            logger.debug("Unexpected exception ${e.message}")
            throw e
        }

        spotRepository.save(pairSpot.toEntity())

        simpMessagingTemplate.convertAndSend(
            "/topic/board-${serviceUtils.getBoardId()}/pair/change", ChangeSupportMessageModel(pairSpot.toModel())
        )

    }

    fun separatePair(pairPosition: Position, separatePosition: Position) {
        serviceUtils.validateCurrentTurn()

        val pairSpot = spotRepository.getByBoardIdAndXAndY(
            serviceUtils.getBoardId(), pairPosition.x, pairPosition.y
        ).orElseThrow {
            logger.debug("Invalid position for separate pair");
            InvalidPositionException()
        }.toAppEntity()
        serviceUtils.validateCorrectPair((pairSpot.standingCharacter as? AppCharacterPairEntity))

        val separateSpot = spotRepository.getByBoardIdAndXAndY(
            serviceUtils.getBoardId(), separatePosition.x, separatePosition.y
        ).orElseThrow {
            logger.debug("Invalid position for separate pair")
            InvalidPositionException()
        }.toAppEntity()

        val separateCopy = AppSpotEntity(
            separateSpot.id, separateSpot.board, separateSpot.position, separateSpot.terrain,
            (separateSpot.standingCharacter as?  AppCharacterPairEntity)
        )

        val pairCopy = AppSpotEntity(
            pairSpot.id, pairSpot.board, pairSpot.position, pairSpot.terrain,
            (pairSpot.standingCharacter as?  AppCharacterPairEntity)
        )

        try {
            characterService.separatePair(pairSpot, separateCopy)
        }
        catch(e: NoCharacterOnSpotException) {
            logger.debug("No character on spot")
            throw e
        }
        catch(e: CharacterMovedException) {
            logger.debug("Selected character that moved")
            throw e
        }
        catch (e: SeparatePairException) {
            logger.debug("Character standing on selected spot")
            throw e
        }
        catch (e: NoSupportCharacterException) {
            logger.debug("Selected pair does not have support character")
            throw e
        }
        catch (e: Exception) {
            logger.debug("Unexpected exception ${e.message}")
            throw e
        }

        separateSpot.standingCharacter = AppCharacterPairEntity(
            0, (pairCopy.standingCharacter?.supportCharacter as AppGameCharacterEntity), null, separateSpot
        )

        spotRepository.saveAll(listOf(pairSpot.toEntity(), separateSpot.toEntity()))

        simpMessagingTemplate.convertAndSend(
            "/topic/board-${serviceUtils.getBoardId()}/pair/separate",
            SeparatePairMessageModel(pairSpot.toModel(), separateSpot.toModel())
        )
    }

    fun tradeSupport(pairPosition: Position, tradePosition: Position) {
        serviceUtils.validateCurrentTurn()

        val pairSpot = spotRepository.getByBoardIdAndXAndY(
            serviceUtils.getBoardId(), pairPosition.x, pairPosition.y
        ).orElseThrow {
            logger.debug("Invalid position for trade support");
            InvalidPositionException()
        }.toAppEntity()
        serviceUtils.validateCorrectPair((pairSpot.standingCharacter as? AppCharacterPairEntity))

        val tradeSpot = spotRepository.getByBoardIdAndXAndY(
            serviceUtils.getBoardId(), tradePosition.x, tradePosition.y
        ).orElseThrow {
            logger.debug("Invalid position for trade support")
            InvalidPositionException()
        }.toAppEntity()
        serviceUtils.validateCorrectPair((tradeSpot.standingCharacter as? AppCharacterPairEntity))

        try {
            characterService.tradeSupport(pairSpot, tradeSpot)
        }
        catch(e: NoCharacterOnSpotException) {
            logger.debug("No character on spot")
            throw e
        }
        catch(e: CharacterMovedException) {
            logger.debug("Selected character that moved")
            throw e
        }
        catch (e: NoSupportCharacterException) {
            logger.debug("Neither pair have a support")
            throw e
        }
        catch (e: Exception) {
            logger.debug("Unexpected exception ${e.message}")
            throw e
        }

        spotRepository.saveAll(listOf(pairSpot.toEntity(), tradeSpot.toEntity()))

        simpMessagingTemplate.convertAndSend(
            "/topic/board-${serviceUtils.getBoardId()}/pair/trade/support",
            TradeSupportMessageModel(pairSpot.toModel(), tradeSpot.toModel())
        )
    }

}