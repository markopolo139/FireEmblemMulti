package pl.ms.fire.emblem.app.persistence.entities

import pl.ms.fire.emblem.business.values.board.Terrain
import javax.persistence.*

@Entity
@Table(name = "spots")
class SpotEntity(

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "spot_id", insertable = true, nullable = false, updatable = true)
    val id: Int,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false, insertable = true, updatable = true)
    val board: BoardEntity?,

    @Column(name = "x", insertable = true, nullable = false, updatable = true)
    val x: Int,

    @Column(name = "y", insertable = true, nullable = false, updatable = true)
    val y: Int,

    @Enumerated(EnumType.STRING)
    @Column(name = "terrain", insertable = true, nullable = false, updatable = true)
    val terrain: Terrain,

    @OneToOne(mappedBy = "spot")
    val characterPair: CharacterPairEntity?

) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SpotEntity

        if (id != other.id) return false
        if (board != other.board) return false
        if (x != other.x) return false
        if (y != other.y) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + board.hashCode()
        result = 31 * result + x
        result = 31 * result + y
        return result
    }
}