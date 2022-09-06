package pl.ms.fire.emblem.web.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import pl.ms.fire.emblem.app.services.GameStatusService
import pl.ms.fire.emblem.web.model.response.SpotResponse
import pl.ms.fire.emblem.web.model.toWebModel

@RestController
@CrossOrigin
@Validated
class GameStatusController {

    @Autowired
    private lateinit var gameStatusService: GameStatusService

    @GetMapping("/api/v1/game/status/is/win")
    fun isWin(@RequestParam("opponent") opponentUsername: String): Boolean = gameStatusService.isWin(opponentUsername)

    @GetMapping("/api/v1/game/status/board")
    fun getCurrentBoard(): List<SpotResponse> = gameStatusService.getCurrentBoard().map { it.toWebModel() }

    @GetMapping("/api/v1/game/status/current/player")
    fun getCurrentPlayer(): String = gameStatusService.getCurrentPlayer()

    @GetMapping("/api/v1/game/status/alive/not/moved/characters")
    fun getAliveNotMovedPairs(): List<SpotResponse> = gameStatusService.getAliveNotMovedPairs().map { it.toWebModel() }

    @GetMapping("/api/v1/game/status/alive/characters")
    fun getAliveCharacters(): List<SpotResponse> = gameStatusService.getAliveCharacters().map { it.toWebModel() }

    @GetMapping("/api/v1/game/status/all/characters")
    fun getAllCharacters(): List<SpotResponse> = gameStatusService.getAllCharacters().map { it.toWebModel() }

    @GetMapping("/api/v1/game/status/enemy/alive/characters")
    fun getEnemyAliveCharacters(): List<SpotResponse> = gameStatusService.getEnemyAliveCharacters().map { it.toWebModel() }

}