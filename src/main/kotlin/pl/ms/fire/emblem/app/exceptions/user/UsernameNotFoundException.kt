package pl.ms.fire.emblem.app.exceptions.user

import pl.ms.fire.emblem.app.exceptions.AppException

class UsernameNotFoundException: AppException("Given username not found")