package pan.alexander.githubclient.ui.repodetails

import com.github.terrakok.cicerone.Router
import moxy.viewstate.strategy.alias.AddToEndSingle
import pan.alexander.githubclient.ui.base.BasePresenter
import pan.alexander.githubclient.ui.base.BaseView

class RepoDetailsContract {

    interface View : BaseView {
        @AddToEndSingle
        fun showRepoDetails()
    }

    abstract class Presenter(router: Router) : BasePresenter<View>(router)
}
