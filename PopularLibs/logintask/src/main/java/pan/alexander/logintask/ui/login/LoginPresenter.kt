package pan.alexander.logintask.ui.login

import kotlinx.coroutines.*
import pan.alexander.logintask.di.DISPATCHER_MAIN
import pan.alexander.logintask.domain.UsersRepository
import pan.alexander.logintask.domain.entities.User
import javax.inject.Inject
import javax.inject.Named

class LoginPresenter @Inject constructor(
    private val usersRepository: UsersRepository,
    @Named(DISPATCHER_MAIN)
    private val dispatcherMain: CoroutineDispatcher
) : Contract.Presenter {
    private val scope by lazy { CoroutineScope(Job() + dispatcherMain) }
    private var view: Contract.View? = null
    private var state: Contract.ViewState? = null

    override fun onAttach(view: Contract.View) {
        this.view = view

        state?.let { setAndSaveViewState(it) } ?: setAndSaveViewState(Contract.ViewState.Idle)

    }

    override fun onDetach() {
        view = null
    }

    override fun onDestroy() {
        scope.cancel()
    }

    override fun onLogIn(login: String, password: String) {
        scope.launch {
            hideKeyboard()

            setAndSaveViewState(Contract.ViewState.Checking)

            changeViewStateDependsOnUserExistAndPassCorrect(login, password)
        }
    }

    private suspend fun changeViewStateDependsOnUserExistAndPassCorrect(
        login: String,
        password: String
    ) {
        if (isUserExistAndPasswordCorrect(login, password)) {
            setAndSaveViewState(Contract.ViewState.Success)
        } else {
            setAndSaveViewState(Contract.ViewState.Error(Contract.ErrorType.USER_NOT_EXIST))
        }
    }

    private suspend fun isUserExistAndPasswordCorrect(login: String, password: String): Boolean {
        return usersRepository.getUser(login)?.let {
            it.password == password
        } ?: false
    }

    override fun onSignUp(login: String, password: String) {
        scope.launch {
            hideKeyboard()

            setAndSaveViewState(Contract.ViewState.Checking)

            checkIfUserExistAndMakeAppropriateAction(login, password)
        }
    }

    private suspend fun checkIfUserExistAndMakeAppropriateAction(login: String, password: String) {
        if (isUserExist(login)) {
            setAndSaveViewState(Contract.ViewState.Error(Contract.ErrorType.USER_ALREADY_EXIST))
        } else {
            saveUserDataAndChangeViewState(login, password)
        }
    }

    private suspend fun isUserExist(login: String): Boolean {
        return usersRepository.getUser(login)?.let { true } ?: false
    }

    private suspend fun saveUserDataAndChangeViewState(login: String, password: String) {
        usersRepository.saveUserData(User(login, password))
        setAndSaveViewState(Contract.ViewState.Success)
    }

    override fun onForgetPassword(login: String) {
        scope.launch {
            hideKeyboard()

            setAndSaveViewState(Contract.ViewState.Checking)

            showPasswordIfUserExistAndChangeState(login)
        }
    }

    private suspend fun showPasswordIfUserExistAndChangeState(login: String) {
        usersRepository.getUser(login)?.let {
            setPasswordAndChangeState(it)
        } ?: setAndSaveViewState(Contract.ViewState.Error(Contract.ErrorType.USER_NOT_EXIST))
    }

    private fun setPasswordAndChangeState(user: User) {
        view?.setPassword(user.password)
        setAndSaveViewState(Contract.ViewState.Idle)
    }

    private fun setAndSaveViewState(viewState: Contract.ViewState) {
        view?.setState(viewState)
        state = viewState
    }

    private fun hideKeyboard() {
        view?.hideKeyboard()
    }
}
