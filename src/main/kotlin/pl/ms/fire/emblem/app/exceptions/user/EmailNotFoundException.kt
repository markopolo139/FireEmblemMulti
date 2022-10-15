package pl.ms.fire.emblem.app.exceptions.user

import pl.ms.fire.emblem.app.exceptions.AppException

class EmailNotFoundException: AppException("Given email not found")