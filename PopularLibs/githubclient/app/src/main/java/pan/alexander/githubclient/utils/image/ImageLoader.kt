package pan.alexander.githubclient.utils.image

interface ImageLoader<T> {
    fun loadInto(url: String, container: T)
}
