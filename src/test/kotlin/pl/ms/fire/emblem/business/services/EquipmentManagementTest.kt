package pl.ms.fire.emblem.business.services

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import pl.ms.fire.emblem.business.exceptions.item.EquipmentLimitExceededException
import pl.ms.fire.emblem.business.exceptions.item.ItemDoesNotExistsException
import pl.ms.fire.emblem.business.exceptions.item.TradeEquippedItemException
import pl.ms.fire.emblem.business.exceptions.item.WeaponNotAllowedException
import pl.ms.fire.emblem.business.serices.EquipmentManagementService
import pl.ms.fire.emblem.business.values.GameCharacter
import pl.ms.fire.emblem.business.values.category.AttackCategory
import pl.ms.fire.emblem.business.values.category.WeaponCategory
import pl.ms.fire.emblem.business.values.character.CharacterClass
import pl.ms.fire.emblem.business.values.character.Stat
import pl.ms.fire.emblem.business.values.items.Item

class EquipmentManagementTest {

    private val equipmentService = EquipmentManagementService()

    private val gameCharacter = GameCharacter(
        "Test", 30, 0,
        mutableListOf(
            Item("Sword", 5, 100, 10, 1, AttackCategory.PHYSICAL, WeaponCategory.SWORD),
            Item("Physic", 25, 100, 110, 1, AttackCategory.PHYSICAL, WeaponCategory.SWORD),
            Item("Magical", 5, 100, 5, 2, AttackCategory.MAGICAL, WeaponCategory.TOME),
            Item("Magical", 5, 100, 5, 2, AttackCategory.MAGICAL, WeaponCategory.STAFF),
            Item("Lance", 5, 100, 5, 2, AttackCategory.PHYSICAL, WeaponCategory.LANCE),
        ),
        mapOf(
            Stat.HEALTH to 30,
            Stat.STRENGTH to 10,
            Stat.MAGICK to 15,
            Stat.DEFENSE to 5,
            Stat.RESISTANCE to 5,
            Stat.SKILL to 10,
            Stat.LUCK to 18,
            Stat.SPEED to 12
        ),
        CharacterClass.DARK_KNIGHT,
        false
    )

    @Test
    fun `test trade item function`() {

        val tradeWithCharacter = GameCharacter(
            "Test", 30, 0,
            mutableListOf(
                Item("Physic", 5, 100, 10, 1, AttackCategory.PHYSICAL, WeaponCategory.SWORD),
                Item("Physic", 25, 100, 110, 1, AttackCategory.PHYSICAL, WeaponCategory.SWORD),
                Item("Magical", 5, 100, 5, 2, AttackCategory.MAGICAL, WeaponCategory.TOME),
                Item("Magical", 5, 100, 5, 2, AttackCategory.MAGICAL, WeaponCategory.TOME),
                Item("Magical", 5, 100, 5, 2, AttackCategory.MAGICAL, WeaponCategory.TOME),
                Item("Magical", 5, 100, 5, 2, AttackCategory.MAGICAL, WeaponCategory.STAFF)
            ),
            mapOf(
                Stat.HEALTH to 30,
                Stat.STRENGTH to 10,
                Stat.MAGICK to 20,
                Stat.DEFENSE to 10,
                Stat.RESISTANCE to 10,
                Stat.SKILL to 15,
                Stat.LUCK to 10,
                Stat.SPEED to 10
            ),
            CharacterClass.DARK_KNIGHT,
            false
        )

        Assertions.assertThrows(TradeEquippedItemException::class.java) {
            equipmentService.tradeEquipment(
                gameCharacter,tradeWithCharacter, 0, null
            )
        }

        Assertions.assertThrows(EquipmentLimitExceededException::class.java) {
            equipmentService.tradeEquipment(
                gameCharacter,tradeWithCharacter, 1, null
            )
        }

        Assertions.assertThrows(TradeEquippedItemException::class.java) {
            equipmentService.tradeEquipment(
                gameCharacter,tradeWithCharacter, 1, 0
            )
        }

        Assertions.assertDoesNotThrow {
            equipmentService.tradeEquipment(
                gameCharacter, tradeWithCharacter, 1, 1
            )
        }

        tradeWithCharacter.equipment.removeAt(5)

        Assertions.assertDoesNotThrow {
            equipmentService.tradeEquipment(
                gameCharacter, tradeWithCharacter, 1, null
            )
        }

        Assertions.assertEquals(6, tradeWithCharacter.equipment.size)
        Assertions.assertEquals(4, gameCharacter.equipment.size)

    }

    @Test
    fun `test equip item function`() {

        Assertions.assertThrows(ItemDoesNotExistsException::class.java) {
            equipmentService.equipItem(gameCharacter, 7)
        }

        Assertions.assertThrows(ItemDoesNotExistsException::class.java) {
            equipmentService.equipItem(gameCharacter, 6)
        }

        Assertions.assertThrows(WeaponNotAllowedException::class.java) {
            equipmentService.equipItem(gameCharacter, 4)
        }

        Assertions.assertDoesNotThrow {
            equipmentService.equipItem(gameCharacter, 1)
        }

        Assertions.assertEquals(1, gameCharacter.currentEquippedItem)

    }

}