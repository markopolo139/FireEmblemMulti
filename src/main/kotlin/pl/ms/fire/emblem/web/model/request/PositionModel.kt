package pl.ms.fire.emblem.web.model.request

import javax.validation.constraints.Min

class PositionModel(
    @field:Min(0) val x: Int,
    @field:Min(0) val y: Int
)