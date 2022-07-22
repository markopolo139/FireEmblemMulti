package pl.ms.fire.emblem.business.serices.battle

import pl.ms.fire.emblem.business.entities.CharacterPair

class MissBattleCalculator: BattleCalculator {
    override fun calcDmg(playerPair: CharacterPair, enemyPair: CharacterPair): Int {
        return -1000
    }
}