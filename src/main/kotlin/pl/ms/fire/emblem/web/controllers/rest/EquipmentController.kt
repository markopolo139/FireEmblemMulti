package pl.ms.fire.emblem.web.controllers.rest

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import pl.ms.fire.emblem.app.services.EquipmentManagementInteractor
import pl.ms.fire.emblem.web.model.request.EquipModel
import pl.ms.fire.emblem.web.model.request.PositionModel
import pl.ms.fire.emblem.web.model.request.TradeItemModel
import pl.ms.fire.emblem.web.model.toBusiness
import javax.validation.Valid

@RestController
@Validated
@CrossOrigin
class EquipmentController {

    @Autowired
    private lateinit var equipmentInteractor: EquipmentManagementInteractor

    @PostMapping("/api/v1/item/equip")
    fun equipItem(@RequestBody @Valid equipModel: EquipModel) =
        equipmentInteractor.equipItem(equipModel.positionModel.toBusiness(), equipModel.itemId)

    @PostMapping("/api/v1/item/trade")
    fun tradeItems(@RequestBody @Valid tradeItemModel: TradeItemModel) =
        equipmentInteractor.tradeItems(
            tradeItemModel.pairPosition.toBusiness(), tradeItemModel.itemId,
            tradeItemModel.tradeWithPosition.toBusiness(), tradeItemModel.tradeItemId
        )

}