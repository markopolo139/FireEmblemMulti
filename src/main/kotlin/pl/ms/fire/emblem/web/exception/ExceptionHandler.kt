package pl.ms.fire.emblem.web.exception

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import javax.servlet.http.HttpServletRequest
import javax.validation.ConstraintViolationException

@Suppress("UNCHECKED_CAST")
@ControllerAdvice
class ExceptionHandler: ResponseEntityExceptionHandler() {

    companion object {
        const val DEFAULT_ACTION = "Contact server admin"
        const val DEFAULT_ERROR_MESSAGE = "Unexpected error occurred"
        val DEFAULT_HTTP_STATUS = HttpStatus.INTERNAL_SERVER_ERROR
    }

    @Autowired
    private lateinit var request: HttpServletRequest

    private fun ApiError.toResponse() = ResponseEntity(this, httpStatus)

    private fun error(
        action: String = DEFAULT_ACTION,
        errorMessage: String = DEFAULT_ERROR_MESSAGE,
        subErrors: List<ApiSubError> = listOf(),
        httpStatus: HttpStatus = DEFAULT_HTTP_STATUS
    ): ResponseEntity<ApiError> =
        ApiError(action, errorMessage, subErrors, httpStatus).toResponse()

    override fun handleHttpMessageNotReadable(
        ex: HttpMessageNotReadableException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        return error(
            action = "Give needed request body",
            errorMessage = ex.message ?: "Required request body",
            httpStatus = HttpStatus.BAD_REQUEST
        ) as ResponseEntity<Any>
    }

    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        val subApiErrors: List<ApiSubError> = ex.bindingResult.fieldErrors.asSequence().map {
            ApiSubError(
                errorMessage = "validation failed for ${it.rejectedValue} - ${it.defaultMessage}",
                suggestedAction = "Check rejected value"
            )
        }.toList()

        return error(
            action = "Check error sublist for more information",
            errorMessage = "Error occurred during validation",
            subErrors = subApiErrors,
            httpStatus = HttpStatus.BAD_REQUEST
        ) as ResponseEntity<Any>
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun constraintViolationExceptionHandler(ex: ConstraintViolationException): ResponseEntity<ApiError> {
        val subApiErrors: List<ApiSubError> = ex.constraintViolations.asSequence().map {
            ApiSubError(
                errorMessage = "validation failed for ${it.invalidValue} - ${it.message}",
                suggestedAction = "Check rejected value"
            )
        }.toList()

        return error(
            action = "Check error sublist for more information",
            errorMessage = "Error occurred during validation",
            subErrors = subApiErrors,
            httpStatus = HttpStatus.BAD_REQUEST
        )
    }
}