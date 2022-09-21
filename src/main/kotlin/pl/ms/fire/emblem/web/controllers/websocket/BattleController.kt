package pl.ms.fire.emblem.web.controllers.websocket

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.stereotype.Controller
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import pl.ms.fire.emblem.app.services.BattleInteractor
import pl.ms.fire.emblem.web.model.request.TwoPositionRequest
import pl.ms.fire.emblem.web.model.toBusiness
import javax.validation.Valid

@Controller
class BattleController {

    @Autowired
    private lateinit var battleInteractor: BattleInteractor

    @MessageMapping("/battle")
    fun battle(@Valid twoPositionRequest: TwoPositionRequest) {
        battleInteractor.battle(
            twoPositionRequest.positionModel1.toBusiness(), twoPositionRequest.positionModel2.toBusiness()
        )
    }

    @MessageMapping("/battle/forecast")
    fun battleForecast(@Valid twoPositionRequest: TwoPositionRequest) {
        battleInteractor.battleForecast(
            twoPositionRequest.positionModel1.toBusiness(), twoPositionRequest.positionModel2.toBusiness()
        )
    }
}