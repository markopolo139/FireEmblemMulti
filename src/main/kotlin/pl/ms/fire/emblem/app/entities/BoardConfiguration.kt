package pl.ms.fire.emblem.app.entities

class BoardConfiguration(
    val opponentId: Int, val height: Int, val width: Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BoardConfiguration

        if (opponentId != other.opponentId) return false
        if (height != other.height) return false
        if (width != other.width) return false

        return true
    }

    override fun hashCode(): Int {
        var result = opponentId
        result = 31 * result + height
        result = 31 * result + width
        return result
    }
}