package pan.alexander.githubclient.utils.eventbus

import io.reactivex.rxjava3.core.Observable

interface EventBus<T> {
    fun post(event: T)
    fun get(): Observable<T>
}
