package pl.ms.fire.emblem.web.controllers.websocket

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.stereotype.Controller
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import pl.ms.fire.emblem.app.services.BoardInteractor
import pl.ms.fire.emblem.web.model.request.MovePairRequest
import pl.ms.fire.emblem.web.model.request.PositionModel
import pl.ms.fire.emblem.web.model.request.TwoPositionRequest
import pl.ms.fire.emblem.web.model.response.SpotResponse
import pl.ms.fire.emblem.web.model.toBusiness
import javax.validation.Valid

@Controller
class BoardController {

    @Autowired
    private lateinit var boardInteractor: BoardInteractor

    @MessageMapping("/board/move/pair")
    fun movePair(@Valid movePairRequest: MovePairRequest) {
        boardInteractor.movePair(
            movePairRequest.startingPositionModel.toBusiness(),
            movePairRequest.route.map { it.toBusiness() }
        )
    }

    @MessageMapping("/board/turn/wait")
    fun waitTurn(@Valid positionModel: PositionModel) = boardInteractor.waitTurn(positionModel.toBusiness())

    @MessageMapping("/board/turn/end")
    fun endTurn() = boardInteractor.endTurn()

    @MessageMapping("/board/heal/staff")
    fun staffHeal(@Valid twoPositionRequest: TwoPositionRequest) =
        boardInteractor.staffHeal(twoPositionRequest.positionModel1.toBusiness(), twoPositionRequest.positionModel2.toBusiness())

}