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
import pl.ms.fire.emblem.business.exceptions.CharacterMovedException
import pl.ms.fire.emblem.business.exceptions.PositionOutOfBoundsException
import pl.ms.fire.emblem.business.exceptions.battle.NotAllowedWeaponCategoryException
import pl.ms.fire.emblem.business.exceptions.battle.OutOfRangeException
import pl.ms.fire.emblem.business.exceptions.battle.StaffInBattleException
import pl.ms.fire.emblem.business.exceptions.board.NotEnoughMovementException
import pl.ms.fire.emblem.business.exceptions.board.PairOnRouteException
import pl.ms.fire.emblem.business.exceptions.board.RouteNotConstantException
import pl.ms.fire.emblem.business.exceptions.board.SpotDoesNotExistsException
import pl.ms.fire.emblem.business.exceptions.character.*
import pl.ms.fire.emblem.business.exceptions.item.*
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

    @ExceptionHandler(NotAllowedWeaponCategoryException::class)
    fun notAllowedWeaponCategoryExceptionHandler(e: NotAllowedWeaponCategoryException): ResponseEntity<ApiError> =
        error(
            "Select another weapon for character (which is in allowed weapons categories)",
            errorMessage = e.message ?: "Selected character can't use selected weapon",
            httpStatus = HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(OutOfRangeException::class)
    fun outOfRangeExceptionHandler(e: OutOfRangeException): ResponseEntity<ApiError> =
        error(
            "Select character in range of selected item",
            errorMessage = e.message ?: "Selected character is out of range",
            httpStatus = HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(StaffInBattleException::class)
    fun staffInBattleExceptionHandler(e: StaffInBattleException): ResponseEntity<ApiError> =
        error(
            "Select character without staff equipped or equip another weapon",
            errorMessage = e.message ?: "Selected character with staff equipped for battle",
            httpStatus = HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(NotEnoughMovementException::class)
    fun notEnoughMovementExceptionHandler(e: NotEnoughMovementException): ResponseEntity<ApiError> =
        error(
            "Select shorter route",
            errorMessage = e.message ?: "Selected spot which is too far for selected character",
            httpStatus = HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(PairOnRouteException::class)
    fun pairOnRouteExceptionHandler(e: PairOnRouteException): ResponseEntity<ApiError> =
        error(
            "Select another route",
            errorMessage = e.message ?: "Pair on selected route (can't go through)",
            httpStatus = HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(RouteNotConstantException::class)
    fun routeNotConstantExceptionHandler(e: RouteNotConstantException): ResponseEntity<ApiError> =
        error(
            "Select constant route",
            errorMessage = e.message ?: "Selected not constant route",
            httpStatus = HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(SpotDoesNotExistsException::class)
    fun spotDoesNotExistsExceptionHandler(e: SpotDoesNotExistsException): ResponseEntity<ApiError> =
        error(
            "Select spot that is on board",
            errorMessage = e.message ?: "Selected spot that does not exists",
            httpStatus = HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(NoCharacterOnSpotException::class)
    fun noCharacterOnSpotExceptionHandler(e: NoCharacterOnSpotException): ResponseEntity<ApiError> =
        error(
            "Select spot with character",
            errorMessage = e.message ?: "Selected spot without character",
            httpStatus = HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(NoSupportCharacterException::class)
    fun noSupportCharacterExceptionHandler(e: NoSupportCharacterException): ResponseEntity<ApiError> =
        error(
            "Select pair with support",
            errorMessage = e.message ?: "Pair does not have support character",
            httpStatus = HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(PairAlreadyHaveSupportException::class)
    fun pairAlreadyHaveSupportExceptionHandler(e: PairAlreadyHaveSupportException): ResponseEntity<ApiError> =
        error(
            "Select pair without support",
            errorMessage = e.message ?: "Selected pair without support",
            httpStatus = HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(PairAlreadyOnSpotException::class)
    fun pairAlreadyOnSpotExceptionHandler(e: PairAlreadyOnSpotException): ResponseEntity<ApiError> =
        error(
            "Select spot without pair",
            errorMessage = e.message ?: "Selected spot with pair",
            httpStatus = HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(SeparatePairException::class)
    fun separatePairExceptionHandler(e: SeparatePairException): ResponseEntity<ApiError> =
        error(
            errorMessage = e.message ?: "Error occurred during separation of pair",
            httpStatus = HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(EquipmentLimitExceededException::class)
    fun equipmentLimitExceededExceptionHandler(e: EquipmentLimitExceededException): ResponseEntity<ApiError> =
        error(
            "Discard item because reached equipment limit",
            errorMessage = e.message ?: "Exceeded equipment limit",
            httpStatus = HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(ItemDoesNotExistsException::class)
    fun itemDoesNotExistsExceptionHandler(e: ItemDoesNotExistsException): ResponseEntity<ApiError> =
        error(
            "Select id in equipment which exists",
            errorMessage = e.message ?: "Selected id that is not in equipment",
            httpStatus = HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(NoItemEquippedException::class)
    fun noItemEquippedExceptionHandler(e: NoItemEquippedException): ResponseEntity<ApiError> =
        error(
            "Equip item for selected character",
            errorMessage = e.message ?: "No item equipped",
            httpStatus = HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(NotStaffException::class)
    fun notStaffExceptionHandler(e: NotStaffException): ResponseEntity<ApiError> =
        error(
            "Select not a staff item",
            errorMessage = e.message ?: "Selected item is not a staff",
            httpStatus = HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(TradeEquippedItemException::class)
    fun tradeEquippedItemExceptionHandler(e: TradeEquippedItemException): ResponseEntity<ApiError> =
        error(
            "Selected not equipped weapon to trade",
            errorMessage = e.message ?: "Selected equipped item to trade",
            httpStatus = HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(WeaponNotAllowedException::class)
    fun weaponNotAllowedExceptionHandler(e: WeaponNotAllowedException): ResponseEntity<ApiError> =
        error(
            errorMessage = e.message ?: "",
            httpStatus = HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(CharacterMovedException::class)
    fun characterMovedExceptionHandler(e: CharacterMovedException): ResponseEntity<ApiError> =
        error(
            "Select character not moved in this turn or end turn",
            errorMessage = e.message ?: "Selected already moved character",
            httpStatus = HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(PositionOutOfBoundsException::class)
    fun positionOutOfBoundsExceptionHandler(e: PositionOutOfBoundsException): ResponseEntity<ApiError> =
        error(
            "Select position in bound",
            errorMessage = e.message ?: "Selected incorrect position",
            httpStatus = HttpStatus.BAD_REQUEST
        )
}