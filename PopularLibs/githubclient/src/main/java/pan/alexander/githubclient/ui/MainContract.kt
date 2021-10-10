package pan.alexander.githubclient.ui

import moxy.MvpPresenter
import moxy.MvpView

class MainContract {
    interface View : MvpView

    abstract class Presenter : MvpPresenter<View>() {
        abstract fun onBackPressed()
    }
}
