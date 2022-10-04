package pl.ms.fire.emblem.web.controllers.rest

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import pl.ms.fire.emblem.app.services.CharacterManagementInteractor
import pl.ms.fire.emblem.web.model.request.PositionModel
import pl.ms.fire.emblem.web.model.request.TwoPositionRequest
import pl.ms.fire.emblem.web.model.toBusiness
import javax.validation.Valid

@RestController
@Validated
@CrossOrigin
class CharacterManagementController {

    @Autowired
    private lateinit var characterInteractor: CharacterManagementInteractor

    @PostMapping("/api/v1/pair/join")
    fun joinIntoPair(@RequestBody @Valid twoPositionRequest: TwoPositionRequest) =
        characterInteractor.joinIntoPair(twoPositionRequest.position1.toBusiness(), twoPositionRequest.position2.toBusiness())

    @PostMapping("/api/v1/pair/support/change")
    fun changeSupport(@RequestBody @Valid pairPosition: PositionModel) =
        characterInteractor.changeSupport(pairPosition.toBusiness())

    @PostMapping("/api/v1/pair/separate")
    fun separatePair(@RequestBody @Valid twoPositionRequest: TwoPositionRequest) =
        characterInteractor.separatePair(twoPositionRequest.position1.toBusiness(), twoPositionRequest.position2.toBusiness())

    @PostMapping("/api/v1/pair/support/trade")
    fun tradeSupport(@RequestBody @Valid twoPositionRequest: TwoPositionRequest) =
        characterInteractor.tradeSupport(twoPositionRequest.position1.toBusiness(), twoPositionRequest.position2.toBusiness())
}