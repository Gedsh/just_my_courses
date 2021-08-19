package pan.alexander.pictureoftheday.domain.notes.entities

import android.content.Context
import androidx.annotation.Keep
import androidx.core.content.ContextCompat
import pan.alexander.pictureoftheday.R
import java.util.*

@Keep
data class Note(
    val id: Int,
    var text: String,
    var date: Date,
    var priority: Priority
) {

    constructor(
        text: String,
        date: Date,
        priority: Priority
    ) : this(
        globalIds++,
        text,
        date,
        priority
    )

    enum class Priority {
        LOW,
        NORMAL,
        HIGH;

        fun getColor(context: Context): Int {
            return when (this) {
                LOW -> ContextCompat.getColor(context, R.color.cyan_200)
                NORMAL -> ContextCompat.getColor(context, R.color.white)
                HIGH -> ContextCompat.getColor(context, R.color.pink_200)
            }
        }
    }

    private companion object {
        var globalIds: Int = 1
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Note

        if (id != other.id) return false
        if (text != other.text) return false
        if (date != other.date) return false
        if (priority != other.priority) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + text.hashCode()
        result = 31 * result + date.hashCode()
        result = 31 * result + priority.hashCode()
        return result
    }

}
