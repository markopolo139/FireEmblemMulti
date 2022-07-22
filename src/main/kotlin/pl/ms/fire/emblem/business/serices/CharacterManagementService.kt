package pl.ms.fire.emblem.business.serices

import pl.ms.fire.emblem.business.exceptions.character.NoCharacterOnSpotException
import pl.ms.fire.emblem.business.exceptions.character.SeparatePairException
import pl.ms.fire.emblem.business.values.board.Spot

class CharacterManagementService {

    fun joinIntoPair(characterSpot: Spot, joinWithSpot: Spot) {
        if (characterSpot.standingCharacter == null || joinWithSpot.standingCharacter == null)
            throw NoCharacterOnSpotException()

        characterSpot.standingCharacter =
            characterSpot.standingCharacter!!.joinWithAnotherCharacter(joinWithSpot.standingCharacter!!)

        joinWithSpot.standingCharacter = null
    }

    fun changeSupport(characterSpot: Spot) {
        if (characterSpot.standingCharacter == null)
            throw NoCharacterOnSpotException()
        characterSpot.standingCharacter!!.changeWithSupport()
    }

    fun separatePair(characterSpot: Spot, separateToSpot: Spot) {
        if (characterSpot.standingCharacter == null)
            throw NoCharacterOnSpotException()

        if (separateToSpot.standingCharacter != null)
            throw SeparatePairException()

        separateToSpot.standingCharacter = characterSpot.standingCharacter!!.separatePair()
        characterSpot.standingCharacter!!.leadCharacter.moved = true
        separateToSpot.standingCharacter!!.leadCharacter.moved = true

    }

    fun tradeSupport(characterSpot: Spot, tradeWithSpot: Spot) {
        if (characterSpot.standingCharacter == null || tradeWithSpot.standingCharacter == null)
            throw NoCharacterOnSpotException()

        characterSpot.standingCharacter!!.tradeSupportCharacter(tradeWithSpot.standingCharacter!!)

        characterSpot.standingCharacter!!.leadCharacter.moved = true
        tradeWithSpot.standingCharacter!!.leadCharacter.moved = true
    }

}