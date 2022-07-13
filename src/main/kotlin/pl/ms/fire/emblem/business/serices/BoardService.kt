package pl.ms.fire.emblem.business.serices

import org.springframework.security.access.prepost.PostAuthorize
import pl.ms.fire.emblem.business.entities.CharacterPair
import pl.ms.fire.emblem.business.entities.GameBoard
import pl.ms.fire.emblem.business.exceptions.*
import pl.ms.fire.emblem.business.utlis.getStat
import pl.ms.fire.emblem.business.values.GameCharacter
import pl.ms.fire.emblem.business.values.board.Position
import pl.ms.fire.emblem.business.values.board.Spot
import pl.ms.fire.emblem.business.values.category.WeaponCategory
import pl.ms.fire.emblem.business.values.character.Stat

class BoardService {
    fun movePair(pairSpot: Spot, route: Collection<Position>, gameBoard: GameBoard): List<Spot> {

        if (pairSpot.standingCharacter == null)
            throw NoCharacterOnSpotException()

        routeValidation(pairSpot, mapPositionRouteToSpotRoute(route, gameBoard))

        val destination = gameBoard.getSpot(route.last())
        destination.standingCharacter = pairSpot.standingCharacter
        pairSpot.standingCharacter = null

        return listOf(pairSpot, destination)
    }

    fun startTurn(playerCharacters: Collection<CharacterPair>) {
        playerCharacters.forEach { it.leadCharacter.moved = false }
    }

    fun waitTurn(playerCharacter: GameCharacter) {
        playerCharacter.moved = true
    }

    fun staffHeal(staffUserSpot: Spot, healedSpot: Spot) {

        if (staffUserSpot.standingCharacter == null || healedSpot.standingCharacter == null)
            throw NoCharacterOnSpotException()

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

        if (route.last().standingCharacter != null)
            throw PairAlreadyOnSpotException()

        var movementLeft = pairSpot.standingCharacter!!.leadCharacter.characterClass.movement

        for (spot in route) {
            movementLeft -= spot.terrain.movementReduction

            if (movementLeft < 0)
                throw NotEnoughMovementException()

            if (spot.standingCharacter != null)
                throw PairOnRouteException()
        }
    }

    private fun mapPositionRouteToSpotRoute(route: Collection<Position>, gameBoard: GameBoard): List<Spot> =
        route.map { gameBoard.getSpot(it) }.toList()
}