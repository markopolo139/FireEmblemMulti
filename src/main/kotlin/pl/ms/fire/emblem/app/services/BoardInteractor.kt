package pl.ms.fire.emblem.app.services

import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import pl.ms.fire.emblem.app.configuration.security.UserEntity
import pl.ms.fire.emblem.app.entities.AppCharacterPairEntity
import pl.ms.fire.emblem.app.entities.AppSpotEntity
import pl.ms.fire.emblem.app.exceptions.InvalidPositionException
import pl.ms.fire.emblem.app.persistence.repositories.*
import pl.ms.fire.emblem.app.persistence.toAppEntity
import pl.ms.fire.emblem.app.persistence.toEntity
import pl.ms.fire.emblem.app.websocket.messages.board.MoveMessageModel
import pl.ms.fire.emblem.app.websocket.messages.board.StaffHealMessageModel
import pl.ms.fire.emblem.app.websocket.messages.board.StartTurnMessageModel
import pl.ms.fire.emblem.app.websocket.messages.board.WaitMessageModel
import pl.ms.fire.emblem.app.websocket.messages.models.toModel
import pl.ms.fire.emblem.business.serices.BoardService
import pl.ms.fire.emblem.business.values.board.Position

//TODO :add loggers
@Service
class BoardInteractor {

    companion object {
        private val logger = LogManager.getLogger()
    }

    @Autowired
    private lateinit var boardService: BoardService

    @Autowired
    private lateinit var spotRepository: SpotRepository

    @Autowired
    private lateinit var boardRepository: BoardRepository

    @Autowired
    private lateinit var simpMessagingTemplate: SimpMessagingTemplate

    @Autowired
    private lateinit var pairRepository: CharacterPairRepository

    @Autowired
    private lateinit var serviceUtils: ServiceUtils

    private val userId: Int
        get() = (SecurityContextHolder.getContext().authentication.principal as UserEntity).id

    fun movePair(startingPosition: Position, route: List<Position>): List<AppSpotEntity> {

        serviceUtils.validateCurrentTurn()

        val board = serviceUtils.getBoard()

        val startingSpot = spotRepository.getByBoardIdAndXAndY(board.id, startingPosition.x, startingPosition.y).orElseThrow {
            logger.debug("Invalid position in move pair")
            InvalidPositionException()
        }.toAppEntity()
        serviceUtils.validateCorrectPair((startingSpot.standingCharacter!! as AppCharacterPairEntity).id)

        val result = boardService.movePair(startingSpot, linkedSetOf<Position>().apply { addAll(route) } , board.toAppEntity())
            .map { (it as AppSpotEntity) }

        spotRepository.saveAll(result.map { it.toEntity() })
        simpMessagingTemplate.convertAndSend(
            "/topic/board-${board.id}/move", MoveMessageModel(result.first().toModel(), result.last().toModel())
        )

        return result
    }

    fun waitTurn(position: Position) {

        serviceUtils.validateCurrentTurn()

        val pairSpot = spotRepository.getByBoardIdAndXAndY(serviceUtils.getBoard().id, position.x, position.y).orElseThrow {
            logger.debug("Invalid position for wait turn")
            InvalidPositionException()
        }.apply {
            characterPair!!.leadCharacter.moved = true
            characterPair!!.supportCharacter?.moved = true
        }
        serviceUtils.validateCorrectPair(pairSpot.characterPair!!.id)

        spotRepository.save(pairSpot)
        simpMessagingTemplate.convertAndSend(
            "/topic/board-${serviceUtils.getBoard().id}/wait/turn", WaitMessageModel(Position(pairSpot.x, pairSpot.y))
        )

        if (pairRepository.getAllPlayerCharacters(userId).none { !it.leadCharacter.moved }) endTurn()

    }

    fun endTurn() {
        serviceUtils.validateCurrentTurn()

        val board = serviceUtils.getBoard().apply {
            currentPlayer = if (playerA.id == userId) playerB else playerA
        }

        boardRepository.save(board)
        simpMessagingTemplate.convertAndSend("/topic/board-${board.id}/change/turn", board.currentPlayer!!.username)

        startTurn(board.currentPlayer!!.id)
    }

    fun staffHeal(staffPosition: Position, healedPosition: Position) {
        serviceUtils.validateCurrentTurn()

        val board = serviceUtils.getBoard()

        val staffSpot = spotRepository.getByBoardIdAndXAndY(board.id, staffPosition.x, staffPosition.y).orElseThrow {
            logger.debug("Invalid position in move pair")
            InvalidPositionException()
        }.toAppEntity()

        val healedSpot = spotRepository.getByBoardIdAndXAndY(board.id, healedPosition.x, healedPosition.y).orElseThrow {
            logger.debug("Invalid position in move pair")
            InvalidPositionException()
        }.toAppEntity()

        serviceUtils.validateCorrectPair((staffSpot.standingCharacter!! as AppCharacterPairEntity).id)
        serviceUtils.validateCorrectPair((healedSpot.standingCharacter!! as AppCharacterPairEntity).id)

        boardService.staffHeal(staffSpot, healedSpot)

        spotRepository.saveAll(listOf(staffSpot.toEntity(), healedSpot.toEntity()))
        simpMessagingTemplate.convertAndSend(
            "/topic/board-${board.id}/staff/heal", StaffHealMessageModel(staffSpot.toModel(), healedSpot.toModel())
        )

    }

    private fun startTurn(playerId: Int) {
        val pairs = pairRepository.getAllPlayerCharacters(playerId).map {
            it.apply {
                leadCharacter.moved = false
                supportCharacter?.moved = false
            }
        }

        pairRepository.saveAll(pairs)

        simpMessagingTemplate.convertAndSendToUser(
            serviceUtils.getUsernameById(playerId), "/queue/user/start/turn",
            StartTurnMessageModel(pairs.map { Position(it.spot!!.x, it.spot!!.y) })
        )

        simpMessagingTemplate.convertAndSendToUser(
            serviceUtils.getOppositePlayerUsername(playerId), "/queue/opponent/start/turn",
            StartTurnMessageModel(pairs.map { Position(it.spot!!.x, it.spot!!.y) })
        )
    }
}