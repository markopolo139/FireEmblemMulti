package pl.ms.fire.emblem.app.services

import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import pl.ms.fire.emblem.app.configuration.security.TokenService
import pl.ms.fire.emblem.app.configuration.security.UserEntity
import pl.ms.fire.emblem.app.entities.AppCharacterPairEntity
import pl.ms.fire.emblem.app.entities.AppSpotEntity
import pl.ms.fire.emblem.app.exceptions.BoardConfigurationException
import pl.ms.fire.emblem.app.exceptions.BoardNotFoundException
import pl.ms.fire.emblem.app.exceptions.UserInGameException
import pl.ms.fire.emblem.app.exceptions.UserNotFoundException
import pl.ms.fire.emblem.app.persistence.entities.BoardEntity
import pl.ms.fire.emblem.app.persistence.entities.PlayerEntity
import pl.ms.fire.emblem.app.persistence.repositories.BoardRepository
import pl.ms.fire.emblem.app.persistence.repositories.GameCharacterRepository
import pl.ms.fire.emblem.app.persistence.repositories.PlayerRepository
import pl.ms.fire.emblem.app.persistence.toEntity
import pl.ms.fire.emblem.business.entities.GameBoard
import pl.ms.fire.emblem.business.utlis.getStat
import pl.ms.fire.emblem.business.values.board.Spot
import pl.ms.fire.emblem.business.values.character.Stat
import kotlin.random.Random

@Service
class CreateGameService {

    companion object {
        private val logger = LogManager.getLogger()
    }

    @Autowired
    private lateinit var tokenService: TokenService

    @Autowired
    private lateinit var boardRepository: BoardRepository

    @Autowired
    private lateinit var playerRepository: PlayerRepository

    @Autowired
    private lateinit var gameCharacterRepository: GameCharacterRepository

    private val userId: Int
        get() = (SecurityContextHolder.getContext().authentication.principal as UserEntity).id

    fun createJoinGameToken(height: Int, width: Int): String {
        validateIfUserInGame(userId)
        return tokenService.createJoinGameToken(userId, height, width)
    }

    fun joinGame(token: String) {
        val playerA = playerRepository.joinFetchPresets(userId)
        val playerB = playerRepository.joinFetchPresets(tokenService.extractIdFromToken(token) ?: throw UserNotFoundException())
        val boardConf = tokenService.getBoardConfigurationFromToken(token)
        if (boardConf == null) {
            logger.debug("Invalid board configuration from $token token")
            throw BoardConfigurationException()
        }

        validateIfUserInGame(playerA.id)
        validateIfUserInGame(playerB.id)

        setRemainingHp(playerA)
        setRemainingHp(playerB)

        val spotGenerator = GameBoard(mutableMapOf(), boardConf.width, boardConf.height)
        spotGenerator.generateField()

        val saveBoard = BoardEntity(
            0, boardConf.width, boardConf.height, playerA, playerB, if (random()) playerA else playerB, mutableSetOf()
        )

        val spots = spotGenerator.spots.values.map { it.toApp().toEntity().apply { board = saveBoard } }.toMutableSet()

        saveBoard.spots.addAll(spots)

        boardRepository.save(saveBoard)
    }

    fun exitGame() {
        boardRepository.deleteById(
            boardRepository.findByPlayerId(userId).orElseThrow {
                logger.debug("Board not found for $userId user id")
                BoardNotFoundException()
            }.id
        )
    }

    private fun validateIfUserInGame(userId: Int) {
        if (boardRepository.findByPlayerId(userId).isPresent) {
            logger.debug("User with given id $userId is in game")
            throw UserInGameException()
        }
    }

    private fun random() = Random.nextBoolean()

    private fun Spot.toApp() = AppSpotEntity(0, null, position, terrain, standingCharacter as AppCharacterPairEntity?)

    private fun setRemainingHp(player: PlayerEntity) {
        val characters = player.presets.elementAt(player.currentPreset).gameCharacters
        characters.map {
                    character -> character.remainingHp = character.stats.first { it.stat == Stat.HEALTH }.value +
                        character.characterClass.boostStats.getStat(Stat.HEALTH)
        }
        gameCharacterRepository.saveAll(characters)
    }
}

