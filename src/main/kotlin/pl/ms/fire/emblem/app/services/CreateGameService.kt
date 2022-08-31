package pl.ms.fire.emblem.app.services

import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import pl.ms.fire.emblem.app.configuration.security.TokenService
import pl.ms.fire.emblem.app.configuration.security.UserEntity
import pl.ms.fire.emblem.app.entities.AppCharacterPairEntity
import pl.ms.fire.emblem.app.entities.AppSpotEntity
import pl.ms.fire.emblem.app.exceptions.*
import pl.ms.fire.emblem.app.persistence.entities.BoardEntity
import pl.ms.fire.emblem.app.persistence.entities.PlayerEntity
import pl.ms.fire.emblem.app.persistence.repositories.BoardRepository
import pl.ms.fire.emblem.app.persistence.repositories.GameCharacterRepository
import pl.ms.fire.emblem.app.persistence.repositories.PlayerRepository
import pl.ms.fire.emblem.app.persistence.repositories.SpotRepository
import pl.ms.fire.emblem.app.persistence.toAppEntity
import pl.ms.fire.emblem.app.persistence.toEntity
import pl.ms.fire.emblem.business.entities.GameBoard
import pl.ms.fire.emblem.business.utlis.getStat
import pl.ms.fire.emblem.business.values.board.Position
import pl.ms.fire.emblem.business.values.board.Spot
import pl.ms.fire.emblem.business.values.character.Stat
import kotlin.random.Random

@Service
class CreateGameService {

    companion object {
        private val logger = LogManager.getLogger()
        private const val PLAYER_Y_LIMIT = 2
        private const val MIN_HEIGHT = 8
        private const val MAX_HEIGHT = 20
        private const val MIN_WIDTH = 8
        private const val MAX_WIDTH = 20
    }

    @Autowired
    private lateinit var tokenService: TokenService

    @Autowired
    private lateinit var boardRepository: BoardRepository

    @Autowired
    private lateinit var playerRepository: PlayerRepository

    @Autowired
    private lateinit var gameCharacterRepository: GameCharacterRepository

    @Autowired
    private lateinit var spotRepository: SpotRepository

    private val userId: Int
        get() = (SecurityContextHolder.getContext().authentication.principal as UserEntity).id

    fun createJoinGameToken(height: Int, width: Int): String {
        validateIfUserInGame(userId)
        validateBoardSize(height, width)
        return tokenService.createJoinGameToken(userId, height, width)
    }

    fun joinGame(token: String) {
        val playerA = playerRepository.joinFetchPresets(userId)
        val playerB =
            playerRepository.joinFetchPresets(tokenService.extractIdFromToken(token) ?: throw UserNotFoundException())
        val boardConf = tokenService.getBoardConfigurationFromToken(token)
        if (boardConf == null) {
            logger.debug("Invalid board configuration from $token token")
            throw BoardConfigurationException("Can't read board configuration from given join game token")
        }

        validateIfUserInGame(playerA.id)
        validateIfUserInGame(playerB.id)

        if(playerA.presets.isEmpty() || playerB.presets.isEmpty()) {
            logger.debug("One of the players does not have presets")
            throw NoPresetsException()
        }

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

    fun setUpCharacters(charactersId: Map<Int, Position>) {
        validateStartPositions(charactersId.values.toSet())
        val boardEntity = boardRepository.joinFetchSpots(
            boardRepository.findByPlayerId(userId).orElseThrow { BoardNotFoundException() }.id
        )
        val spots = boardEntity.toAppEntity().spots
        val editedSpots = mutableListOf<AppSpotEntity>()

        val characters = charactersId.map {
            it.value to gameCharacterRepository.getById(it.key).toAppEntity()
        }.toMap()

        characters.forEach {
            spots[it.key]?.standingCharacter = AppCharacterPairEntity(
                0, it.value, null,
                spots[it.key] as? AppSpotEntity
            )
            editedSpots.add(spots[it.key] as? AppSpotEntity ?: throw InvalidSpotException())
        }

        spotRepository.saveAll(
            editedSpots.map { spot -> spot.toEntity().apply { board = boardEntity } }.toList()
        )
    }

    private fun validateBoardSize(height: Int, width: Int) {
        if (height < MIN_HEIGHT || width < MIN_WIDTH || height > MAX_HEIGHT || width > MAX_WIDTH)
            throw BoardConfigurationException("Invalid board size")
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
        characters.map { character ->
            character.remainingHp = character.stats.first { it.stat == Stat.HEALTH }.value +
                    character.characterClass.boostStats.getStat(Stat.HEALTH)
        }
        gameCharacterRepository.saveAll(characters)
    }

    private fun isPlayerA(): Boolean =
        boardRepository.findByPlayerId(userId).orElseThrow { BoardNotFoundException() }.playerA.id == userId

    private fun getPlayerByLimit() =
        boardRepository.findByPlayerId(userId).orElseThrow { BoardNotFoundException() }.height - PLAYER_Y_LIMIT

    private fun getYRange(): IntRange =
        if (isPlayerA()) 0..1 else (getPlayerByLimit() + 1)..getPlayerByLimit() + PLAYER_Y_LIMIT

    private fun getXRange(): IntRange {
        val width = boardRepository.findByPlayerId(userId).orElseThrow { BoardNotFoundException() }.width
        return (width/2) - 2..(width/2) + 2
    }

    private fun validateStartPositions(positions: Set<Position>) {
        val xRange = getXRange()
        val yRange = getYRange()

        positions.forEach {
            if (it.x !in xRange || it.y !in yRange)
                throw InvalidStartPositionException()
        }
    }

}

