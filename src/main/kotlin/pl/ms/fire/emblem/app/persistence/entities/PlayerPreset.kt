package pl.ms.fire.emblem.app.persistence.entities

import javax.persistence.*

@Entity
@Table(name = "player_character_presets")
class PlayerPreset(

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "preset_id", insertable = true, nullable = false, updatable = true)
    val id: Int,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    val player: PlayerEntity,

    @OneToMany(mappedBy = "preset")
    val gameCharacters: Set<GameCharacterEntity>
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PlayerPreset

        if (id != other.id) return false
        if (player != other.player) return false
        if (gameCharacters != other.gameCharacters) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + player.hashCode()
        result = 31 * result + gameCharacters.hashCode()
        return result
    }
}