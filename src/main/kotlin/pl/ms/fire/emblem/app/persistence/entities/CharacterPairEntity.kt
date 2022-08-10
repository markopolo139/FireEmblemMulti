package pl.ms.fire.emblem.app.persistence.entities

import javax.persistence.*

@Entity
@Table(name = "character_pairs")
class CharacterPairEntity(

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "pair_id", insertable = true, nullable = false, updatable = true)
    val id: Int,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lead_character_id", nullable = false)
    val leadCharacter: GameCharacterEntity,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "support_character_id", nullable = true)
    val supportCharacter: GameCharacterEntity?,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spot_id", nullable = true)
    val spot: SpotEntity?

) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CharacterPairEntity

        if (id != other.id) return false
        if (leadCharacter != other.leadCharacter) return false
        if (supportCharacter != other.supportCharacter) return false
        if (spot != other.spot) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + leadCharacter.hashCode()
        result = 31 * result + (supportCharacter?.hashCode() ?: 0)
        result = 31 * result + (spot?.hashCode() ?: 0)
        return result
    }
}