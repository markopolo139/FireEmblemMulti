package pl.ms.fire.emblem.app.exceptions

class InvalidNumberOfPresetsException(characterLimit: Int):
    AppException("Preset have invalid number of characters(must be $characterLimit)")