package pl.ms.fire.emblem.web.controllers.websocket

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.stereotype.Controller
import pl.ms.fire.emblem.app.services.BattleInteractor
import pl.ms.fire.emblem.web.model.request.TwoPositionRequest
import pl.ms.fire.emblem.web.model.toBusiness
import javax.validation.Valid

@Controller
class BattleControllerWS {

    @Autowired
    private lateinit var battleInteractor: BattleInteractor

    @MessageMapping("/battle")
    fun battle(@Valid twoPositionRequest: TwoPositionRequest) {
        battleInteractor.battle(
            twoPositionRequest.position1.toBusiness(), twoPositionRequest.position2.toBusiness()
        )
    }

    @MessageMapping("/battle/forecast")
    fun battleForecast(@Valid twoPositionRequest: TwoPositionRequest) {
        battleInteractor.battleForecast(
            twoPositionRequest.position1.toBusiness(), twoPositionRequest.position2.toBusiness()
        )
    }
}