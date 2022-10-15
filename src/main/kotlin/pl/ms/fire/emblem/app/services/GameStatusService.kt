package pl.ms.fire.emblem.app.services

import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import pl.ms.fire.emblem.app.configuration.security.UserEntity
import pl.ms.fire.emblem.app.entities.AppSpotEntity
import pl.ms.fire.emblem.app.exceptions.board.BoardNotFoundException
import pl.ms.fire.emblem.app.exceptions.board.InvalidCharacterPairException
import pl.ms.fire.emblem.app.exceptions.user.UsernameNotFoundException
import pl.ms.fire.emblem.app.persistence.repositories.BoardRepository
import pl.ms.fire.emblem.app.persistence.repositories.CharacterPairRepository
import pl.ms.fire.emblem.app.persistence.repositories.PlayerRepository
import pl.ms.fire.emblem.app.persistence.toAppEntity

@Service
class GameStatusService {

    companion object {
        private val logger = LogManager.getLogger()
    }

    @Autowired
    private lateinit var characterPairRepository: CharacterPairRepository

    @Autowired
    private lateinit var boardRepository: BoardRepository

    @Autowired
    private lateinit var playerRepository: PlayerRepository

    private val userId: Int
        get() = (SecurityContextHolder.getContext().authentication.principal as UserEntity).id

    fun isWin(opponentUsername: String): Boolean =
        getOnlyAliveCharacters(
            playerRepository.findByUsername(opponentUsername).orElseThrow {
                logger.debug("Opponent username invalid ($opponentUsername)")
                UsernameNotFoundException()
            }.id
        ).isEmpty()

    fun getCurrentBoard(): List<AppSpotEntity> =
        boardRepository.joinFetchSpots(boardRepository.findByPlayerId(userId).orElseThrow {
            logger.debug("Logged in user is not in game")
            BoardNotFoundException()
        }.id)
            .spots.map { it.toAppEntity() }.toList()

    fun getCurrentPlayer(): String =
        boardRepository.findByPlayerId(userId).orElseThrow {
            logger.debug("Couldn't find board for logged in player")
            BoardNotFoundException()
        }.currentPlayer?.username!!

    fun getAliveNotMovedPairs(): List<AppSpotEntity> =
        getAliveAndNotMovedCharacters(userId).map {
            if (it.spot == null) {
                logger.debug("Character does not have spot (InvalidCharacterPair)")
                throw InvalidCharacterPairException()
            }
            it.spot!!.toAppEntity()
        }

    fun getAliveCharacters(): List<AppSpotEntity> =
        getOnlyAliveCharacters(userId).map {
            if (it.spot == null) {
                logger.debug("Character does not have spot (InvalidCharacterPair)")
                throw InvalidCharacterPairException()
            }
            it.spot!!.toAppEntity()
        }

    fun getEnemyAliveCharacters(): List<AppSpotEntity> {
        val board = getBoard()

        return getOnlyAliveCharacters(
            if (board.playerA.id == userId) board.playerB!!.id else board.playerA.id
        ).map {
            if (it.spot == null) {
                logger.debug("Character does not have spot (InvalidCharacterPair)")
                throw InvalidCharacterPairException()
            }
            it.spot!!.toAppEntity()
        }
    }

    fun getAllCharacters(): List<AppSpotEntity> =
        characterPairRepository.getAllPlayerCharacters(userId).map {
            if (it.spot == null) {
                logger.debug("Character does not have spot (InvalidCharacterPair)")
                throw InvalidCharacterPairException()
            }
            it.spot!!.toAppEntity()
        }


    private fun getOnlyNotMovedCharacters(playerId: Int) =
        characterPairRepository.getAllPlayerCharacters(playerId).filter { !it.leadCharacter.moved }.toSet()


    private fun getOnlyAliveCharacters(playerId: Int) =
        characterPairRepository.getAllPlayerCharacters(playerId).filter { it.leadCharacter.remainingHp != 0 }.toSet()

    private fun getAliveAndNotMovedCharacters(playerId: Int) =
        getOnlyNotMovedCharacters(playerId).filter { !it.leadCharacter.moved }.toSet()

    private fun getBoard() = boardRepository.findByPlayerId(userId).orElseThrow { BoardNotFoundException() }

}