package pan.alexander.githubclient.ui.base

import com.github.terrakok.cicerone.Router
import moxy.MvpPresenter

abstract class BasePresenter<V : BaseView>(
    private val router: Router
) : MvpPresenter<V>() {
    fun onBackPressed(): Boolean {
        router.exit()
        return true
    }
}
