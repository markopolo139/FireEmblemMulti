package pl.ms.fire.emblem.app.persistence

import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import pl.ms.fire.emblem.app.persistence.embeddable.ItemEmbeddable
import pl.ms.fire.emblem.app.persistence.embeddable.StatEmbeddable
import pl.ms.fire.emblem.app.persistence.entities.*
import pl.ms.fire.emblem.app.persistence.repositories.*
import pl.ms.fire.emblem.business.values.board.Terrain
import pl.ms.fire.emblem.business.values.category.AttackCategory
import pl.ms.fire.emblem.business.values.category.WeaponCategory
import pl.ms.fire.emblem.business.values.character.CharacterClass
import pl.ms.fire.emblem.business.values.character.Stat

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@SpringBootTest
class PersistenceGeneralTest {

    @Autowired
    private lateinit var boardRepository: BoardRepository

    @Autowired
    private lateinit var characterPairRepository: CharacterPairRepository

    @Autowired
    private lateinit var gameCharacterRepository: GameCharacterRepository

    @Autowired
    private lateinit var playerRepository: PlayerRepository

    @Autowired
    private lateinit var presetRepository: PresetRepository

    @Autowired
    private lateinit var spotRepository: SpotRepository

    @Test
    @Order(1)
    fun `save player`() {
        var player = PlayerEntity(
            0, "test1", "zsk", "test1@test.pl", null, 0, mutableSetOf(), mutableSetOf("USER")
        )

        playerRepository.save(
            PlayerEntity(
                0, "test2", "zsk", "test2@test.pl", null, 0, mutableSetOf(), mutableSetOf("USER")
            )
        )

        playerRepository.save(
            PlayerEntity(
                0, "test3", "zsk", "test3@test.pl", null, 0, mutableSetOf(), mutableSetOf("USER")
            )
        )

        player = playerRepository.save(player)

        Assertions.assertNotEquals(0, player.id)
    }

    @Test
    @Order(2)
    fun `preset with characters`() {
        var player = playerRepository.findByEmail("test1@test.pl").get()
        var player2 = playerRepository.findByEmail("test2@test.pl").get()

        val presets = mutableSetOf(
            PlayerPresetEntity(0, player, mutableSetOf()),
            PlayerPresetEntity(1, player, mutableSetOf()),
        )

        val presets2 = mutableSetOf(
            PlayerPresetEntity(0, player, mutableSetOf()),
        )

        val gameCharacter1 = GameCharacterEntity(
            0, null, "character1", 1, 0, CharacterClass.GENERAL,
            false, mutableSetOf(StatEmbeddable(Stat.DEFENSE, 0), StatEmbeddable(Stat.HEALTH, 11)),
            mutableSetOf(ItemEmbeddable("weapon", 1, 2, 3, 1, AttackCategory.MAGICAL, WeaponCategory.AXE, 1))
        )

        presets.first().addCharacter(gameCharacter1)

        val gameCharacter2 = GameCharacterEntity(
            0, null, "character2", 1, 0, CharacterClass.GENERAL,
            false, mutableSetOf(StatEmbeddable(Stat.DEFENSE, 0), StatEmbeddable(Stat.HEALTH, 11)),
            mutableSetOf(ItemEmbeddable("weapon", 1, 2, 3, 1, AttackCategory.MAGICAL, WeaponCategory.AXE, 1))
        )

        val gameCharacter3 = GameCharacterEntity(
            0, null, "character3", 1, 0, CharacterClass.GENERAL,
            false, mutableSetOf(StatEmbeddable(Stat.DEFENSE, 0), StatEmbeddable(Stat.HEALTH, 11)),
            mutableSetOf(ItemEmbeddable("weapon", 1, 2, 3, 1, AttackCategory.MAGICAL, WeaponCategory.AXE, 1))
        )

        val gameCharacter4 = GameCharacterEntity(
            0, null, "character4", 1, 0, CharacterClass.GENERAL,
            false, mutableSetOf(StatEmbeddable(Stat.DEFENSE, 0), StatEmbeddable(Stat.HEALTH, 11)),
            mutableSetOf(ItemEmbeddable("weapon", 1, 2, 3, 1, AttackCategory.MAGICAL, WeaponCategory.AXE, 1))
        )

        val gameCharacter5 = GameCharacterEntity(
            0, null, "character5", 1, 0, CharacterClass.GENERAL,
            false, mutableSetOf(StatEmbeddable(Stat.DEFENSE, 0), StatEmbeddable(Stat.HEALTH, 11)),
            mutableSetOf(ItemEmbeddable("weapon", 1, 2, 3, 1, AttackCategory.MAGICAL, WeaponCategory.AXE, 1))
        )

        presets.last().addCharacterList(setOf(gameCharacter2, gameCharacter3, gameCharacter5))
        presets2.last().addCharacterList(setOf(gameCharacter4))

        player = playerRepository.joinFetchPresets(player.id)
        player2 = playerRepository.joinFetchPresets(player2.id)

        player.addPresetList(presets)
        player2.addPresetList(presets2)

        player = playerRepository.save(player)
        playerRepository.save(player2)

        Assertions.assertFalse(playerRepository.joinFetchPresets(player.id).presets.isEmpty())

        player.deletePreset(player.presets.last())
        playerRepository.save(player)

        player = playerRepository.joinFetchPresets(player.id)

        Assertions.assertEquals(1, player.presets.size)
        Assertions.assertNotNull(player.presets.elementAt(0).gameCharacters.elementAt(0))
    }

    @Test
    @Order(3)
    fun `test reading character from preset`() {
        Assertions.assertNotNull(
            playerRepository.joinFetchPresets(playerRepository.findByEmail("test1@test.pl").get().id).presets.first().gameCharacters.first()
        )
    }

    @Test
    @Order(4)
    fun `test saving gameCharacter with changed remaining hp`() {
        val player2 = playerRepository.joinFetchPresets(playerRepository.findByEmail("test2@test.pl").get().id)
        val gc = player2.presets.first().gameCharacters.first()
        gc.preset?.player?.presets?.clear()
        gc.preset?.player?.roles?.clear()
        gc.preset?.gameCharacters?.clear()
        val gameCharacterChanged = GameCharacterEntity(
            gc.id, gc.preset, gc.name, gc.remainingHp + 1, gc.currentEquippedItem, gc.characterClass, gc.moved, gc.stats, gc.items
        )

        gameCharacterRepository.save(gameCharacterChanged)
    }

    @Test
    @Order(5)
    fun `test deleting player3`() {
        val player3 = playerRepository.findByEmail("test3@test.pl").get()

        playerRepository.deleteById(player3.id)

        Assertions.assertTrue(playerRepository.findByEmail("test3@test.pl").isEmpty)
    }

    @Test
    @Order(6)
    fun `board and spots test`() {
        var board = BoardEntity(
            0, 10, 10, playerRepository.findByEmail("test1@test.pl").get(),
            playerRepository.findByEmail("test2@test.pl").get(), playerRepository.findByEmail("test1@test.pl").get(), mutableSetOf()
        )

        board = boardRepository.save(board)

        board.spots.addAll(
            mutableSetOf(
                SpotEntity(0, board, 1,2, Terrain.FORREST, null),
                SpotEntity(0, board, 1,3, Terrain.FORTRESS, null),
                SpotEntity(0, board, 1,4, Terrain.GRASS, null),
                SpotEntity(0, board, 1,5, Terrain.PLAIN, null),
                SpotEntity(0, board, 1,6, Terrain.SAND, null)
            )
        )

        boardRepository.save(board)

    }

    @Test
    @Order(7)
    fun `character pair test`() {

        val playerA = playerRepository.joinFetchPresets(playerRepository.findByEmail("test1@test.pl").get().id)
        val currentPresetA = playerA.presets.elementAt(playerA.currentPreset)

        val playerB = playerRepository.joinFetchPresets(playerRepository.findByEmail("test2@test.pl").get().id)
        val currentPresetB = playerB.presets.elementAt(playerB.currentPreset)

        val board = boardRepository.joinFetchSpots(boardRepository.findByPlayerId(playerA.id).get().id)

        val spotA = SpotEntity(0, board, 5, 1, Terrain.SAND, null)
        val characterPairA = CharacterPairEntity(
            0, currentPresetA.gameCharacters.first(), currentPresetA.gameCharacters.last(), spotA
        )
        spotA.characterPair = characterPairA

        val characterPairB = CharacterPairEntity(
            0, currentPresetB.gameCharacters.first(), null, board.spots.first()
        )
        board.spots.first().characterPair = characterPairB

        val characterPairC = CharacterPairEntity(
            0, currentPresetA.gameCharacters.last(), null, board.spots.last()
        )
        board.spots.last().characterPair = characterPairC

        board.spots.add(spotA)

        boardRepository.save(board)

        characterPairRepository.getAllPlayerCharacters(playerA.id).forEach {
            println(
                it.leadCharacter.id
            )
        }

    }

    @Test
    @Order(8)
    fun `clean up`() {
        boardRepository.deleteAll()
        playerRepository.deleteByEmail("test1@test.pl")
        playerRepository.deleteByEmail("test2@test.pl")
        playerRepository.deleteByEmail("test3@test.pl")
    }
}