package pl.ms.fire.emblem.app.persistence.entities

import javax.persistence.*

@Entity
@Table(name = "random_players")
class RandomPlayer(
    @Id @GeneratedValue(strategy = GenerationType.AUTO) @Column(name = "random_id") val id: Int,
    @Column(name = "token", nullable = false) val token: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RandomPlayer

        if (id != other.id) return false
        if (token != other.token) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + token.hashCode()
        return result
    }
}