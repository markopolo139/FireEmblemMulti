package pl.ms.fire.emblem.web.exception

import org.springframework.http.HttpStatus

class ApiError(
    val suggestedAction: String,
    val errorMessage: String,
    val subErrors: List<ApiSubError>,
    val httpStatus: HttpStatus
)