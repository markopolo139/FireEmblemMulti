package pl.ms.fire.emblem.web.controllers

import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import pl.ms.fire.emblem.web.utils.getServerAddress
import javax.servlet.http.HttpServletRequest

@RestController
@CrossOrigin
class Test {

    @GetMapping("/api/v1/test")
    fun test(httpServletRequest: HttpServletRequest): String {
        return getServerAddress(httpServletRequest)
    }

}