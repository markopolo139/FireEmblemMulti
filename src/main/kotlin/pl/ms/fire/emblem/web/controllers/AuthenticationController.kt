package pl.ms.fire.emblem.web.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import pl.ms.fire.emblem.app.services.AuthenticationService

@RestController
@CrossOrigin
class AuthenticationController {

    @Autowired
    private lateinit var authenticationService: AuthenticationService

    @GetMapping("/auth")
    fun auth(@RequestParam("login") login: String, @RequestParam("password") password: String): String {
        return authenticationService.authenticate(login, password)
    }

}