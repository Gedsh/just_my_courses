package pan.alexander.githubclient.ui.userrepos

import com.github.terrakok.cicerone.Router
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.Skip
import pan.alexander.githubclient.domain.entities.UserGithubRepo
import pan.alexander.githubclient.ui.base.BasePresenter
import pan.alexander.githubclient.ui.base.BaseView

class ReposContract {

    sealed class ViewState {
        object Idle : ViewState()
        object Loading : ViewState()
        object Success : ViewState()
    }

    interface View : BaseView {
        @AddToEndSingle
        fun initRecycler()

        @Skip
        fun getUserData()

        @AddToEndSingle
        fun setState(state: ViewState)

        @AddToEndSingle
        fun updateRepos(repos: List<UserGithubRepo>)
    }

    abstract class Presenter(router: Router) : BasePresenter<View>(router) {
        abstract fun onUserDataReady(userId: Long, reposUrl: String)
        abstract fun onRepoClicked(repo: UserGithubRepo)
    }
}
