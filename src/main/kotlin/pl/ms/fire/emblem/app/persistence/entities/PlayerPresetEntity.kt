package pl.ms.fire.emblem.app.persistence.entities

import javax.persistence.*
@Entity
@Table(name = "player_character_presets")
class PlayerPresetEntity(

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "preset_id", insertable = true, nullable = false, updatable = true)
    val id: Int,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    var player: PlayerEntity?,

    @OneToMany(mappedBy = "preset", orphanRemoval = true, cascade = [CascadeType.ALL])
    val gameCharacters: MutableSet<GameCharacterEntity>

) {

    fun addCharacter(gameCharacterEntity: GameCharacterEntity) {
        gameCharacters.add(gameCharacterEntity)
        gameCharacterEntity.preset = this
    }

    fun addCharacterList(gameCharacters: Set<GameCharacterEntity>) =
        gameCharacters.forEach { addCharacter(it) }

    fun removeCharacter(gameCharacterEntity: GameCharacterEntity) {
        gameCharacters.remove(gameCharacterEntity)
        gameCharacterEntity.preset = null
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PlayerPresetEntity

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