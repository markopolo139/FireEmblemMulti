package pl.ms.fire.emblem.business.serices.battle

import pl.ms.fire.emblem.business.entities.CharacterPair

interface BattleCalculator {
    fun calcDmg(playerPair: CharacterPair, enemyPair: CharacterPair, randomNumber: Int): Int
}