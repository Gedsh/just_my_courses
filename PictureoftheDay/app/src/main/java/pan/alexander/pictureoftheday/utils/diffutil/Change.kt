package pan.alexander.pictureoftheday.utils.diffutil

data class Change<out T>(
    val oldData: T,
    val newData: T
)
