package pl.ms.fire.emblem.app.persistence.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import pl.ms.fire.emblem.app.persistence.entities.PlayerPresetEntity

@Repository
interface PresetRepository: JpaRepository<PlayerPresetEntity, Int> {

    fun getAllByPlayerId(id: Int): Set<PlayerPresetEntity>

    @Modifying
    @Transactional
    fun deleteByPlayerId(id: Int)

}