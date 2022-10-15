package pl.ms.fire.emblem.app.services

import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import pl.ms.fire.emblem.app.configuration.security.UserEntity
import pl.ms.fire.emblem.app.exceptions.user.EmailNotFoundException
import pl.ms.fire.emblem.app.exceptions.user.UserEmailAlreadyExistsException
import pl.ms.fire.emblem.app.exceptions.create.game.UserInGameException
import pl.ms.fire.emblem.app.exceptions.user.UsernameNotFoundException
import pl.ms.fire.emblem.app.persistence.entities.PlayerEntity
import pl.ms.fire.emblem.app.persistence.repositories.BoardRepository
import pl.ms.fire.emblem.app.persistence.repositories.PlayerRepository
import pl.ms.fire.emblem.app.persistence.toUserEntity

@Service
class UserService: UserDetailsService{

    companion object {
        private val logger = LogManager.getLogger()
    }

    @Autowired
    private lateinit var playerRepository: PlayerRepository

    @Autowired
    private lateinit var boardRepository: BoardRepository

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    private val userId: Int
        get() = (SecurityContextHolder.getContext().authentication.principal as UserEntity).id

    override fun loadUserByUsername(username: String?): UserDetails {
        if (username == null) {
            logger.error("Given username not found")
            throw UsernameNotFoundException()
        }

        return try {
            playerRepository.findByUsername(username).get().toUserEntity()
        }
        catch (e: NoSuchElementException) {
            logger.error("Username $username not found in database")
            throw UsernameNotFoundException()
        }

    }

    fun loadUserByUserId(userId: Int): UserDetails = try {
        playerRepository.findById(userId).get().toUserEntity()
    }
    catch (ex: NoSuchElementException) {
        logger.error("User With given id is not found")
        throw ex
    }

    fun createPlayer(email: String, username: String, password: String) {

        if(playerRepository.findByEmail(email).isPresent) {
            logger.debug("User with given email $email already exists in database")
            throw UserEmailAlreadyExistsException()
        }

        val playerEntity = PlayerEntity(
            0, username, passwordEncoder.encode(password), email,
            null, 0, mutableSetOf(), mutableSetOf("USER")
        )

        playerRepository.save(playerEntity)
    }

    fun deleteUser() {
        playerInGameValidation(userId)
        playerRepository.deleteById(userId)
    }

    @PreAuthorize("hasRole('ADMIN')")
    fun deleteUser(email: String) {
        val player = playerRepository.findByEmail(email).orElseThrow { EmailNotFoundException() }
        playerInGameValidation(player.id)
        playerRepository.deleteById(player.id)
    }

    private fun playerInGameValidation(userId: Int) {
        if (boardRepository.findByPlayerId(userId).isPresent) {
            logger.debug("User with given id $userId is in game")
            throw UserInGameException()
        }
    }

}