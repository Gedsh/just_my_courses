package pan.alexander.fileconverter.utils.files

import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract
import java.io.File

interface FileUtils {
    fun getFilePicker(): ActivityResultContract<String, Uri?>
    fun isOpenDocumentProviderExist(mimeType: String): Boolean
    fun getFileCreator(): ActivityResultContract<Pair<String, String>, Uri?>
    fun isCreateDocumentProviderExist(fileNameToMimeType: Pair<String, String>): Boolean
    fun getFileNameFromUri(uriWrapper: UriWrapper): String
    fun createTempFile(name: String): File
    fun copyFile(tempFile: File, destinationFileUri: UriWrapper)
    fun deleteFile(file: File): Boolean
    fun deleteFile(uriWrapper: UriWrapper): Boolean
}
