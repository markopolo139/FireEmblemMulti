package pl.ms.fire.emblem.app.exceptions

class InvalidNumberOfCharactersInPresetException(characterLimit: Int):
    AppException("Preset have invalid number of characters(must be $characterLimit)")