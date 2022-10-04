package pl.ms.fire.emblem.web.model.request

import javax.validation.Valid

class TwoPositionRequest(
    @field:Valid val position1: PositionModel,
    @field:Valid val position2: PositionModel
)