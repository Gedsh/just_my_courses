package pan.alexander.pictureoftheday.domain.settings

enum class AppUiMode(val orderNumber: Int) {
    MODE_AUTO(1),
    MODE_DAY(2),
    MODE_NIGHT(3);

    companion object {
        fun byNumber(number: Int): AppUiMode {
            return values().first { it.orderNumber == number }
        }
    }
}
