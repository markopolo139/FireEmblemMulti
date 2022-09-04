package pl.ms.fire.emblem.app.persistence.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import pl.ms.fire.emblem.app.persistence.entities.SpotEntity
import java.util.Optional

@Repository
interface SpotRepository: JpaRepository<SpotEntity, Int> {

    fun getByBoardIdAndXAndY(boardId: Int, x: Int, y: Int): Optional<SpotEntity>

    fun getAllByBoardId(boardId: Int): Set<SpotEntity>

}