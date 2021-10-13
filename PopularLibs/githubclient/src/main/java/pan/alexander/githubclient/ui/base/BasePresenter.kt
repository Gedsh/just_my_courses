package pan.alexander.githubclient.ui.base

import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import moxy.MvpPresenter

abstract class BasePresenter<V : BaseView>(
    private val router: Router
) : MvpPresenter<V>() {
    private val disposables = CompositeDisposable()

    fun onBackPressed(): Boolean {
        router.exit()
        return true
    }

    fun Disposable.autoDispose() {
        disposables.add(this)
    }

    override fun onDestroy() {
        super.onDestroy()

        disposables.clear()
    }
}
