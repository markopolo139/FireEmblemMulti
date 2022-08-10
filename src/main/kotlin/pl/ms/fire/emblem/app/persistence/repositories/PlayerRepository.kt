package pl.ms.fire.emblem.app.persistence.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.stereotype.Repository
import pl.ms.fire.emblem.app.persistence.entities.PlayerEntity
import java.util.*
import org.springframework.transaction.annotation.Transactional

@Repository
interface PlayerRepository: JpaRepository<PlayerEntity, Int> {

    fun findByUsername(username: String): Optional<PlayerEntity>

    fun findByEmail(email: String): Optional<PlayerEntity>

    fun findByGameToken(gameToken: String): Optional<PlayerEntity>

    @Modifying
    @Transactional
    fun deleteByEmail(email: String)

}