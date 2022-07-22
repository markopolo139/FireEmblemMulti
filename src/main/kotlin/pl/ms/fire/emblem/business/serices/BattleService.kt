package pl.ms.fire.emblem.business.serices

import pl.ms.fire.emblem.business.exceptions.battle.OutOfRangeException
import pl.ms.fire.emblem.business.exceptions.character.NoCharacterOnSpotException
import pl.ms.fire.emblem.business.exceptions.item.NoItemEquippedException
import pl.ms.fire.emblem.business.serices.battle.BattleCalculator
import pl.ms.fire.emblem.business.serices.battle.DefaultBattleCalculator
import pl.ms.fire.emblem.business.serices.battle.MissBattleCalculator
import pl.ms.fire.emblem.business.utlis.BattleUtils
import pl.ms.fire.emblem.business.utlis.getStat
import pl.ms.fire.emblem.business.values.battle.WeaponTriangle
import pl.ms.fire.emblem.business.values.board.Position
import pl.ms.fire.emblem.business.values.board.Spot
import pl.ms.fire.emblem.business.values.character.DefensiveSkill
import pl.ms.fire.emblem.business.values.character.OffensiveSkill

class BattleService {

    fun battle(attacker: Spot, defender: Spot) {
        preBattleValidation(attacker, defender)
        val attackerWeaponTriangle = checkWeaponTriangle(attacker, defender)
        var calculator = setUpBattleCalculator(attacker, defender, attackerWeaponTriangle)
        var dmgDealt = calculator.calcDmg(attacker.standingCharacter!!, defender.standingCharacter!!)
        setHealth(defender, dmgDealt, attackerWeaponTriangle)

        if (defender.standingCharacter!!.leadCharacter.remainingHealth <= 0) {
            defender.standingCharacter = defender.standingCharacter!!.deadOfLeadCharacter()
            attacker.standingCharacter!!.leadCharacter.moved = true
            return
        }

        try {
            preBattleValidation(defender, attacker)
            val defenderWeaponTriangle = checkWeaponTriangle(defender, attacker)
            calculator = setUpBattleCalculator(defender, attacker, defenderWeaponTriangle)
            dmgDealt = calculator.calcDmg(defender.standingCharacter!!, attacker.standingCharacter!!)
            setHealth(attacker, dmgDealt, defenderWeaponTriangle)
        }
        catch (ex: Exception) {
            when(ex) {
                is NoItemEquippedException, is OutOfRangeException -> Unit
                else -> throw ex
            }
        }

        if (attacker.standingCharacter!!.leadCharacter.remainingHealth <= 0) {
            attacker.standingCharacter = attacker.standingCharacter!!.deadOfLeadCharacter()
            return
        }

        if(BattleUtils.isDoubleAttack(attacker.standingCharacter!!, defender.standingCharacter!!)) {
            calculator = setUpBattleCalculator(attacker, defender, attackerWeaponTriangle)
            dmgDealt = calculator.calcDmg(attacker.standingCharacter!!, defender.standingCharacter!!)
            setHealth(defender, dmgDealt, attackerWeaponTriangle)

            if (defender.standingCharacter!!.leadCharacter.remainingHealth <= 0)
                defender.standingCharacter = defender.standingCharacter!!.deadOfLeadCharacter()
        }

        attacker.standingCharacter!!.leadCharacter.moved = true

    }

    private fun preBattleValidation(attacker: Spot, defender: Spot) {
        if(attacker.standingCharacter == null)
            throw NoCharacterOnSpotException()

        if (defender.standingCharacter == null)
            throw NoCharacterOnSpotException()

        val standingPair = attacker.standingCharacter!!

        val equippedWeapon = standingPair.leadCharacter.equipment.getOrNull(standingPair.leadCharacter.currentEquippedItem)
            ?: throw NoItemEquippedException()

        if (Position.checkAbsolutePosition(attacker.position, defender.position) > equippedWeapon.range)
            throw OutOfRangeException()

    }

    private fun checkWeaponTriangle(attacker: Spot, defender: Spot): WeaponTriangle {
        val attackerWeapon =
            attacker.standingCharacter!!.leadCharacter.equipment[attacker.standingCharacter!!.leadCharacter.currentEquippedItem]

        val defenderWeapon =
            defender.standingCharacter!!.leadCharacter.equipment.getOrNull(defender.standingCharacter!!.leadCharacter.currentEquippedItem)
                ?: return WeaponTriangle.NORMAL

        return if (attackerWeapon.weaponCategory.superiorCategory == defenderWeapon.weaponCategory) {
            WeaponTriangle.ADVANTAGE
        } else {
            if (attackerWeapon.weaponCategory.weakCategory == defenderWeapon.weaponCategory) WeaponTriangle.DISADVANTAGE
            else WeaponTriangle.NORMAL
        }

    }

    private fun setUpBattleCalculator(attacker: Spot, defender: Spot, weaponTriangle: WeaponTriangle): BattleCalculator {

        val attackerPair = attacker.standingCharacter!!
        val defenderPair = defender.standingCharacter!!

        if (
            !BattleUtils.hitCheck(
                attackerPair.battleStat.hitRate + weaponTriangle.hitRateModifier,
                defenderPair.battleStat.avoid + defender.terrain.avoidBoost
            )
        ) return MissBattleCalculator()

        for (skill in attackerPair.leadCharacter.characterClass.skills)
            if (
                BattleUtils.skillCheck(
                    skill.percentCalc.invoke(attackerPair.boostedStats.getStat(skill.statUsedToCalculation))
                ) && skill is OffensiveSkill
            ) return skill

        return DefaultBattleCalculator()
    }

    private fun setHealth(defender: Spot, dmgDealt: Int, weaponTriangle: WeaponTriangle) {
        var trueDmgDealt = if (dmgDealt - defender.terrain.dmgReduction + weaponTriangle.dmgDealtModifier <= 0) return
        else dmgDealt - defender.terrain.dmgReduction + weaponTriangle.dmgDealtModifier

        val defenderPair = defender.standingCharacter!!
        for (skill in defenderPair.leadCharacter.characterClass.skills)
            if (
                BattleUtils.skillCheck(
                    skill.percentCalc.invoke(defenderPair.boostedStats.getStat(skill.statUsedToCalculation))
                ) && skill is DefensiveSkill
            ) trueDmgDealt = skill.dmgModifier(trueDmgDealt, defenderPair.leadCharacter.remainingHealth)

        defender.standingCharacter!!.leadCharacter.remainingHealth -= trueDmgDealt
    }
}