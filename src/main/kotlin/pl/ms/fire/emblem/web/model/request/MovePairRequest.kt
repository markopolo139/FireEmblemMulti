package pl.ms.fire.emblem.web.model.request

import javax.validation.Valid

class MovePairRequest(
    @field:Valid val startingPositionModel: PositionModel,
    val route: List<@Valid PositionModel>
)