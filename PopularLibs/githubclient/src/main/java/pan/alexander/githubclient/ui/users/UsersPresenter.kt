package pan.alexander.githubclient.ui.users

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.kotlin.subscribeBy
import pan.alexander.githubclient.App.Companion.LOG_TAG
import androidx.recyclerview.widget.RecyclerView
import com.github.terrakok.cicerone.Router
import pan.alexander.githubclient.domain.users.UsersInteractor
import pan.alexander.githubclient.ui.users.adapter.AdapterContract
import pan.alexander.githubclient.utils.navigation.Screens
import javax.inject.Inject

class UsersPresenter @Inject constructor(
    val usersListPresenter: AdapterContract.UserListPresenter,
    private val usersInteractor: UsersInteractor,
    private val router: Router,
    private val screens: Screens
) : UsersContract.Presenter(router) {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        initRecycler()

        loadData()

        initItemClickListener()
    }

    private fun initRecycler() {
        viewState.initRecycler()
    }

    private fun loadData() {
        usersInteractor.getUsers()
            .subscribeBy(
                onSuccess = {
                    usersListPresenter.users.addAll(it)
                    viewState.updateUsers()
                },
                onError = {
                    Log.e(LOG_TAG, "Get users failure", it)
                }
            ).autoDispose()
    }

    private fun initItemClickListener() {
        usersListPresenter.onItemClickListener = { itemView ->
            itemView.itemPosition.takeIf { it != RecyclerView.NO_POSITION }?.let {
                val user = usersListPresenter.users[it]
                router.navigateTo(screens.userDetails(user))
            }
        }
    }
}
