package pl.ms.fire.emblem.app.services

import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import pl.ms.fire.emblem.app.configuration.security.UserEntity
import pl.ms.fire.emblem.app.exceptions.BoardNotFoundException
import pl.ms.fire.emblem.app.exceptions.InvalidCharacterPairException
import pl.ms.fire.emblem.app.exceptions.UsernameNotFoundException
import pl.ms.fire.emblem.app.persistence.repositories.BoardRepository
import pl.ms.fire.emblem.app.persistence.repositories.CharacterPairRepository
import pl.ms.fire.emblem.app.persistence.repositories.PlayerRepository
import pl.ms.fire.emblem.app.persistence.toAppEntity
import pl.ms.fire.emblem.app.websocket.messages.models.SpotModel
import pl.ms.fire.emblem.web.model.toModel

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

    fun getCurrentBoard(): List<SpotModel> =
        boardRepository.joinFetchSpots(boardRepository.findByPlayerId(userId).orElseThrow {
            logger.debug("Logged in user is not in game")
            BoardNotFoundException()
        }.id)
            .spots.map { it.toAppEntity().toModel() }.toList()

    fun getCurrentPlayer(): String =
        boardRepository.findByPlayerId(userId).orElseThrow {
            logger.debug("Couldn't find board for logged in player")
            BoardNotFoundException()
        }.currentPlayer?.username!!

    fun getAliveNotMovedPairs(): List<SpotModel> =
        getAliveAndNotMovedCharacters(userId).map {
            it.spot?.toAppEntity()?.toModel() ?: throw InvalidCharacterPairException()
        }

    fun getAliveCharacters(): List<SpotModel> =
        getOnlyAliveCharacters(userId).map {
            it.spot?.toAppEntity()?.toModel() ?: throw InvalidCharacterPairException()
        }

    fun getAllCharacters(): List<SpotModel> =
        characterPairRepository.getAllPlayerCharacters(userId).map {
            it.spot?.toAppEntity()?.toModel() ?: throw InvalidCharacterPairException()
        }


    private fun getOnlyNotMovedCharacters(playerId: Int) =
        characterPairRepository.getAllPlayerCharacters(playerId).filter { !it.leadCharacter.moved }.toSet()


    private fun getOnlyAliveCharacters(playerId: Int) =
        characterPairRepository.getAllPlayerCharacters(playerId).filter { it.leadCharacter.remainingHp != 0 }.toSet()

    private fun getAliveAndNotMovedCharacters(playerId: Int) =
        getOnlyNotMovedCharacters(playerId).filter { !it.leadCharacter.moved }.toSet()

}