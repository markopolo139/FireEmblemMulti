package pl.ms.fire.emblem.business.entities

import pl.ms.fire.emblem.business.exceptions.NoSupportCharacterException
import pl.ms.fire.emblem.business.exceptions.PairAlreadyHaveSupportException
import pl.ms.fire.emblem.business.utlis.getStat
import pl.ms.fire.emblem.business.values.GameCharacter
import pl.ms.fire.emblem.business.values.character.BattleStat
import pl.ms.fire.emblem.business.values.character.Stat

class CharacterPair(
    var leadCharacter: GameCharacter,
    var supportCharacter: GameCharacter?,
) {
    val battleStat: BattleStat = BattleStat(0,0,0,0)
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
        boostedStats = leadCharacter.combineClassStatsWithCharacterStats()

        if (supportCharacter != null) {
            val supCharStats = supportCharacter!!.combineClassStatsWithCharacterStats().filter { it.key != Stat.HEALTH }
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

        return CharacterPair(leadCharacter, characterPair.leadCharacter)
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

    fun deadOfLeadCharacter() =
        if (supportCharacter == null) null else CharacterPair(supportCharacter as GameCharacter, null)
}