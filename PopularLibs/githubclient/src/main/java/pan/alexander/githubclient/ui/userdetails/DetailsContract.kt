package pan.alexander.githubclient.ui.userdetails

import com.github.terrakok.cicerone.Router
import moxy.viewstate.strategy.alias.AddToEndSingle
import pan.alexander.githubclient.ui.base.BasePresenter
import pan.alexander.githubclient.ui.base.BaseView

class DetailsContract {

    interface View : BaseView {
        @AddToEndSingle
        fun showUserDetails()
    }

    abstract class Presenter(router: Router) : BasePresenter<View>(router)
}
