package pan.alexander.githubclient.utils.eventbus

import io.reactivex.rxjava3.core.Observable

interface EventBus<T : Any> {
    fun post(event: T)
    fun get(): Observable<T>
}
