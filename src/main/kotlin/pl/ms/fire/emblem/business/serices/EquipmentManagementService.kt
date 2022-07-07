package pl.ms.fire.emblem.business.serices

import pl.ms.fire.emblem.business.exceptions.*
import pl.ms.fire.emblem.business.values.GameCharacter
import pl.ms.fire.emblem.business.values.board.Spot
import pl.ms.fire.emblem.business.values.items.Item

class EquipmentManagementService {

    companion object {
        const val EQUIPMENT_LIMIT = 6
    }

    fun tradeEquipment(
        characterSpot: Spot, tradeWithSpot: Spot, characterTradeItemIndex: Int, tradeWithEquipmentIndex: Int?
    ) {
        if (characterSpot.standingCharacter == null || tradeWithSpot.standingCharacter == null)
            throw NoCharacterOnSpotException()

        val character = characterSpot.standingCharacter!!.leadCharacter
        val tradeWithCharacter = tradeWithSpot.standingCharacter!!.leadCharacter

        if (character.currentEquippedItem == characterTradeItemIndex)
            throw TradeEquippedItemException()

        if (tradeWithEquipmentIndex == null) {
            if (tradeWithCharacter.equipment.size >= EQUIPMENT_LIMIT)
                throw EquipmentLimitExceededException()

            tradeWithCharacter.equipment.add(
                character.equipment.getOrNull(characterTradeItemIndex) ?: throw ItemDoesNotExistsException()
            )

        }
        else {
            if (tradeWithCharacter.currentEquippedItem == tradeWithEquipmentIndex)
                throw TradeEquippedItemException()

            val tempItem = character.equipment.getOrNull(characterTradeItemIndex) ?: throw ItemDoesNotExistsException()

            character.equipment.add(
                characterTradeItemIndex,
                tradeWithCharacter.equipment.getOrNull(tradeWithEquipmentIndex) ?: throw ItemDoesNotExistsException()
            )

            tradeWithCharacter.equipment.add(
                tradeWithEquipmentIndex, tempItem
            )

        }
    }

    fun equipItem(characterSpot: Spot, equipItemIndex: Int) {
        if (characterSpot.standingCharacter == null)
            throw NoCharacterOnSpotException()

        val character = characterSpot.standingCharacter!!.leadCharacter

        validateItemToEquip(character, character.equipment[equipItemIndex])

        character.currentEquippedItem = equipItemIndex
    }

    private fun validateItemToEquip(character: GameCharacter, equipItem: Item?) {

        if (equipItem == null)
            throw ItemDoesNotExistsException()

        if(!character.characterClass.allowedWeapons.contains(equipItem.weaponCategory))
            throw WeaponNotAllowedException()
    }

}