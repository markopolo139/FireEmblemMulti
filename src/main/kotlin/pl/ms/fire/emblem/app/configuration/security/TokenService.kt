package pl.ms.fire.emblem.app.configuration.security

import io.jsonwebtoken.*
import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import org.springframework.http.HttpHeaders
import pl.ms.fire.emblem.app.entities.BoardConfiguration
import java.security.SignatureException
import java.util.*
import javax.servlet.http.HttpServletRequest

@Service
class TokenService {

    companion object {
        private val logger = LogManager.getLogger()
        private const val DEFAULT_HEIGHT = 10
        private const val DEFAULT_WIDTH = 10
    }

    @Autowired
    private lateinit var jwtConf: JwtConf

    @Value("\${api.auth.token.join.game.height}")
    private lateinit var heightValue: String
    private var _height: Int? = null
    private val height: Int
        get() {
            if (_height == null) initHeightValue(heightValue)
            return _height!!
        }

    @Value("\${api.auth.token.join.game.width}")
    private lateinit var widthValue: String
    private var _width: Int? = null
    private val width: Int
        get() {
            if (_width == null) initWidthValue(widthValue)
            return _width!!
        }

    enum class Claims(val value: String) {
        PASSWORD_RECOVERY("pwr"),
        JOIN_GAME("jg"),
        BOARD_HEIGHT("bh"),
        BOARD_WIDTH("bw"),
    }

    fun createAuthenticationToken(userId: Int): String =
        Jwts
            .builder()
            .configure(jwtConf.validity)
            .setSubject(userId.toString())
            .compact()

    fun createPasswordRecoveryToken(userId: Int): String =
        Jwts
            .builder()
            .configure(jwtConf.passwordRecoveryValidity)
            .setSubject(userId.toString())
            .claim(Claims.PASSWORD_RECOVERY.value, true)
            .compact()

    fun createJoinGameToken(userId: Int, height: Int, width: Int): String =
        Jwts
            .builder()
            .configure(jwtConf.joinGameValidity)
            .setSubject(userId.toString())
            .claim(Claims.JOIN_GAME.value, true)
            .claim(Claims.BOARD_WIDTH.value, width)
            .claim(Claims.BOARD_HEIGHT.value, height)
            .compact()

    fun createRandomJoinGameToken(userId: Int): String =
        Jwts
            .builder()
            .configure(jwtConf.joinGameValidity)
            .setSubject(userId.toString())
            .claim(Claims.JOIN_GAME.value, true)
            .claim(Claims.BOARD_WIDTH.value, width)
            .claim(Claims.BOARD_HEIGHT.value, height)
            .compact()

    fun extractIdFromToken(): Int? {

        val token = extractTokenFromRequest(currentRequest!!) ?: return null

        try {
            return Jwts
                .parserBuilder()
                .configure()
                .build()
                .parseClaimsJws(token)
                .body.subject.toInt()
        }
        catch (e: ExpiredJwtException) {
            logger.debug("Token authentication failed due to expired token")
        } catch (e: SignatureException) {
            logger.warn("Token authentication failed due invalid signature")
        } catch (e: JwtException) {
            logger.debug("Token authentication failed due to exception: $e")
        }

        return null
    }

    fun extractIdFromToken(token: String?): Int? {

        if (token == null) return null

        try {
            return Jwts
                .parserBuilder()
                .configure()
                .build()
                .parseClaimsJws(token)
                .body.subject.toInt()
        }
        catch (e: ExpiredJwtException) {
            logger.debug("Token authentication failed due to expired token")
            throw e
        } catch (e: SignatureException) {
            logger.warn("Token authentication failed due invalid signature")
            throw e
        } catch (e: JwtException) {
            logger.debug("Token authentication failed due to exception: $e")
            throw e
        }
    }

    fun isPasswordRecoveryToken(): Boolean {
        val token = extractTokenFromRequest(currentRequest!!) ?: return false

        try {
            return Jwts
                .parserBuilder()
                .configure()
                .build()
                .parseClaimsJws(token)
                .body.get(Claims.PASSWORD_RECOVERY.value, Boolean::class.javaObjectType) ?: false
        }
        catch (e: ExpiredJwtException) {
            logger.debug("Token authentication failed due to expired token")
            throw e
        } catch (e: SignatureException) {
            logger.warn("Token authentication failed due invalid signature")
            throw e
        } catch (e: JwtException) {
            logger.debug("Token authentication failed due to exception: $e")
            throw e
        }
    }

    fun getBoardConfigurationFromToken(token: String): BoardConfiguration? {
        val tokenBody =
            try {
                Jwts.parserBuilder().configure().build().parseClaimsJws(token).body
            } catch (e: ExpiredJwtException) {
                logger.debug("Token authentication failed due to expired token")
                return null
            } catch (e: SignatureException) {
                logger.warn("Token authentication failed due invalid signature")
                return null
            } catch (e: JwtException) {
                logger.debug("Token authentication failed due to exception: $e")
                return null
            }

        return BoardConfiguration(
            tokenBody.subject.toInt(), tokenBody.get(Claims.BOARD_HEIGHT.value, Int::class.javaObjectType),
            tokenBody.get(Claims.BOARD_WIDTH.value, Int::class.javaObjectType)
        )
    }

    private val currentRequest: HttpServletRequest?
        get() = ((RequestContextHolder.getRequestAttributes()) as? ServletRequestAttributes)?.request

    private fun extractTokenFromRequest(request: HttpServletRequest): String? {
        val authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION) ?: return null

        return if (authorizationHeader.startsWith("Bearer ")) authorizationHeader.substring(7) else null
    }

    private fun initHeightValue(value: String) {
        try {
            _height = value.toInt()
            return
        }
        catch(e: Exception) {
            logger.debug("Can't find height value using default")
        }
        _height = DEFAULT_HEIGHT
    }

    private fun initWidthValue(value: String) {
        try {
            _width = value.toInt()
            return
        }
        catch(e: Exception) {
            logger.debug("Can't find width value using default")
        }
        _width = DEFAULT_WIDTH
    }

    private fun JwtBuilder.configure(validity: Long): JwtBuilder {
        setIssuer(jwtConf.issuer)
        setAudience(jwtConf.audience)
        setExpiration(Date(Date().time + validity))
        signWith(jwtConf.key)
        return this
    }

    private fun JwtParserBuilder.configure(): JwtParserBuilder {
        requireIssuer(jwtConf.issuer)
        requireAudience(jwtConf.audience)
        setSigningKey(jwtConf.key)
        return this
    }

}