package pan.alexander.githubclient.ui.users

import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.kotlin.subscribeBy
import pan.alexander.githubclient.domain.users.UsersInteractor
import pan.alexander.githubclient.ui.users.adapter.AdapterContract
import pan.alexander.githubclient.utils.eventbus.Error
import pan.alexander.githubclient.utils.eventbus.EventBus
import pan.alexander.githubclient.utils.logger.AppLogger
import pan.alexander.githubclient.utils.navigation.Screens
import javax.inject.Inject

private const val RECYCLER_NO_POSITION = -1

class UsersPresenter @Inject constructor(
    val usersListPresenter: AdapterContract.UserListPresenter,
    private val usersInteractor: UsersInteractor,
    private val router: Router,
    private val screens: Screens,
    private val errorEventBus: EventBus<Error>
) : UsersContract.Presenter(router) {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        viewState.setState(UsersContract.ViewState.Idle)

        initRecycler()

        loadData()

        initItemClickListener()
    }

    private fun initRecycler() {
        viewState.initRecycler()
    }

    private fun loadData() {
        usersInteractor.getUsers()
            .doOnSubscribe { viewState.setState(UsersContract.ViewState.Loading) }
            .doOnError { throwable ->
                throwable.message?.let { errorEventBus.post(Error(it)) }
                viewState.setState(UsersContract.ViewState.Idle)
            }
            .subscribeBy(
                onSuccess = {
                    usersListPresenter.users.addAll(it)
                    viewState.updateUsers()
                    viewState.setState(UsersContract.ViewState.Success)
                },
                onError = {
                    AppLogger.logE("Get users failure", it)
                }
            ).autoDispose()
    }

    private fun initItemClickListener() {
        usersListPresenter.onItemClickListener = { itemView ->
            itemView.itemPosition.takeIf { it != RECYCLER_NO_POSITION }?.let {
                val user = usersListPresenter.users[it]
                router.navigateTo(screens.userDetails(user))
            }
        }
    }
}
