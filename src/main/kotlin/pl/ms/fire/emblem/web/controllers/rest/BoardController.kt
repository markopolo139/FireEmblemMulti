package pl.ms.fire.emblem.web.controllers.rest

import org.springframework.beans.factory.annotation.Autowired
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
import pl.ms.fire.emblem.web.model.toWebModel
import javax.validation.Valid

@RestController
@Validated
@CrossOrigin
class BoardController {

    @Autowired
    private lateinit var boardInteractor: BoardInteractor

    @PostMapping("/api/v1/board/move/pair")
    fun movePair(@RequestBody @Valid movePairRequest: MovePairRequest): List<SpotResponse> =
        boardInteractor.movePair(
            movePairRequest.startingPositionModel.toBusiness(),
            movePairRequest.route.map { it.toBusiness() }
        ).map { it.toWebModel() }

    @PostMapping("/api/v1/board/turn/wait")
    fun waitTurn(@RequestBody @Valid positionModel: PositionModel) =
        boardInteractor.waitTurn(positionModel.toBusiness())


    @PostMapping("/api/v1/board/turn/end")
    fun endTurn() = boardInteractor.endTurn()

    @PostMapping("/api/v1/board/heal/staff")
    fun staffHeal(@RequestBody @Valid twoPositionRequest: TwoPositionRequest) =
        boardInteractor.staffHeal(twoPositionRequest.position1.toBusiness(), twoPositionRequest.position2.toBusiness())

}