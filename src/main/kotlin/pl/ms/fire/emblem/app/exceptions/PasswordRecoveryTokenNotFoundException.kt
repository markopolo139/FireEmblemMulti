package pl.ms.fire.emblem.app.exceptions

class PasswordRecoveryTokenNotFoundException: AppException("Given token is not used for password recovery")