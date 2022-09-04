package pl.ms.fire.emblem.business.serices

import pl.ms.fire.emblem.business.exceptions.CharacterMovedException
import pl.ms.fire.emblem.business.exceptions.item.EquipmentLimitExceededException
import pl.ms.fire.emblem.business.exceptions.item.ItemDoesNotExistsException
import pl.ms.fire.emblem.business.exceptions.item.TradeEquippedItemException
import pl.ms.fire.emblem.business.exceptions.item.WeaponNotAllowedException
import pl.ms.fire.emblem.business.values.GameCharacter
import pl.ms.fire.emblem.business.values.items.Item

class EquipmentManagementService {

    companion object {
        const val EQUIPMENT_LIMIT = 6
    }

    fun tradeEquipment(
        character: GameCharacter, tradeWithCharacter: GameCharacter, characterTradeItemIndex: Int, tradeWithEquipmentIndex: Int?
    ) {

        if (character.moved) throw CharacterMovedException()

        if (character.currentEquippedItem == characterTradeItemIndex)
            throw TradeEquippedItemException()

        if (tradeWithEquipmentIndex == null) {
            if (tradeWithCharacter.equipment.size >= EQUIPMENT_LIMIT)
                throw EquipmentLimitExceededException()

            tradeWithCharacter.equipment.add(
                character.equipment.getOrNull(characterTradeItemIndex) ?: throw ItemDoesNotExistsException()
            )

            character.equipment.removeAt(characterTradeItemIndex)

        }
        else {
            if (tradeWithCharacter.currentEquippedItem == tradeWithEquipmentIndex)
                throw TradeEquippedItemException()

            val tempItem = character.equipment.getOrNull(characterTradeItemIndex) ?: throw ItemDoesNotExistsException()

            character.equipment.removeAt(characterTradeItemIndex)
            character.equipment.add(
                characterTradeItemIndex,
                tradeWithCharacter.equipment.getOrNull(tradeWithEquipmentIndex) ?: throw ItemDoesNotExistsException()
            )

            tradeWithCharacter.equipment.removeAt(tradeWithEquipmentIndex)
            tradeWithCharacter.equipment.add(tradeWithEquipmentIndex, tempItem)

        }

        character.moved = true
    }

    fun equipItem(character: GameCharacter, equipItemIndex: Int) {

        if (character.moved) throw CharacterMovedException()

        validateItemToEquip(character, character.equipment.getOrNull(equipItemIndex))

        character.currentEquippedItem = equipItemIndex
    }

    private fun validateItemToEquip(character: GameCharacter, equipItem: Item?) {

        if (equipItem == null)
            throw ItemDoesNotExistsException()

        if(!character.characterClass.allowedWeapons.contains(equipItem.weaponCategory))
            throw WeaponNotAllowedException()
    }

}