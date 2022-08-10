package pl.ms.fire.emblem.app.persistence.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import pl.ms.fire.emblem.app.persistence.entities.GameCharacterEntity

@Repository
interface GameCharacterRepository: JpaRepository<GameCharacterEntity, Int> {

    fun getAllByPresetId(presetId: Int): Set<GameCharacterEntity>

}