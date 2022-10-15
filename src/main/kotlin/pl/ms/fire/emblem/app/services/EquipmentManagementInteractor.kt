package pl.ms.fire.emblem.app.services

import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service
import pl.ms.fire.emblem.app.entities.AppCharacterPairEntity
import pl.ms.fire.emblem.app.exceptions.board.InvalidPositionException
import pl.ms.fire.emblem.app.persistence.repositories.SpotRepository
import pl.ms.fire.emblem.app.persistence.toAppEntity
import pl.ms.fire.emblem.app.persistence.toEntity
import pl.ms.fire.emblem.app.websocket.messages.equipment.EquipMessageModel
import pl.ms.fire.emblem.app.websocket.messages.equipment.TradeMessageModel
import pl.ms.fire.emblem.app.websocket.messages.models.toModel
import pl.ms.fire.emblem.business.exceptions.CharacterMovedException
import pl.ms.fire.emblem.business.exceptions.item.EquipmentLimitExceededException
import pl.ms.fire.emblem.business.exceptions.item.ItemDoesNotExistsException
import pl.ms.fire.emblem.business.exceptions.item.TradeEquippedItemException
import pl.ms.fire.emblem.business.exceptions.item.WeaponNotAllowedException
import pl.ms.fire.emblem.business.serices.EquipmentManagementService
import pl.ms.fire.emblem.business.values.board.Position

@Service
class EquipmentManagementInteractor {

    companion object {
        private val logger = LogManager.getLogger()
    }

    @Autowired
    private lateinit var equipmentService: EquipmentManagementService

    @Autowired
    private lateinit var serviceUtils: ServiceUtils

    @Autowired
    private lateinit var spotRepository: SpotRepository

    @Autowired
    private lateinit var simpMessagingTemplate: SimpMessagingTemplate

    fun equipItem(pairPosition: Position, itemId: Int) {
        serviceUtils.validateCurrentTurn()

        val pairSpot = spotRepository.getByBoardIdAndXAndY(serviceUtils.getBoardId(), pairPosition.x, pairPosition.y)
            .orElseThrow {
                logger.debug("Invalid position for equip item")
                InvalidPositionException()
            }.toAppEntity()

        serviceUtils.validateCorrectPair((pairSpot.standingCharacter as? AppCharacterPairEntity))

        try {
            equipmentService.equipItem(pairSpot.standingCharacter!!.leadCharacter, itemId)
        }
        catch(e: CharacterMovedException) {
            logger.debug("Selected character that moved")
            throw e
        }
        catch (e: ItemDoesNotExistsException) {
            logger.debug("Selected item id does not correspond in equipment list")
            throw e
        }
        catch (e: WeaponNotAllowedException) {
            logger.debug("Selected character can't use selected weapon")
            throw e
        }
        catch (e: Exception) {
            logger.debug("Unexpected exception ${e.message}")
            throw e
        }

        spotRepository.save(pairSpot.toEntity())
        simpMessagingTemplate.convertAndSend(
            "/topic/board-${serviceUtils.getBoardId()}/item/equip", EquipMessageModel(pairSpot.toModel())
        )

    }

    fun tradeItems(pairPosition: Position, itemId: Int, tradeWithPosition: Position, tradeItemId: Int?) {
        serviceUtils.validateCurrentTurn()

        val pairSpot = spotRepository.getByBoardIdAndXAndY(serviceUtils.getBoardId(), pairPosition.x, pairPosition.y)
            .orElseThrow {
                logger.debug("Invalid position for equip item")
                InvalidPositionException()
            }.toAppEntity()

        serviceUtils.validateCorrectPair((pairSpot.standingCharacter as? AppCharacterPairEntity))

        val tradeWithSpot = spotRepository.getByBoardIdAndXAndY(serviceUtils.getBoardId(), tradeWithPosition.x, tradeWithPosition.y)
            .orElseThrow {
                logger.debug("Invalid position for equip item")
                InvalidPositionException()
            }.toAppEntity()

        serviceUtils.validateCorrectPair((tradeWithSpot.standingCharacter as? AppCharacterPairEntity))

        try {
            equipmentService.tradeEquipment(
                pairSpot.standingCharacter!!.leadCharacter, tradeWithSpot.standingCharacter!!.leadCharacter, itemId, tradeItemId
            )
        }
        catch(e: CharacterMovedException) {
            logger.debug("Selected character that moved")
            throw e
        }
        catch (e: ItemDoesNotExistsException) {
            logger.debug("Selected item id does not correspond in equipment list")
            throw e
        }
        catch (e: TradeEquippedItemException) {
            logger.debug("Selected item to trade is equipped by character")
            throw e
        }
        catch (e: EquipmentLimitExceededException) {
            logger.debug("Cannot exceed equipment limit (${EquipmentManagementService.EQUIPMENT_LIMIT})")
            throw e
        }
        catch (e: Exception) {
            logger.debug("Unexpected exception ${e.message}")
            throw e
        }

        spotRepository.saveAll(listOf(pairSpot.toEntity(), tradeWithSpot.toEntity()))
        simpMessagingTemplate.convertAndSend(
            "/topic/board-${serviceUtils.getBoardId()}/item/trade", TradeMessageModel(pairSpot.toModel(), tradeWithSpot.toModel())
        )


    }

}