package pan.alexander.githubclient.ui.users

import com.github.terrakok.cicerone.Router
import moxy.viewstate.strategy.alias.AddToEndSingle
import pan.alexander.githubclient.ui.base.BasePresenter
import pan.alexander.githubclient.ui.base.BaseView

class UsersContract {

    interface View : BaseView {
        @AddToEndSingle
        fun initRecycler()

        @AddToEndSingle
        fun updateUsers()
    }

    abstract class Presenter(router: Router) : BasePresenter<View>(router)
}
