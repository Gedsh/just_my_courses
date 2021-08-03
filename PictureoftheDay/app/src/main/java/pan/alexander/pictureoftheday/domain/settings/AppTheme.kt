package pan.alexander.pictureoftheday.domain.settings

enum class AppTheme(val orderNumber: Int) {
    PURPLE(1),
    PINK(2),
    INDIGO(3);

    companion object {
        fun byNumber(number: Int): AppTheme {
            return values().first { it.orderNumber == number }
        }
    }
}
