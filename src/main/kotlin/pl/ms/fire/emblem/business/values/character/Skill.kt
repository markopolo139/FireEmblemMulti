package pl.ms.fire.emblem.business.values.character

import pl.ms.fire.emblem.business.entities.CharacterPair
import pl.ms.fire.emblem.business.serices.battle.BattleCalculator
import pl.ms.fire.emblem.business.utlis.Displayable

interface Skill: Displayable {
    val statUsedToCalculation: Stat
    val priority: Int
    val percentCalc: (Int) -> (Int)
}