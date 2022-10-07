package pl.ms.fire.emblem.web.model.request

import pl.ms.fire.emblem.business.values.board.Position
import javax.validation.Valid
import javax.validation.constraints.Min

class TradeItemModel(
    @field:Valid val pairPosition: PositionModel,
    @field:Min(0) val itemId: Int,
    @field:Valid val tradeWithPosition: PositionModel,
    @field:Min(0) val tradeItemId: Int?
)