package pl.ms.fire.emblem.business.values.board

import pl.ms.fire.emblem.business.entities.CharacterPair

class Spot(
    val position: Position,
    val terrain: Terrain,
    var standingCharacter: CharacterPair?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Spot

        if (position != other.position) return false
        if (terrain != other.terrain) return false
        if (standingCharacter != other.standingCharacter) return false

        return true
    }

    override fun hashCode(): Int {
        var result = position.hashCode()
        result = 31 * result + terrain.hashCode()
        result = 31 * result + (standingCharacter?.hashCode() ?: 0)
        return result
    }
}