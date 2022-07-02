package pl.ms.fire.emblem.business.values.board

import pl.ms.fire.emblem.business.utlis.Displayable

enum class Terrain(dmgReduction: Int, movementReduction: Int): Displayable {
    GRASS(0,0),
    PLAIN(0,0),
    FORREST(2,2),
    MOUNTAIN(3,99),
    WATER(0,99);

    override val displayName: String
        get() = name
}