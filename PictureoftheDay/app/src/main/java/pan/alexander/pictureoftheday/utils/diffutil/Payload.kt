package pan.alexander.pictureoftheday.utils.diffutil

object Payload {
    fun <T> createCombinedPayload(payloads: List<Change<T>>?): Change<T>? {
        if (payloads?.isNotEmpty() == true) {
            val firstChange = payloads.first()
            val lastChange = payloads.last()
            return Change(firstChange.oldData, lastChange.newData)
        }
        return null
    }
}
