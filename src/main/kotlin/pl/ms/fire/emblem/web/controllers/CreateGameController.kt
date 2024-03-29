package pl.ms.fire.emblem.web.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import pl.ms.fire.emblem.app.services.CreateGameService
import pl.ms.fire.emblem.business.values.board.Position

@RestController
@Validated
@CrossOrigin
class CreateGameController {

    @Autowired
    private lateinit var createGameService: CreateGameService

    @GetMapping("/api/v1/game/create")
    fun createToken(
        @RequestParam("height") height: Int,
        @RequestParam("width") width: Int
    ): String = createGameService.createJoinGameToken(height, width)

    @PostMapping("/api/v1/game/join")
    fun joinGame(
        @RequestParam("token") token: String,
    ) = createGameService.joinGame(token)

    @PostMapping("/api/v1/game/set/characters")
    fun setUpCharacters(
        @RequestBody characters: Map<Int, Position>
    ) = createGameService.setUpCharacters(characters)

    @PutMapping("/api/v1/game/exit")
    fun exitGame() {
        createGameService.exitGame()
    }

    @GetMapping("/api/v1/game/id")
    fun getBoardId() = createGameService.getBoardId()

    @GetMapping("/api/v1/game/configuration")
    fun getBoardConfiguration(@RequestParam("boardId") boardId: Int) = createGameService.getBoardConfiguration(boardId)

    @PostMapping("/api/v1/game/join/random")
    fun joinRandomGame() {
        createGameService.createJoinGameTokenForRandom()
    }
}