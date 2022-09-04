package pl.ms.fire.emblem.app.persistence.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import pl.ms.fire.emblem.app.persistence.entities.SpotEntity

@Repository
interface SpotRepository: JpaRepository<SpotEntity, Int> {

    fun getByBoardIdAndXAndY(boardId: Int, x: Int, y: Int): SpotEntity

    fun getAllByBoardId(boardId: Int): Set<SpotEntity>

}