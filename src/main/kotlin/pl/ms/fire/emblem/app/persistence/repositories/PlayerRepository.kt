package pl.ms.fire.emblem.app.persistence.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import pl.ms.fire.emblem.app.persistence.entities.PlayerEntity
import java.util.*
import org.springframework.transaction.annotation.Transactional

@Repository
interface PlayerRepository: JpaRepository<PlayerEntity, Int> {

    fun findByUsername(username: String): Optional<PlayerEntity>

    fun findByEmail(email: String): Optional<PlayerEntity>

    fun findByGameToken(gameToken: String): Optional<PlayerEntity>

    fun findUsernameByUserId(userId: Int): String

    @Query("select p from PlayerEntity p left join fetch p.presets where p.id = :playerId")
    fun joinFetchPresets(@Param("playerId") playerId: Int): PlayerEntity

    @Modifying
    @Transactional
    fun deleteByEmail(email: String)

}