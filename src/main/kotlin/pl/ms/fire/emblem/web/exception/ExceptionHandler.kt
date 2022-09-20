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
import pl.ms.fire.emblem.app.exceptions.*
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
typealias AppSpotDoesNotExists = pl.ms.fire.emblem.app.exceptions.SpotDoesNotExistsException

@Suppress("UNCHECKED_CAST")
@ControllerAdvice
class ExceptionHandler: ResponseEntityExceptionHandler() {


    companion object {
        const val DEFAULT_ACTION = "Contact server admin"
        const val DEFAULT_ERROR_MESSAGE = "Unexpected error occurred"
        const val DEFAULT_NULL_ERROR_MESSAGE = "No message"
        val DEFAULT_HTTP_STATUS = HttpStatus.INTERNAL_SERVER_ERROR
    }

    @Autowired
    private lateinit var request: HttpServletRequest

    private fun ApiError.toResponse() = ResponseEntity(this, httpStatus)

    private fun error(
        action: String = DEFAULT_ACTION,
        errorMessage: String? = DEFAULT_ERROR_MESSAGE,
        subErrors: List<ApiSubError> = listOf(),
        httpStatus: HttpStatus = DEFAULT_HTTP_STATUS
    ): ResponseEntity<ApiError> =
        ApiError(action, errorMessage ?: DEFAULT_NULL_ERROR_MESSAGE, subErrors, httpStatus).toResponse()

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
            errorMessage = e.message,
            httpStatus = HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(OutOfRangeException::class)
    fun outOfRangeExceptionHandler(e: OutOfRangeException): ResponseEntity<ApiError> =
        error(
            "Select character in range of selected item",
            errorMessage = e.message,
            httpStatus = HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(StaffInBattleException::class)
    fun staffInBattleExceptionHandler(e: StaffInBattleException): ResponseEntity<ApiError> =
        error(
            "Select character without staff equipped or equip another weapon",
            errorMessage = e.message,
            httpStatus = HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(NotEnoughMovementException::class)
    fun notEnoughMovementExceptionHandler(e: NotEnoughMovementException): ResponseEntity<ApiError> =
        error(
            "Select shorter route",
            errorMessage = e.message,
            httpStatus = HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(PairOnRouteException::class)
    fun pairOnRouteExceptionHandler(e: PairOnRouteException): ResponseEntity<ApiError> =
        error(
            "Select another route",
            errorMessage = e.message,
            httpStatus = HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(RouteNotConstantException::class)
    fun routeNotConstantExceptionHandler(e: RouteNotConstantException): ResponseEntity<ApiError> =
        error(
            "Select constant route",
            errorMessage = e.message,
            httpStatus = HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(SpotDoesNotExistsException::class)
    fun spotDoesNotExistsExceptionHandler(e: SpotDoesNotExistsException): ResponseEntity<ApiError> =
        error(
            "Select spot that is on board",
            errorMessage = e.message,
            httpStatus = HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(NoCharacterOnSpotException::class)
    fun noCharacterOnSpotExceptionHandler(e: NoCharacterOnSpotException): ResponseEntity<ApiError> =
        error(
            "Select spot with character",
            errorMessage = e.message,
            httpStatus = HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(NoSupportCharacterException::class)
    fun noSupportCharacterExceptionHandler(e: NoSupportCharacterException): ResponseEntity<ApiError> =
        error(
            "Select pair with support",
            errorMessage = e.message,
            httpStatus = HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(PairAlreadyHaveSupportException::class)
    fun pairAlreadyHaveSupportExceptionHandler(e: PairAlreadyHaveSupportException): ResponseEntity<ApiError> =
        error(
            "Select pair without support",
            errorMessage = e.message,
            httpStatus = HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(PairAlreadyOnSpotException::class)
    fun pairAlreadyOnSpotExceptionHandler(e: PairAlreadyOnSpotException): ResponseEntity<ApiError> =
        error(
            "Select spot without pair",
            errorMessage = e.message,
            httpStatus = HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(SeparatePairException::class)
    fun separatePairExceptionHandler(e: SeparatePairException): ResponseEntity<ApiError> =
        error(
            errorMessage = e.message,
            httpStatus = HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(EquipmentLimitExceededException::class)
    fun equipmentLimitExceededExceptionHandler(e: EquipmentLimitExceededException): ResponseEntity<ApiError> =
        error(
            "Discard item because reached equipment limit",
            errorMessage = e.message,
            httpStatus = HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(ItemDoesNotExistsException::class)
    fun itemDoesNotExistsExceptionHandler(e: ItemDoesNotExistsException): ResponseEntity<ApiError> =
        error(
            "Select id in equipment which exists",
            errorMessage = e.message,
            httpStatus = HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(NoItemEquippedException::class)
    fun noItemEquippedExceptionHandler(e: NoItemEquippedException): ResponseEntity<ApiError> =
        error(
            "Equip item for selected character",
            errorMessage = e.message,
            httpStatus = HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(NotStaffException::class)
    fun notStaffExceptionHandler(e: NotStaffException): ResponseEntity<ApiError> =
        error(
            "Select not a staff item",
            errorMessage = e.message,
            httpStatus = HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(TradeEquippedItemException::class)
    fun tradeEquippedItemExceptionHandler(e: TradeEquippedItemException): ResponseEntity<ApiError> =
        error(
            "Selected not equipped weapon to trade",
            errorMessage = e.message,
            httpStatus = HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(WeaponNotAllowedException::class)
    fun weaponNotAllowedExceptionHandler(e: WeaponNotAllowedException): ResponseEntity<ApiError> =
        error(
            errorMessage = e.message,
            httpStatus = HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(CharacterMovedException::class)
    fun characterMovedExceptionHandler(e: CharacterMovedException): ResponseEntity<ApiError> =
        error(
            "Select character not moved in this turn or end turn",
            errorMessage = e.message,
            httpStatus = HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(PositionOutOfBoundsException::class)
    fun positionOutOfBoundsExceptionHandler(e: PositionOutOfBoundsException): ResponseEntity<ApiError> =
        error(
            "Select position in bound",
            errorMessage = e.message,
            httpStatus = HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(BoardConfigurationException::class)
    fun boardConfigurationExceptionHandler(e: BoardConfigurationException): ResponseEntity<ApiError> =
        error(
            "Correct invalid values in board configuration",
            errorMessage = e.message,
            httpStatus = HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(BoardNotFoundException::class)
    fun boardNotFoundExceptionHandler(e: BoardNotFoundException): ResponseEntity<ApiError> =
        error(
            errorMessage = e.message,
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR
        )

    @ExceptionHandler(EmailNotFoundException::class)
    fun emailNotFoundExceptionHandler(e: EmailNotFoundException): ResponseEntity<ApiError> =
        error(
            errorMessage = e.message,
            httpStatus = HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(InvalidCharacterPairException::class)
    fun invalidCharacterPairExceptionHandler(e: InvalidCharacterPairException): ResponseEntity<ApiError> =
        error(
            "Character pair does not have spot",
            errorMessage = e.message,
            httpStatus = HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(InvalidNumberOfCharactersInPresetException::class)
    fun invalidNumberOfCharactersInPresetExceptionHandler(e: InvalidNumberOfCharactersInPresetException): ResponseEntity<ApiError> =
        error(
            "Create correct number of characters for preset",
            errorMessage = e.message,
            httpStatus = HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(InvalidPositionException::class)
    fun invalidPositionExceptionHandler(e: InvalidPositionException): ResponseEntity<ApiError> =
        error(
            "Check log for specific place where error occurred",
            errorMessage = e.message,
            httpStatus = HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(InvalidSpotException::class)
    fun invalidSpotExceptionHandler(e: InvalidSpotException): ResponseEntity<ApiError> =
        error(
            errorMessage = e.message,
            httpStatus = HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(InvalidStartPositionException::class)
    fun invalidStartPositionExceptionHandler(e: InvalidStartPositionException): ResponseEntity<ApiError> =
        error(
            "Correct invalid position for set up characters",
            errorMessage = e.message,
            httpStatus = HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(InvalidStatForCharacterException::class)
    fun invalidStatForCharacterExceptionHandler(e: InvalidStatForCharacterException): ResponseEntity<ApiError> =
        error(
            "Correct invalid number of distributed stats for character",
            errorMessage = e.message,
            httpStatus = HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(InvalidWebSocketTokenException::class)
    fun invalidWebSocketTokenExceptionHandler(e: InvalidWebSocketTokenException): ResponseEntity<ApiError> =
        error(
            errorMessage = e.message,
            httpStatus = HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(NoPresetsException::class)
    fun noPresetsExceptionHandler(e: NoPresetsException): ResponseEntity<ApiError> =
        error(
            "Player must have at least one preset to start a game",
            errorMessage = e.message,
            httpStatus = HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(NotCurrentTurnException::class)
    fun notCurrentTurnExceptionHandler(e: NotCurrentTurnException): ResponseEntity<ApiError> =
        error(
            "Wait for your turn",
            errorMessage = e.message,
            httpStatus = HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(NotPlayersPairException::class)
    fun notPlayersPairExceptionHandler(e: NotPlayersPairException): ResponseEntity<ApiError> =
        error(
            "Select character pair that belong to current player",
            errorMessage = e.message,
            httpStatus = HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(PasswordRecoveryTokenNotFoundException::class)
    fun passwordRecoveryTokenNotFoundExceptionHandler(e: PasswordRecoveryTokenNotFoundException): ResponseEntity<ApiError> =
        error(
            "Type correct password recovery token",
            errorMessage = e.message,
            httpStatus = HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(PresetDoesNotExistsException::class)
    fun presetDoesNotExistsExceptionHandler(e: PresetDoesNotExistsException): ResponseEntity<ApiError> =
        error(
            "Select preset id that player have",
            errorMessage = e.message,
            httpStatus = HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(PresetLimitExceededException::class)
    fun presetLimitExceededExceptionHandler(e: PresetLimitExceededException): ResponseEntity<ApiError> =
        error(
            "Delete one of previous preset (reached the limit)",
            errorMessage = e.message,
            httpStatus = HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(AppSpotDoesNotExists::class)
    fun appSpotDoesNotExistsHandler(e: AppSpotDoesNotExists): ResponseEntity<ApiError> =
        error(
            errorMessage = e.message,
            httpStatus = HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(UserEmailAlreadyExistsException::class)
    fun userEmailAlreadyExistsExceptionHandler(e: UserEmailAlreadyExistsException): ResponseEntity<ApiError> =
        error(
            "Selected email already exists",
            errorMessage = e.message,
            httpStatus = HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(UserInGameException::class)
    fun userInGameExceptionHandler(e: UserInGameException): ResponseEntity<ApiError> =
        error(
            "End or exit current game",
            errorMessage = e.message,
            httpStatus = HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(UsernameNotFoundException::class)
    fun usernameNotFoundExceptionHandler(e: UsernameNotFoundException): ResponseEntity<ApiError> =
        error(
            "Type valid username",
            errorMessage = e.message,
            httpStatus = HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(UserNotFoundException::class)
    fun userNotFoundExceptionHandler(e: UserNotFoundException): ResponseEntity<ApiError> =
        error(
            errorMessage = e.message,
            httpStatus = HttpStatus.BAD_REQUEST
        )

}