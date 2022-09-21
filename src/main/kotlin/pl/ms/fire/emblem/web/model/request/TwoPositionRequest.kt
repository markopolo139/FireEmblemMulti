package pl.ms.fire.emblem.web.model.request

import javax.validation.Valid

class TwoPositionRequest(
    @field:Valid val positionModel1: PositionModel,
    @field:Valid val positionModel2: PositionModel
)