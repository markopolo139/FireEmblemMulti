package pl.ms.fire.emblem.web.controllers.websocket

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.stereotype.Controller
import pl.ms.fire.emblem.app.services.CharacterManagementInteractor
import pl.ms.fire.emblem.web.model.request.PositionModel
import pl.ms.fire.emblem.web.model.request.TwoPositionRequest
import pl.ms.fire.emblem.web.model.toBusiness
import javax.validation.Valid

@Controller
class CharacterManagementControllerWS {

    @Autowired
    private lateinit var characterInteractor: CharacterManagementInteractor

    @MessageMapping("/pair/join")
    fun joinIntoPair(@Valid twoPositionRequest: TwoPositionRequest) =
        characterInteractor.joinIntoPair(twoPositionRequest.position1.toBusiness(), twoPositionRequest.position2.toBusiness())

    @MessageMapping("/pair/support/change")
    fun changeSupport( @Valid pairPosition: PositionModel) =
        characterInteractor.changeSupport(pairPosition.toBusiness())

    @MessageMapping("/pair/separate")
    fun separatePair(@Valid twoPositionRequest: TwoPositionRequest) =
        characterInteractor.separatePair(twoPositionRequest.position1.toBusiness(), twoPositionRequest.position2.toBusiness())

    @MessageMapping("/pair/support/trade")
    fun tradeSupport(@Valid twoPositionRequest: TwoPositionRequest) =
        characterInteractor.tradeSupport(twoPositionRequest.position1.toBusiness(), twoPositionRequest.position2.toBusiness())

}