package pl.ms.fire.emblem.business.entities

import pl.ms.fire.emblem.business.exceptions.character.NoSupportCharacterException
import pl.ms.fire.emblem.business.exceptions.character.PairAlreadyHaveSupportException
import pl.ms.fire.emblem.business.utlis.getStat
import pl.ms.fire.emblem.business.values.GameCharacter
import pl.ms.fire.emblem.business.values.character.BattleStat
import pl.ms.fire.emblem.business.values.character.Stat

open class CharacterPair(
    var leadCharacter: GameCharacter,
    var supportCharacter: GameCharacter?,
) {
    val battleStat: BattleStat = BattleStat(0,0,0,0,0)
    var boostedStats: MutableMap<Stat, Int> = mutableMapOf()

    init {
        updateBoostedStats()
        updateBattleStat()
    }

    fun updateBattleStat() {
        battleStat.updateBattleStats(this)

        if (supportCharacter != null) {
            battleStat.avoid += 10
            battleStat.critical += 5
            battleStat.hitRate += 10
        }
    }

    private fun updateBoostedStats() {
        boostedStats = leadCharacter.combinedStat

        if (supportCharacter != null) {
            val supCharStats = supportCharacter!!.combinedStat.filter { it.key != Stat.HEALTH }
                .mapValues {
                when (it.value) {
                    in 0..10 -> 2
                    in 11..20 -> 4
                    else -> 6
                }
            }

            boostedStats = boostedStats.mapValues { it.value + supCharStats.getStat(it.key)}.toMutableMap()
        }
    }

    fun changeWithSupport() {
        if (supportCharacter == null)
            throw NoSupportCharacterException()

        val tempCharacter = leadCharacter
        leadCharacter = supportCharacter as GameCharacter
        supportCharacter = tempCharacter

        updateBoostedStats()
        updateBattleStat()
    }

    fun separatePair(): CharacterPair {
        if (supportCharacter == null)
            throw NoSupportCharacterException()

        val result = CharacterPair(supportCharacter as GameCharacter, null)
        supportCharacter = null

        updateBoostedStats()
        updateBattleStat()

        return result
    }

    fun joinWithAnotherCharacter(characterPair: CharacterPair): CharacterPair {
        if (supportCharacter != null || characterPair.supportCharacter != null)
            throw PairAlreadyHaveSupportException()

        supportCharacter = characterPair.leadCharacter
        updateBoostedStats()
        updateBattleStat()

        return this
    }

    fun tradeSupportCharacter(characterPair: CharacterPair) {
        if(supportCharacter == null && characterPair.supportCharacter == null)
            throw NoSupportCharacterException()

        val temp = characterPair.supportCharacter
        characterPair.supportCharacter = supportCharacter
        supportCharacter = temp

        characterPair.updateBoostedStats()
        characterPair.updateBattleStat()

        updateBoostedStats()
        updateBattleStat()
    }

    fun deadOfLeadCharacter(): CharacterPair? {
        return if (supportCharacter != null) {
            leadCharacter = supportCharacter as GameCharacter
            supportCharacter = null
            updateBoostedStats()
            updateBattleStat()
            this
        }
        else {
            null
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CharacterPair

        if (leadCharacter != other.leadCharacter) return false
        if (supportCharacter != other.supportCharacter) return false
        if (battleStat != other.battleStat) return false
        if (boostedStats != other.boostedStats) return false

        return true
    }

    override fun hashCode(): Int {
        var result = leadCharacter.hashCode()
        result = 31 * result + (supportCharacter?.hashCode() ?: 0)
        result = 31 * result + battleStat.hashCode()
        result = 31 * result + boostedStats.hashCode()
        return result
    }


}