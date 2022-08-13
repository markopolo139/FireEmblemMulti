package pl.ms.fire.emblem.app.recovery

import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.thymeleaf.TemplateEngine
import pl.ms.fire.emblem.app.configuration.security.TokenService
import pl.ms.fire.emblem.app.exceptions.EmailNotFoundException
import pl.ms.fire.emblem.app.exceptions.PasswordRecoveryTokenNotFoundException
import pl.ms.fire.emblem.app.persistence.repositories.PlayerRepository

@Service
class PasswordRecoveryService {

    companion object {
        private val logger = LogManager.getLogger()

        private const val FROM = "spring assistant"
        private const val PATH = "/api/v1/change/password?token="
    }

    @Autowired
    private lateinit var mailSender: JavaMailSender

    @Autowired
    private lateinit var tokenService: TokenService

    @Autowired
    private lateinit var playerRepository: PlayerRepository

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    private lateinit var templateEngine: TemplateEngine

    fun sendMessage(email: String, serverAddress: String) {

        val player = playerRepository.findByEmail(email).orElseGet {
            logger.error("Typed email $email not found")
            throw EmailNotFoundException()
        }

        prepareMessage(email, "$serverAddress$PATH${tokenService.createPasswordRecoveryToken(player.id)}")
    }

    private fun prepareMessage(email: String, sendPath: String) {



    }

    fun changePassword(newPassword: String) {

        if (tokenService.isPasswordRecoveryToken()) {
            logger.error("Given token is not used for password recovery")
            throw PasswordRecoveryTokenNotFoundException()
        }

        val player = playerRepository.getById(tokenService.extractIdFromToken()!!).apply {
            password = passwordEncoder.encode(newPassword)
        }

        playerRepository.save(player)

    }

}