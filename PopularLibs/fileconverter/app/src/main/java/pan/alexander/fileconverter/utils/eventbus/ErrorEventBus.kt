package pan.alexander.fileconverter.utils.eventbus

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ErrorEventBus @Inject constructor() : EventBus<AppError> {

    private val bus: PublishSubject<AppError> = PublishSubject.create()

    override fun post(event: AppError) = bus.onNext(event)

    override fun get(): Observable<AppError> = bus
}

data class AppError(val message: String, val throwable: Throwable?)
