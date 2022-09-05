package pl.ms.fire.emblem.app.services

import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import pl.ms.fire.emblem.app.configuration.security.UserEntity
import pl.ms.fire.emblem.app.exceptions.BoardNotFoundException
import pl.ms.fire.emblem.app.exceptions.InvalidPositionException
import pl.ms.fire.emblem.app.persistence.repositories.BoardRepository
import pl.ms.fire.emblem.app.persistence.repositories.GameCharacterRepository
import pl.ms.fire.emblem.app.persistence.repositories.PlayerRepository
import pl.ms.fire.emblem.app.persistence.repositories.SpotRepository
import pl.ms.fire.emblem.app.persistence.toAppEntity
import pl.ms.fire.emblem.app.persistence.toEntity
import pl.ms.fire.emblem.app.websocket.messages.battle.BattleForecastMessage
import pl.ms.fire.emblem.app.websocket.messages.battle.BattleMessageModel
import pl.ms.fire.emblem.app.websocket.messages.models.toModel
import pl.ms.fire.emblem.business.serices.BattleService
import pl.ms.fire.emblem.business.serices.BoardService
import pl.ms.fire.emblem.business.utlis.Displayable
import pl.ms.fire.emblem.business.values.battle.BattleForecast
import pl.ms.fire.emblem.business.values.board.Position
import pl.ms.fire.emblem.web.model.toModel

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
    private lateinit var boardRepository: BoardRepository

    private val userId: Int
        get() = (SecurityContextHolder.getContext().authentication.principal as UserEntity).id

    fun battle(attacker: Position, defender: Position): List<Displayable> {
        val boardId = getBoardId()

        val attackerSpot = spotRepository.getByBoardIdAndXAndY(boardId, attacker.x, attacker.y).orElseThrow {
             logger.debug("Given attacker position is invalid")
             InvalidPositionException()
        }.toAppEntity()

        val defenderSpot = spotRepository.getByBoardIdAndXAndY(boardId, defender.x, defender.y).orElseThrow {
             logger.debug("Given defender position is invalid")
             InvalidPositionException()
        }.toAppEntity()


        val course = battleService.battle(attackerSpot, defenderSpot)
        spotRepository.save(attackerSpot.toEntity())
        spotRepository.save(defenderSpot.toEntity())

        simpMessagingTemplate.convertAndSend(
            "/topic/board-$boardId/battle",
            BattleMessageModel(attackerSpot.toModel(), defenderSpot.toModel(), course.map { it.displayName })
        )

        return course
    }

    fun battleForecast(attacker: Position, defender: Position): BattleForecast {
        val attackerSpot = spotRepository.getByBoardIdAndXAndY(getBoardId(), attacker.x, attacker.y).orElseThrow {
            logger.debug("Given attacker position is invalid")
            InvalidPositionException()
        }.toAppEntity()
        val defenderSpot = spotRepository.getByBoardIdAndXAndY(getBoardId(), defender.x, defender.y).orElseThrow {
            logger.debug("Given defender position is invalid")
            InvalidPositionException()
        }.toAppEntity()

        val forecast = battleService.battleForecast(attackerSpot, defenderSpot)

        simpMessagingTemplate.convertAndSendToUser(
            getUserUsername(), "/queue/battle/forecast",
            BattleForecastMessage(attackerSpot.toModel(), defenderSpot.toModel(), forecast)
        )

        return forecast
    }

    private fun getBoardId() = boardRepository.findIdByPlayerId(userId)

    private fun getUserUsername() = (SecurityContextHolder.getContext().authentication.principal as UserEntity).username
}