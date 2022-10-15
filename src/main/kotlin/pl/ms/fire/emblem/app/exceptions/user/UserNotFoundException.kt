package pl.ms.fire.emblem.app.exceptions.user

import pl.ms.fire.emblem.app.exceptions.AppException

class UserNotFoundException: AppException("User not found")