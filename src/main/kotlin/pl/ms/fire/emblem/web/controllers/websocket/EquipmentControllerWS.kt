package pl.ms.fire.emblem.web.controllers.websocket

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.stereotype.Controller
import pl.ms.fire.emblem.app.services.EquipmentManagementInteractor
import pl.ms.fire.emblem.web.model.request.EquipModel
import pl.ms.fire.emblem.web.model.request.TradeItemModel
import pl.ms.fire.emblem.web.model.toBusiness
import javax.validation.Valid

@Controller
class EquipmentControllerWS {

    @Autowired
    private lateinit var equipmentInteractor: EquipmentManagementInteractor

    @MessageMapping("/item/equip")
    fun equipItem(@Valid equipModel: EquipModel) =
        equipmentInteractor.equipItem(equipModel.positionModel.toBusiness(), equipModel.itemId)

    @MessageMapping("/item/trade")
    fun tradeItems(@Valid tradeItemModel: TradeItemModel) =
        equipmentInteractor.tradeItems(
            tradeItemModel.pairPosition.toBusiness(), tradeItemModel.itemId,
            tradeItemModel.tradeWithPosition.toBusiness(), tradeItemModel.tradeItemId
        )

}