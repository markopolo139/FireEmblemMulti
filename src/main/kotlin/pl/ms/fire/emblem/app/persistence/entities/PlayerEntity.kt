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
    var gameToken: String?,

    @Column(name = "current_preset", insertable = true, nullable = false, updatable = true)
    var currentPreset: Int,

    @OneToMany(mappedBy = "player", orphanRemoval = true, cascade = [CascadeType.ALL])
    val presets: MutableSet<PlayerPresetEntity>,

    @ElementCollection
    @CollectionTable(name = "player_roles", joinColumns = [JoinColumn(name = "player_id")])
    @Column(name = "role")
    val roles: MutableSet<String>,

    ) {

    fun addPreset(preset: PlayerPresetEntity) {
        presets.add(preset)
        preset.player = this
    }

    fun addPresetList(presets: Set<PlayerPresetEntity>) =
        presets.forEach { addPreset(it) }

    fun deletePreset(preset: PlayerPresetEntity) {
        presets.remove(preset)
        preset.player = null
    }

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