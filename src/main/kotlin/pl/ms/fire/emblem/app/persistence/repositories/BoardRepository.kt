package pl.ms.fire.emblem.app.persistence.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import pl.ms.fire.emblem.app.persistence.entities.BoardEntity
import pl.ms.fire.emblem.app.persistence.entities.PlayerEntity
import java.util.*

@Repository
interface BoardRepository: JpaRepository<BoardEntity, Int> {

    @Query("select be from BoardEntity be where be.playerA.id = :playerId or be.playerB.id = :playerId")
    fun findByPlayerId(@Param("playerId") playerId: Int): Optional<BoardEntity>

    @Query("select b from BoardEntity b left join fetch b.boards where b.id = :boardId")
    fun joinFetchSpots(@Param("boardId") boardId: Int): BoardEntity
}