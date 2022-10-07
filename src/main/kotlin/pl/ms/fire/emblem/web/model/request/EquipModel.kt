package pl.ms.fire.emblem.web.model.request

import pl.ms.fire.emblem.business.serices.EquipmentManagementService
import javax.validation.Valid
import javax.validation.constraints.Max
import javax.validation.constraints.Min

class EquipModel(
    @field:Valid val positionModel: PositionModel,
    @field:Min(0) @field:Max((EquipmentManagementService.EQUIPMENT_LIMIT - 1).toLong()) val itemId: Int
)