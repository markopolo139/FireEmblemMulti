package pl.ms.fire.emblem.app.configuration.security

import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import javax.crypto.SecretKey

@Component
class JwtConf {

    companion object {
        private val logger = LogManager.getLogger()

        const val DEFAULT_VALIDITY: Long = 900000
        const val DEFAULT_PASSWORD_RECOVERY_VALIDITY: Long = 180000
        const val DEFAULT_JOIN_GAME_VALIDITY: Long = 540000
    }

    @Value("\${api.auth.token.issuer}")
    lateinit var issuer: String

    @Value("\${api.auth.token.audience}")
    lateinit var audience: String

    @Value("\${api.auth.token.secret}")
    private lateinit var secret: String

    private var _key: SecretKey? = null

    val key: SecretKey
        get() {
            if (_key == null) initKey()
            return _key!!
        }

    @Value("\${api.auth.token.validity}")
    lateinit var validityValue: String

    private var _validity: Long? = null

    val validity: Long
        get() {
            if (_validity == null) initValidity()
            return _validity!!
        }

    @Value("\${api.auth.token.password.recovery.validity}")
    lateinit var passwordRecoveryValidityValue: String

    private var _passwordRecoveryValidity: Long? = null

    val passwordRecoveryValidity: Long
        get() {
            if (_passwordRecoveryValidity == null) initPasswordRecoveryValidity()
            return _passwordRecoveryValidity!!
        }

    @Value("\${api.auth.token.join.game.validity}")
    lateinit var joinGameValidityValue: String

    private var _joinGameValidity: Long? = null

    val joinGameValidity: Long
        get() {
            if (_joinGameValidity == null) initJoinGameValidity()
            return _joinGameValidity!!
        }

    private fun initKey() {
        _key = if (secret.isBlank()) Keys.secretKeyFor(SignatureAlgorithm.HS512)
               else Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret))
    }

    private fun initValidity() {
        try {
            _validity = validityValue.toLong()
            return
        }
        catch (ex: Exception) {
            logger.warn("Validity value not found, using default")
        }
        _validity = DEFAULT_VALIDITY
    }

    private fun initPasswordRecoveryValidity() {
        try {
            _passwordRecoveryValidity = passwordRecoveryValidityValue.toLong()
            return
        }
        catch (ex: Exception) {
            logger.warn("Password recovery validity value not found, using default")
        }
        _passwordRecoveryValidity = DEFAULT_PASSWORD_RECOVERY_VALIDITY
    }

    private fun initJoinGameValidity() {
        try {
            _joinGameValidity = joinGameValidityValue.toLong()
            return
        }
        catch (ex: Exception) {
            logger.warn("Join game validity value not found, using default")
        }
        _joinGameValidity = DEFAULT_JOIN_GAME_VALIDITY
    }

}