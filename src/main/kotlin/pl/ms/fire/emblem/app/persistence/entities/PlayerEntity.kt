package pl.ms.fire.emblem.app.persistence.entities

import javax.persistence.*

@Entity
@Table(name = "players")
class PlayerEntity(

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "player_id", insertable = true, nullable = false, updatable = true)
    val id: Int,

    @Column(name = "username", insertable = true, nullable = false, updatable = true)
    val username: String,

    @Column(name = "password", insertable = true, nullable = false, updatable = true)
    val password: String,

    @Column(name = "email", insertable = true, nullable = false, updatable = true, unique = true)
    val email: String,

    @Column(name = "game_token", insertable = true, nullable = true, updatable = true)
    val gameToken: String?,

    @Column(name = "current_preset", insertable = true, nullable = false, updatable = true)
    val currentPreset: Int,

    @OneToMany(mappedBy = "player")
    @JoinColumn(name = "player_id")
    val presets: Set<PlayerPreset>,

    @ElementCollection
    @CollectionTable(name = "player_roles", joinColumns = [JoinColumn(name = "player_id")])
    @Column(name = "role")
    val roles: Set<String>,

) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PlayerEntity

        if (id != other.id) return false
        if (email != other.email) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + email.hashCode()
        return result
    }
}