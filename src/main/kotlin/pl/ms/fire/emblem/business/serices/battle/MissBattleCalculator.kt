package pl.ms.fire.emblem.business.serices.battle

import pl.ms.fire.emblem.business.entities.CharacterPair
import pl.ms.fire.emblem.business.utlis.Displayable

class MissBattleCalculator: BattleCalculator, Displayable {
    override fun calcDmg(playerPair: CharacterPair, enemyPair: CharacterPair): Int {
        return -1000
    }

    override val displayName: String
        get() = "Miss"
}