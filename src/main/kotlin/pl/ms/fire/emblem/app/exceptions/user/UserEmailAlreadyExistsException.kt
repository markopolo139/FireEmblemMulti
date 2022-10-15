package pl.ms.fire.emblem.app.exceptions.user

import pl.ms.fire.emblem.app.exceptions.AppException

class UserEmailAlreadyExistsException: AppException("Given email already exists in database")