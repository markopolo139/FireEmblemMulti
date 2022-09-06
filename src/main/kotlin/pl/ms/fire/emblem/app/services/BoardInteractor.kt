package pl.ms.fire.emblem.app.services

import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import pl.ms.fire.emblem.app.configuration.security.UserEntity
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

//TODO : add loggers and check if current turn, add validation checking if selected character belongs to current logged in player
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
    private lateinit var playerRepository: PlayerRepository

    @Autowired
    private lateinit var simpMessagingTemplate: SimpMessagingTemplate

    @Autowired
    private lateinit var pairRepository: CharacterPairRepository

    private val userId: Int
        get() = (SecurityContextHolder.getContext().authentication.principal as UserEntity).id

    fun movePair(startingPosition: Position, route: List<Position>): List<AppSpotEntity> {
        val board = getBoard()

        val startingSpot = spotRepository.getByBoardIdAndXAndY(board.id, startingPosition.x, startingPosition.y).orElseThrow {
            logger.debug("Invalid position in move pair")
            InvalidPositionException()
        }.toAppEntity()

        val result = boardService.movePair(startingSpot, linkedSetOf<Position>().apply { addAll(route) } , board.toAppEntity())
            .map { (it as AppSpotEntity) }

        spotRepository.saveAll(result.map { it.toEntity() })
        simpMessagingTemplate.convertAndSend(
            "/topic/board-${board.id}/move", MoveMessageModel(result.first().toModel(), result.last().toModel())
        )

        return result
    }

    fun waitTurn(position: Position) {
        val pairSpot = spotRepository.getByBoardIdAndXAndY(getBoard().id, position.x, position.y).orElseThrow {
            logger.debug("Invalid position for wait turn")
            InvalidPositionException()
        }.apply {
            characterPair!!.leadCharacter.moved = true
            characterPair!!.supportCharacter?.moved = true
        }

        spotRepository.save(pairSpot)
        simpMessagingTemplate.convertAndSend(
            "/topic/board-${getBoard().id}/wait/turn", WaitMessageModel(Position(pairSpot.x, pairSpot.y))
        )

        if (pairRepository.getAllPlayerCharacters(userId).none { !it.leadCharacter.moved }) endTurn()

    }

    fun endTurn() {
        val board = getBoard().apply {
            currentPlayer = if (playerA.id == userId) playerB else playerA
        }

        boardRepository.save(board)
        simpMessagingTemplate.convertAndSend("/topic/board-${board.id}/change/turn", board.currentPlayer!!.username)

        startTurn(board.currentPlayer!!.id)
    }

    fun staffHeal(staffPosition: Position, healedPosition: Position) {
        val board = getBoard()

        val staffSpot = spotRepository.getByBoardIdAndXAndY(board.id, staffPosition.x, staffPosition.y).orElseThrow {
            logger.debug("Invalid position in move pair")
            InvalidPositionException()
        }.toAppEntity()

        val healedSpot = spotRepository.getByBoardIdAndXAndY(board.id, healedPosition.x, healedPosition.y).orElseThrow {
            logger.debug("Invalid position in move pair")
            InvalidPositionException()
        }.toAppEntity()

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
            getUsernameById(playerId), "/queue/user/start/turn",
            StartTurnMessageModel(pairs.map { Position(it.spot!!.x, it.spot!!.y) })
        )

        simpMessagingTemplate.convertAndSendToUser(
            getOppositePlayerUsername(playerId), "/queue/opponent/start/turn",
            StartTurnMessageModel(pairs.map { Position(it.spot!!.x, it.spot!!.y) })
        )
    }

    private fun getBoard() = boardRepository.joinFetchSpots(boardRepository.findIdByPlayerId(userId))

    private fun getUsernameById(playerId: Int) = playerRepository.getById(playerId).username

    private fun getOppositePlayerUsername(playerId: Int): String {
        val board = getBoard()

        return if (board.playerA.id == playerId) board.playerB!!.username else board.playerA.username
    }
}