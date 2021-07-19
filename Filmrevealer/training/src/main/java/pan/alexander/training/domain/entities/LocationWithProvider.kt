package pan.alexander.training.domain.entities

data class LocationWithProvider(
    val latitude: Double,
    val longitude: Double,
    val provider: Provider
) {
    enum class Provider {
        GPS_PROVIDER,
        NETWORK_PROVIDER,
        LAST_KNOWN
    }
}
