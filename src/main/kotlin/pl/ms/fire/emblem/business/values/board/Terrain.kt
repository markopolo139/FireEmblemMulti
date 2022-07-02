package pl.ms.fire.emblem.business.values.board

import pl.ms.fire.emblem.business.utlis.Displayable

enum class Terrain(dmgReduction: Int, movementReduction: Int): Displayable {
    GRASS(0,1),
    PLAIN(0,1),
    FORREST(2,2),
    MOUNTAIN(3,4),
    FORTRESS(5, 3),
    SAND(0, 3);

    override val displayName: String
        get() = name
}