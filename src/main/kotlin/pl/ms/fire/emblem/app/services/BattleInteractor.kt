package pl.ms.fire.emblem.app.services

import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import pl.ms.fire.emblem.app.configuration.security.UserEntity
import pl.ms.fire.emblem.app.entities.AppCharacterPairEntity
import pl.ms.fire.emblem.app.exceptions.board.InvalidPositionException
import pl.ms.fire.emblem.app.persistence.repositories.*
import pl.ms.fire.emblem.app.persistence.toAppEntity
import pl.ms.fire.emblem.app.persistence.toEntity
import pl.ms.fire.emblem.app.websocket.messages.battle.BattleForecastMessage
import pl.ms.fire.emblem.app.websocket.messages.battle.BattleMessageModel
import pl.ms.fire.emblem.app.websocket.messages.models.toModel
import pl.ms.fire.emblem.business.exceptions.CharacterMovedException
import pl.ms.fire.emblem.business.exceptions.battle.NotAllowedWeaponCategoryException
import pl.ms.fire.emblem.business.exceptions.battle.OutOfRangeException
import pl.ms.fire.emblem.business.exceptions.battle.StaffInBattleException
import pl.ms.fire.emblem.business.exceptions.character.NoCharacterOnSpotException
import pl.ms.fire.emblem.business.exceptions.item.ItemDoesNotExistsException
import pl.ms.fire.emblem.business.exceptions.item.NoItemEquippedException
import pl.ms.fire.emblem.business.serices.BattleService
import pl.ms.fire.emblem.business.utlis.Displayable
import pl.ms.fire.emblem.business.values.battle.BattleForecast
import pl.ms.fire.emblem.business.values.board.Position

@Service
class BattleInteractor {

    companion object {
        private val logger = LogManager.getLogger()
    }

    @Autowired
    private lateinit var battleService: BattleService

    @Autowired
    private lateinit var simpMessagingTemplate: SimpMessagingTemplate

    @Autowired
    private lateinit var spotRepository: SpotRepository

    @Autowired
    private lateinit var serviceUtils: ServiceUtils

    private val userId: Int
        get() = (SecurityContextHolder.getContext().authentication.principal as UserEntity).id

    //TODO: check if defender is enemy( or leave for tactical aspect) !THINK!
    fun battle(attacker: Position, defender: Position): List<Displayable> {

        serviceUtils.validateCurrentTurn()

        val boardId = serviceUtils.getBoardId()

        val attackerSpot = spotRepository.getByBoardIdAndXAndY(boardId, attacker.x, attacker.y).orElseThrow {
             logger.debug("Given attacker position is invalid")
             InvalidPositionException()
        }.toAppEntity()
        serviceUtils.validateCorrectPair((attackerSpot.standingCharacter as? AppCharacterPairEntity))

        val defenderSpot = spotRepository.getByBoardIdAndXAndY(boardId, defender.x, defender.y).orElseThrow {
             logger.debug("Given defender position is invalid")
             InvalidPositionException()
        }.toAppEntity()

        val course: List<Displayable> = try {
            battleService.battle(attackerSpot, defenderSpot)
        }
        catch(e: NoCharacterOnSpotException) {
            logger.debug("No character on spot")
            throw e
        }
        catch(e: CharacterMovedException) {
            logger.debug("Selected character that moved")
            throw e
        }
        catch(e: NoItemEquippedException) {
            logger.debug("Selected character without weapon equipped")
            throw e
        }
        catch(e: NotAllowedWeaponCategoryException) {
            logger.debug("Invalid equipped weapon")
            throw e
        }
        catch(e: StaffInBattleException) {
            logger.debug("Character equipped with staff (Can't use staff in battle)")
            throw e
        }
        catch(e: OutOfRangeException) {
            logger.debug("Can't attack with equipped weapon (out of reach)")
            throw e
        }
        catch (e: Exception) {
            logger.debug("Unexpected exception ${e.message}")
            throw e
        }

        spotRepository.save(attackerSpot.toEntity())
        spotRepository.save(defenderSpot.toEntity())

        simpMessagingTemplate.convertAndSend(
            "/topic/board-$boardId/battle",
            BattleMessageModel(attackerSpot.toModel(), defenderSpot.toModel(), course.map { it.displayName })
        )

        return course
    }

    fun battleForecast(attacker: Position, defender: Position): BattleForecast {

        serviceUtils.validateCurrentTurn()

        val attackerSpot = spotRepository.getByBoardIdAndXAndY(serviceUtils.getBoardId(), attacker.x, attacker.y).orElseThrow {
            logger.debug("Given attacker position is invalid")
            InvalidPositionException()
        }.toAppEntity()
        serviceUtils.validateCorrectPair((attackerSpot.standingCharacter as? AppCharacterPairEntity))

        val defenderSpot = spotRepository.getByBoardIdAndXAndY(serviceUtils.getBoardId(), defender.x, defender.y).orElseThrow {
            logger.debug("Given defender position is invalid")
            InvalidPositionException()
        }.toAppEntity()

        val forecast = try {
            battleService.battleForecast(attackerSpot, defenderSpot)
        }
        catch(e: NoCharacterOnSpotException) {
            logger.debug("No character on spot")
            throw e
        }
        catch(e: CharacterMovedException) {
            logger.debug("Selected character that moved")
            throw e
        }
        catch(e: NoItemEquippedException) {
            logger.debug("Selected character without weapon equipped")
            throw e
        }
        catch(e: NotAllowedWeaponCategoryException) {
            logger.debug("Invalid equipped weapon")
            throw e
        }
        catch(e: StaffInBattleException) {
            logger.debug("Character equipped with staff (Can't use staff in battle)")
            throw e
        }
        catch(e: OutOfRangeException) {
            logger.debug("Can't attack with equipped weapon (out of reach)")
            throw e
        }
        catch (e: ItemDoesNotExistsException) {
            logger.debug("Equipped item does not exists (null in equipment for character)")
            throw e
        }
        catch (e: Exception) {
            logger.debug("Unexpected exception ${e.message}")
            throw e
        }

        simpMessagingTemplate.convertAndSendToUser(
            serviceUtils.getUserUsername(), "/queue/battle/forecast",
            BattleForecastMessage(attackerSpot.toModel(), defenderSpot.toModel(), forecast)
        )

        return forecast
    }
}