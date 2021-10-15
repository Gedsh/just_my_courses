package pan.alexander.githubclient.ui

import moxy.MvpPresenter
import moxy.MvpView
import moxy.viewstate.strategy.alias.Skip

class MainContract {
    interface View : MvpView {
        @Skip
        fun showError(message: String)
    }

    abstract class Presenter : MvpPresenter<View>() {
        abstract fun onBackPressed()
    }
}
