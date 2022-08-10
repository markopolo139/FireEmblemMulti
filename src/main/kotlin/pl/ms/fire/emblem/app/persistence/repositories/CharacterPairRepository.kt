package pl.ms.fire.emblem.app.persistence.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import pl.ms.fire.emblem.app.persistence.entities.CharacterPairEntity

@Repository
interface CharacterPairRepository: JpaRepository<CharacterPairEntity, Int> {

    @Query(
        "select * from character_pairs where lead_character_id in (" +
                "select game_character_id from game_characters where" +
                "preset_id in (select preset_id from player_character_presets where player_id = :playerId)" +
        ")", nativeQuery = true
    )
    fun getAllPlayerCharacters(@Param("playerId") playerId: Int): Set<CharacterPairEntity>

}