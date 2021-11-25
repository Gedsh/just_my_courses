package pan.alexander.logintask.ui.login

abstract class Contract {

    sealed class ViewState {
        object Idle : ViewState()
        object Checking : ViewState()
        object Success : ViewState()
        data class Error(val type: ErrorType) : ViewState()
    }

    enum class ErrorType {
        USER_ALREADY_EXIST, USER_NOT_EXIST
    }

    interface View {
        fun setState(state: ViewState)
        fun setPassword(password: String)
        fun hideKeyboard()
    }

    interface Presenter {
        fun onAttach(view: View)
        fun onDetach()
        fun onDestroy()

        fun onLogIn(login: String, password: String)
        fun onSignUp(login: String, password: String)
        fun onForgetPassword(login: String)
    }
}
