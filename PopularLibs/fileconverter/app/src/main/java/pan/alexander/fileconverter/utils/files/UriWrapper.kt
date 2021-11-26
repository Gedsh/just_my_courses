package pan.alexander.fileconverter.utils.files

import android.net.Uri

@JvmInline
value class UriWrapper(val uri: Uri) {
    fun isTaskCancellationUri(): Boolean = uri == Uri.EMPTY

    companion object {
        fun getTaskCancellationUri() = UriWrapper(Uri.EMPTY)
    }
}
