package pl.ms.fire.emblem.business.serices

import pl.ms.fire.emblem.business.exceptions.CharacterMovedException
import pl.ms.fire.emblem.business.exceptions.battle.OutOfRangeException
import pl.ms.fire.emblem.business.exceptions.character.NoCharacterOnSpotException
import pl.ms.fire.emblem.business.exceptions.character.SeparatePairException
import pl.ms.fire.emblem.business.values.board.Position
import pl.ms.fire.emblem.business.values.board.Spot

class CharacterManagementService {

    fun joinIntoPair(characterSpot: Spot, joinToSpot: Spot) {
        if (characterSpot.standingCharacter == null || joinToSpot.standingCharacter == null)
            throw NoCharacterOnSpotException()

        if (characterSpot.standingCharacter?.leadCharacter?.moved == true)
            throw CharacterMovedException()

        joinToSpot.standingCharacter =
            joinToSpot.standingCharacter!!.joinWithAnotherCharacter(characterSpot.standingCharacter!!)

        characterSpot.standingCharacter = null
    }

    fun changeSupport(characterSpot: Spot) {
        if (characterSpot.standingCharacter == null)
            throw NoCharacterOnSpotException()

        if (characterSpot.standingCharacter?.leadCharacter?.moved == true)
            throw CharacterMovedException()

        characterSpot.standingCharacter!!.changeWithSupport()
    }

    fun separatePair(characterSpot: Spot, separateToSpot: Spot) {
        if (characterSpot.standingCharacter == null)
            throw NoCharacterOnSpotException()

        if (separateToSpot.standingCharacter != null)
            throw SeparatePairException()

        if (characterSpot.standingCharacter?.leadCharacter?.moved == true)
            throw CharacterMovedException()

        if (Position.checkAbsolutePosition(characterSpot.position, separateToSpot.position) != 1) {
            throw OutOfRangeException()
        }

        separateToSpot.standingCharacter = characterSpot.standingCharacter!!.separatePair()
        characterSpot.standingCharacter!!.leadCharacter.moved = true
        separateToSpot.standingCharacter!!.leadCharacter.moved = true

    }

    fun tradeSupport(characterSpot: Spot, tradeWithSpot: Spot) {
        if (characterSpot.standingCharacter == null || tradeWithSpot.standingCharacter == null)
            throw NoCharacterOnSpotException()

        if(characterSpot.standingCharacter?.leadCharacter?.moved == true)
            throw CharacterMovedException()

        characterSpot.standingCharacter!!.tradeSupportCharacter(tradeWithSpot.standingCharacter!!)
    }

}