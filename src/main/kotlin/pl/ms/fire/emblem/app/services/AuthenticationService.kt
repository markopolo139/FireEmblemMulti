package pl.ms.fire.emblem.app.services

import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service
import pl.ms.fire.emblem.app.configuration.security.TokenService
import pl.ms.fire.emblem.app.configuration.security.UserEntity

@Service
class AuthenticationService {

    companion object {
        private val logger = LogManager.getLogger()
    }

    @Autowired
    private lateinit var tokenService: TokenService

    @Autowired
    private lateinit var authenticationManager: AuthenticationManager

    fun authenticate(username: String?, password: String?): String = try {
        val auth = authenticationManager.authenticate(UsernamePasswordAuthenticationToken(username, password))
        tokenService.createAuthenticationToken((auth.principal as UserEntity).id)
    } catch(ex: BadCredentialsException) {
        logger.error("Attempt of obtaining token with invalid credentials has been made for username $username")
        throw ex
    }

}