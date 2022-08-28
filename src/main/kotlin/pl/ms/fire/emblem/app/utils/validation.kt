package pl.ms.fire.emblem.app.utils

import pl.ms.fire.emblem.app.entities.AppSpotEntity
import pl.ms.fire.emblem.app.exceptions.CharacterMovedException
import pl.ms.fire.emblem.app.exceptions.SpotDoesNotExistsException

fun validateSpot(spot: AppSpotEntity?) {
    if(spot == null)
        throw SpotDoesNotExistsException()

    if (spot.standingCharacter?.leadCharacter?.moved == true)
        throw CharacterMovedException()
}