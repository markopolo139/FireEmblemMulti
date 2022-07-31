package pl.ms.fire.emblem.app.persistence.entities

import pl.ms.fire.emblem.app.persistence.embeddable.ItemEmbeddable
import pl.ms.fire.emblem.app.persistence.embeddable.StatEmbeddable
import pl.ms.fire.emblem.business.values.character.CharacterClass
import javax.persistence.*

@Entity
@Table(name = "game_characters")
class GameCharacterEntity(

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "game_character_id", insertable = true, nullable = false, updatable = true)
    val id: Int,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "preset_id", nullable = false)
    val preset: PlayerPresetEntity,

    @Column(name = "name", insertable = true, nullable = false, updatable = true)
    val name: String,

    @Column(name = "remaining_hp", insertable = true, nullable = false, updatable = true)
    val remainingHp: Int,

    @Column(name = "current_equipped_item", insertable = true, nullable = false, updatable = true)
    val currentEquippedItem: Int,

    @Column(name = "character_class", insertable = true, nullable = false, updatable = true)
    @Enumerated(EnumType.STRING)
    val characterClass: CharacterClass,

    @Column(name = "moved", insertable = true, nullable = false, updatable = true)
    val moved: Boolean,

    @ElementCollection
    @CollectionTable(name = "character_stats", joinColumns = [JoinColumn(name = "game_character_id")])
    val stats: Set<StatEmbeddable>,

    @ElementCollection
    @CollectionTable(name = "character_items", joinColumns = [JoinColumn(name = "game_character_id")])
    val items: Set<ItemEmbeddable>

) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GameCharacterEntity

        if (id != other.id) return false
        if (preset != other.preset) return false
        if (name != other.name) return false
        if (remainingHp != other.remainingHp) return false
        if (currentEquippedItem != other.currentEquippedItem) return false
        if (characterClass != other.characterClass) return false
        if (moved != other.moved) return false
        if (stats != other.stats) return false
        if (items != other.items) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + preset.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + remainingHp
        result = 31 * result + currentEquippedItem
        result = 31 * result + characterClass.hashCode()
        result = 31 * result + moved.hashCode()
        result = 31 * result + stats.hashCode()
        result = 31 * result + items.hashCode()
        return result
    }
}