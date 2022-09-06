package pl.ms.fire.emblem.web.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import pl.ms.fire.emblem.app.persistence.toEntity
import pl.ms.fire.emblem.app.services.PlayerPresetService
import pl.ms.fire.emblem.app.websocket.messages.models.toModel
import pl.ms.fire.emblem.web.model.request.WebGameCharacterModel
import pl.ms.fire.emblem.web.model.toEntityNoPreset
import pl.ms.fire.emblem.web.model.toWebModel
import javax.validation.Valid

@RestController
@Validated
@CrossOrigin
class PresetController {

    @Autowired
    private lateinit var playerPresetService: PlayerPresetService

    @PostMapping("/api/v1/preset/save")
    fun savePreset(@RequestBody characters: List<@Valid WebGameCharacterModel>) =
        playerPresetService.createPreset(characters.map { it.toEntityNoPreset() }.toSet())


    @PostMapping("/api/v1/preset/list/save")
    fun savePresets(@RequestBody charactersList: List<List<@Valid WebGameCharacterModel>>) =
        playerPresetService.createPresets(
            charactersList.map { characterModels -> characterModels.map { it.toEntityNoPreset() }.toSet() }
        )

    @PostMapping("/api/v1/preset/delete")
    fun deletePreset(@RequestParam id: Int) = playerPresetService.deletePreset(id)

    @PostMapping("/api/v1/preset/list/delete")
    fun deleteAllPresets() = playerPresetService.deletePresets()

    @GetMapping("/api/v1/preset/get")
    fun getPresets(): Map<Int, List<WebGameCharacterModel>> {
        return playerPresetService.getAllPresets().mapIndexed { index, appPresetEntity ->
            index to appPresetEntity.gameCharacterEntities.map { it.toWebModel() }
        }.toMap()
    }

    @GetMapping("/api/v1/preset/get/selected")
    fun getSelectedPreset(): List<WebGameCharacterModel> =
        playerPresetService.getSelectedPreset().gameCharacterEntities.map { it.toWebModel() }.toList()


    @PutMapping("/api/v1/preset/select")
    fun selectPreset(@RequestParam id: Int) = playerPresetService.selectPreset(id)
}