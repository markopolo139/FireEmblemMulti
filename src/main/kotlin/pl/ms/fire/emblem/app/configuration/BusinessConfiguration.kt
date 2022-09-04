package pl.ms.fire.emblem.app.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope
import pl.ms.fire.emblem.business.serices.BattleService
import pl.ms.fire.emblem.business.serices.BoardService
import pl.ms.fire.emblem.business.serices.CharacterManagementService
import pl.ms.fire.emblem.business.serices.EquipmentManagementService

@Configuration
class BusinessConfiguration {

    @Bean
    @Scope("singleton")
    fun getBattleService() = BattleService()

    @Bean
    @Scope("singleton")
    fun getBoardService() = BoardService()

    @Bean
    @Scope("singleton")
    fun getCharacterManagementService() = CharacterManagementService()

    @Bean
    @Scope("singleton")
    fun getEquipmentManagementService() = EquipmentManagementService()

}