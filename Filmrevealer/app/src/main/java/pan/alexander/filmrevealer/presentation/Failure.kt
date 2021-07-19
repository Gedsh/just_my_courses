package pan.alexander.filmrevealer.presentation

sealed class Failure {
    data class WithMessage(val message: String) : Failure()
    data class WithMessageAndAction(val message: String, val block: () -> Unit) : Failure()
}
