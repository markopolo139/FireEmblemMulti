package pl.ms.fire.emblem.app.persistence.entities

import javax.persistence.*

@Entity
@Table(name = "boards")
class BoardEntity(

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "board_id", insertable = true, nullable = false, updatable = true)
    val id: Int,

    @Column(name = "width", insertable = true, nullable = false, updatable = true)
    val width: Int,

    @Column(name = "height", insertable = true, nullable = false, updatable = true)
    val height: Int,

    @OneToOne(orphanRemoval = false)
    @JoinColumn(name = "player_a_id", nullable = false, insertable = true, updatable = true)
    val playerA: PlayerEntity,

    @OneToOne(orphanRemoval = false)
    @JoinColumn(name = "player_b_id", nullable = false, insertable = true, updatable = true)
    val playerB: PlayerEntity,

    @OneToOne(orphanRemoval = false)
    @JoinColumn(name = "current_player_id", nullable = false, insertable = true, updatable = true)
    val currentPlayer: PlayerEntity,

) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BoardEntity

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id
    }
}