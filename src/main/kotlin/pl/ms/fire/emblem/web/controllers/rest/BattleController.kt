package pl.ms.fire.emblem.web.controllers.rest

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import pl.ms.fire.emblem.app.services.BattleInteractor
import pl.ms.fire.emblem.web.model.request.TwoPositionRequest
import pl.ms.fire.emblem.web.model.toBusiness
import javax.validation.Valid

@RestController
@Validated
@CrossOrigin
class BattleController {

    @Autowired
    private lateinit var battleInteractor: BattleInteractor

    @PostMapping("/api/v1/battle")
    fun battle(@RequestBody @Valid twoPositionRequest: TwoPositionRequest) =
        battleInteractor.battle(
            twoPositionRequest.positionModel1.toBusiness(), twoPositionRequest.positionModel2.toBusiness()
        ).map { it.displayName }

    @PostMapping("/api/v1/battle/forecast")
    fun battleForecast(@RequestBody @Valid twoPositionRequest: TwoPositionRequest) =
        battleInteractor.battleForecast(
            twoPositionRequest.positionModel1.toBusiness(), twoPositionRequest.positionModel2.toBusiness()
        )
}