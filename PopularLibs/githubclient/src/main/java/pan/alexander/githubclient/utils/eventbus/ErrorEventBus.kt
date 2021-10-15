package pan.alexander.githubclient.utils.eventbus

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ErrorEventBus @Inject constructor() : EventBus<Error> {

    private val bus: PublishSubject<Error> = PublishSubject.create()

    override fun post(event: Error) = bus.onNext(event)

    override fun get(): Observable<Error> = bus
}

@JvmInline
value class Error(val message: String)
