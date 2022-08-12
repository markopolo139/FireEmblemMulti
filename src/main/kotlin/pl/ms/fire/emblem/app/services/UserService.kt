package pl.ms.fire.emblem.app.services

import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import pl.ms.fire.emblem.app.exceptions.UsernameNotFoundException
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
    private lateinit var passwordEncoder: PasswordEncoder

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

}