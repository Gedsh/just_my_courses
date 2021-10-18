package pan.alexander.githubclient.ui

import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.plusAssign
import io.reactivex.rxjava3.kotlin.subscribeBy
import pan.alexander.githubclient.utils.eventbus.Error
import pan.alexander.githubclient.utils.eventbus.EventBus
import pan.alexander.githubclient.utils.navigation.Screens
import javax.inject.Inject

class MainPresenter @Inject constructor(
    private val router: Router,
    private val screens: Screens,
    private val errorEventBus: EventBus<Error>
) : MainContract.Presenter() {

    private val disposables by lazy { CompositeDisposable() }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        initErrorListener()

        router.replaceScreen(screens.users())
    }

    private fun initErrorListener() {
        disposables += errorEventBus.get()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = { viewState.showError(it.message) }
            )
    }

    override fun onBackPressed() {
        router.exit()
    }

    override fun onDestroy() {
        disposables.clear()

        super.onDestroy()
    }
}
