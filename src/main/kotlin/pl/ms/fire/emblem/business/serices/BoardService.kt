package pl.ms.fire.emblem.business.serices

import pl.ms.fire.emblem.business.entities.CharacterPair
import pl.ms.fire.emblem.business.entities.GameBoard
import pl.ms.fire.emblem.business.exceptions.CharacterMovedException
import pl.ms.fire.emblem.business.exceptions.battle.NotAllowedWeaponCategoryException
import pl.ms.fire.emblem.business.exceptions.battle.OutOfRangeException
import pl.ms.fire.emblem.business.exceptions.board.NotEnoughMovementException
import pl.ms.fire.emblem.business.exceptions.board.PairOnRouteException
import pl.ms.fire.emblem.business.exceptions.board.RouteNotConstantException
import pl.ms.fire.emblem.business.exceptions.character.NoCharacterOnSpotException
import pl.ms.fire.emblem.business.exceptions.character.PairAlreadyOnSpotException
import pl.ms.fire.emblem.business.exceptions.item.ItemDoesNotExistsException
import pl.ms.fire.emblem.business.exceptions.item.NotStaffException
import pl.ms.fire.emblem.business.utlis.getStat
import pl.ms.fire.emblem.business.values.GameCharacter
import pl.ms.fire.emblem.business.values.board.Position
import pl.ms.fire.emblem.business.values.board.Spot
import pl.ms.fire.emblem.business.values.category.WeaponCategory
import pl.ms.fire.emblem.business.values.character.Stat
import java.util.LinkedList

class BoardService {
    fun movePair(pairSpot: Spot, route: LinkedHashSet<Position>, gameBoard: GameBoard): List<Spot> {

        if (pairSpot.standingCharacter == null)
            throw NoCharacterOnSpotException()

        if (pairSpot.standingCharacter?.leadCharacter?.moved == true)
            throw CharacterMovedException()

        routeValidation(pairSpot, mapPositionRouteToSpotRoute(route, gameBoard))

        val destination = gameBoard.getSpot(route.last())
        destination.standingCharacter = pairSpot.standingCharacter
        pairSpot.standingCharacter = null

        return listOf(pairSpot, destination)
    }

    fun waitTurn(playerCharacter: GameCharacter) {
        playerCharacter.moved = true
    }

    fun staffHeal(staffUserSpot: Spot, healedSpot: Spot) {

        if (staffUserSpot.standingCharacter == null || healedSpot.standingCharacter == null)
            throw NoCharacterOnSpotException()

        if (staffUserSpot.standingCharacter?.leadCharacter?.moved == true)
            throw CharacterMovedException()

        val staffUser = staffUserSpot.standingCharacter!!.leadCharacter
        val staff = staffUser.equipment.getOrNull(staffUser.currentEquippedItem) ?: throw ItemDoesNotExistsException()

        if (staff.weaponCategory != WeaponCategory.STAFF)
            throw NotStaffException()

        if (!staffUser.characterClass.allowedWeapons.contains(WeaponCategory.STAFF))
            throw NotAllowedWeaponCategoryException(staff.weaponCategory)

        if (Position.checkAbsolutePosition(staffUserSpot.position, healedSpot.position) > staff.range)
            throw OutOfRangeException()

        val healedCharacter = healedSpot.standingCharacter!!.leadCharacter

        if ((healedCharacter.remainingHealth + staff.mt) > healedCharacter.combinedStat.getStat(Stat.HEALTH)) {
            healedCharacter.remainingHealth = healedCharacter.combinedStat.getStat(Stat.HEALTH)
        }
        else {
            healedCharacter.remainingHealth = healedCharacter.remainingHealth + staff.mt
        }

        staffUser.moved = true
    }

    private fun routeValidation(pairSpot: Spot, route: Collection<Spot>) {

        if (pairSpot == route.last()) return

        if (route.last().standingCharacter != null)
            throw PairAlreadyOnSpotException()

        var movementLeft = pairSpot.standingCharacter!!.leadCharacter.characterClass.movement
        var lastPosition = pairSpot.position

        for (spot in route) {
            movementLeft -= spot.terrain.movementReduction

            if (movementLeft < 0)
                throw NotEnoughMovementException()

            if (spot.standingCharacter != null)
                throw PairOnRouteException()

            if (Position.checkAbsolutePosition(spot.position, lastPosition) != 1)
                throw RouteNotConstantException()

            lastPosition = spot.position
        }
    }

    private fun mapPositionRouteToSpotRoute(route: Collection<Position>, gameBoard: GameBoard): List<Spot> =
        route.map { gameBoard.getSpot(it) }.toList()
}