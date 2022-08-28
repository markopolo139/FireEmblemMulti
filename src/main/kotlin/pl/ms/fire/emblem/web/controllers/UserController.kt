package pl.ms.fire.emblem.web.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import pl.ms.fire.emblem.app.services.UserService
import pl.ms.fire.emblem.web.model.request.RegistryModel
import javax.validation.Valid
import javax.validation.constraints.Email

@RestController
@Validated
@CrossOrigin
class UserController {

    @Autowired
    private lateinit var userService: UserService

    @PostMapping("/api/v1/user/create")
    fun createUser(@RequestBody registryModel: RegistryModel) {
        userService.createPlayer(
            registryModel.email, registryModel.username, registryModel.password
        )
    }

    @PostMapping("/api/v1/user/delete")
    fun createUser() {
        userService.deleteUser()
    }

    @PostMapping("/api/v1/user/delete")
    fun createUser(@RequestParam @Valid @Email email: String) {
        userService.deleteUser(email)
    }

}