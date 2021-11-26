package pan.alexander.githubclient.database

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import javax.inject.Inject

private const val LIST_ITEM_SEPARATOR = ","

@ProvidedTypeConverter
class ListTypeConverter @Inject constructor() {

    @TypeConverter
    fun listToString(list: List<String>): String = list.joinToString(LIST_ITEM_SEPARATOR)

    @TypeConverter
    fun stringToList(string: String): List<String> = string.split(LIST_ITEM_SEPARATOR).let {
        if (it.firstOrNull()?.isNotBlank() == true) {
            it
        } else {
            emptyList()
        }
    }
}
