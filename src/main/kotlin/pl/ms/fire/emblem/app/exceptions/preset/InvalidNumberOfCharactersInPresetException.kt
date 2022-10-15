package pl.ms.fire.emblem.app.exceptions.preset

import pl.ms.fire.emblem.app.exceptions.AppException

class InvalidNumberOfCharactersInPresetException(characterLimit: Int):
    AppException("Preset have invalid number of characters(must be $characterLimit)")