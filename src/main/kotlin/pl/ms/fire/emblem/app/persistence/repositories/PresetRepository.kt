package pl.ms.fire.emblem.app.persistence.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import pl.ms.fire.emblem.app.persistence.entities.PlayerPresetEntity

@Repository
interface PresetRepository: JpaRepository<PlayerPresetEntity, Int> {

    @Query("select p from PlayerPresetEntity p left join fetch p.gameCharacters where p.player.id = :playerId")
    fun joinFetchByPlayerId(@Param("playerId") playerId: Int): Set<PlayerPresetEntity>

    @Modifying
    @Transactional
    fun deleteByPlayerId(id: Int)

}