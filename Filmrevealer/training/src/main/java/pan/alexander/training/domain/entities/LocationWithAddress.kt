package pan.alexander.training.domain.entities

data class LocationWithAddress(
    val latitude: Double,
    val longitude: Double,
    var addressLine: String,
    val destination: Destination
) {
    enum class Destination {
        CURRENT_LOCATION,
        SELECTED_LOCATION
    }
}
