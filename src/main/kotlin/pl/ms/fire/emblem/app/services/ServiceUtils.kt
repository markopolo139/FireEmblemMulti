package pl.ms.fire.emblem.app.services

import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import pl.ms.fire.emblem.app.configuration.security.UserEntity
import pl.ms.fire.emblem.app.entities.AppCharacterPairEntity
import pl.ms.fire.emblem.app.exceptions.board.NotCurrentTurnException
import pl.ms.fire.emblem.app.exceptions.board.NotPlayersPairException
import pl.ms.fire.emblem.app.persistence.repositories.BoardRepository
import pl.ms.fire.emblem.app.persistence.repositories.CharacterPairRepository
import pl.ms.fire.emblem.app.persistence.repositories.PlayerRepository
import pl.ms.fire.emblem.business.exceptions.character.NoCharacterOnSpotException

@Component
class ServiceUtils {

    companion object {
        private var logger = LogManager.getLogger()
    }

    @Autowired
    private lateinit var playerRepository: PlayerRepository

    @Autowired
    private lateinit var boardRepository: BoardRepository

    @Autowired
    private lateinit var pairRepository: CharacterPairRepository

    private val userId: Int
        get() = (SecurityContextHolder.getContext().authentication.principal as UserEntity).id

    fun getBoardId() = boardRepository.findIdByPlayerId(userId)

    fun getBoard() = boardRepository.joinFetchSpots(boardRepository.findIdByPlayerId(userId))

    fun getUserUsername(): String = (SecurityContextHolder.getContext().authentication.principal as UserEntity).username

    fun getUsernameById(playerId: Int) = playerRepository.getById(playerId).username

    fun getOppositePlayerUsername(playerId: Int): String {
        val board = getBoard()

        return if (board.playerA.id == playerId) board.playerB!!.username else board.playerA.username
    }

    fun validateCurrentTurn() {
        if (getBoard().currentPlayer!!.id != userId) {
            logger.debug("It is not player turn")
            throw NotCurrentTurnException()
        }
    }

    fun validateCorrectPair(pair: AppCharacterPairEntity?) {
        if (pair == null) {
            logger.debug("Selected spot does not have pair")
            throw NoCharacterOnSpotException()
        }

        val pairId = pair.id

        if (pairRepository.getAllPlayerCharacters(userId).map { it.id }.none { it == pairId }) {
            logger.debug("Selected pair does not belong to current player")
            throw NotPlayersPairException()
        }
    }
}