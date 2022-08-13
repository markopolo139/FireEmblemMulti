package pl.ms.fire.emblem.web.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import pl.ms.fire.emblem.app.recovery.PasswordRecoveryService
import pl.ms.fire.emblem.app.services.AuthenticationService
import pl.ms.fire.emblem.web.utils.getServerAddress
import javax.servlet.http.HttpServletRequest
import kotlin.math.log

@RestController
@CrossOrigin
class Test {


    @Autowired
    private lateinit var authenticationService: AuthenticationService

    @Autowired
    private lateinit var passwordRecoveryService: PasswordRecoveryService

    @GetMapping("/api/v1/test")
    fun test(httpServletRequest: HttpServletRequest, @RequestParam("email") email: String) {
        passwordRecoveryService.sendMessage(email, getServerAddress(httpServletRequest))
    }

    @GetMapping("/auth")
    fun auth(@RequestParam("login") login: String, @RequestParam("password") password: String): String {
        return authenticationService.authenticate(login, password)
    }

}