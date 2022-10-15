package pl.ms.fire.emblem.app.recovery

import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.MailException
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import pl.ms.fire.emblem.app.configuration.security.TokenService
import pl.ms.fire.emblem.app.exceptions.user.EmailNotFoundException
import pl.ms.fire.emblem.app.exceptions.token.PasswordRecoveryTokenNotFoundException
import pl.ms.fire.emblem.app.persistence.repositories.PlayerRepository
import java.io.File

@Service
class PasswordRecoveryService {

    companion object {
        private val logger = LogManager.getLogger()

        private const val FROM = "spring assistant"
        private const val PATH = "/api/v1/change/password?token="
    }

    @Value("\${spring.mail.username}")
    private lateinit var emailFrom: String

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

        val context = Context().apply {
            setVariable("sendPath", sendPath)
        }

        val mimeMessage = mailSender.createMimeMessage()

        val messageHelper = MimeMessageHelper(mimeMessage, true).apply {
            setFrom(emailFrom, FROM)
            setTo(email)
            addInline("image", File("/src/main/kotlin/resources/static/forgotPasswordImage.png"))
            setSubject("Password recovery email")
            setText(templateEngine.process("email", context), true)
        }

        try {
            mailSender.send(mimeMessage)
        }
        catch (ex: MailException) {
            logger.info("Email could not be sent, because error occurred")
            throw ex
        }
        catch (ex: Exception) {
            throw ex
        }
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