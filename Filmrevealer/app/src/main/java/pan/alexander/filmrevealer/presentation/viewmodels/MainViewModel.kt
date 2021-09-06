package pan.alexander.filmrevealer.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.*
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.plusAssign
import io.reactivex.rxjava3.kotlin.subscribeBy
import pan.alexander.filmrevealer.App

class MainViewModel : ViewModel(), LifecycleObserver {
    private val mainInteractor = App.instance.daggerComponent.getMainInteractor()
    private val disposables by lazy { CompositeDisposable() }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private fun cleanDatabase() {
        disposables += mainInteractor.get().deleteOldFilmDetails()
            .subscribeBy(
                onError = { Log.e(App.LOG_TAG, "Clean database fault", it) }
            )
    }
}
