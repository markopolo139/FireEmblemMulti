package pl.ms.fire.emblem.app.persistence.embeddable

import pl.ms.fire.emblem.business.values.character.Stat
import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.EnumType
import javax.persistence.Enumerated

@Embeddable
class StatEmbeddable(
    @Column(name = "stat", nullable = false, insertable = true, updatable = true) @Enumerated(EnumType.STRING)
    val stat: Stat,

    @Column(name = "`value`", nullable = false, insertable = true, updatable = true)
    val value: Int
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as StatEmbeddable

        if (stat != other.stat) return false
        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        var result = stat.hashCode()
        result = 31 * result + value
        return result
    }
}