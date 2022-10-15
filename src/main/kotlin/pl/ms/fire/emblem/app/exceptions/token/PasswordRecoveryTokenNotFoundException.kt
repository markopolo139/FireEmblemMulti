package pl.ms.fire.emblem.app.exceptions.token

import pl.ms.fire.emblem.app.exceptions.AppException

class PasswordRecoveryTokenNotFoundException: AppException("Given token is not used for password recovery")