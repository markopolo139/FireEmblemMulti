package pl.ms.fire.emblem.business.values.board

import pl.ms.fire.emblem.business.utlis.Displayable

enum class Terrain(
    val dmgReduction: Int,
    val avoidBoost: Int,
    val movementReduction: Int
): Displayable {
    GRASS(0,5,1),
    PLAIN(0,0,1),
    FORREST(2,30,2),
    MOUNTAIN(3,40,4),
    FORTRESS(5,20, 3),
    SAND(0,-10, 3);

    override val displayName: String
        get() = name
}