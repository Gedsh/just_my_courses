package pan.alexander.fileconverter.utils.files

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import androidx.activity.result.contract.ActivityResultContract
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject
import javax.inject.Singleton

const val JPEG_MIME_TYPE = "image/jpeg"
const val PNG_MIME_TYPE = "image/png"

private const val BUFFER_SIZE = 8 * 1024
private const val DEFAULT_FILE_NAME = "file"
private const val TEMP_FILE_EXTENSION = ".tmp"

@Singleton
class FileUtilsImpl @Inject constructor(
    private val context: Context
) : FileUtils {

    private val picker = object : ActivityResultContract<String, Uri?>() {

        override fun createIntent(context: Context, mimeType: String): Intent =
            Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = mimeType

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    putExtra(DocumentsContract.EXTRA_INITIAL_URI, Environment.DIRECTORY_DCIM)
                }
            }


        override fun parseResult(resultCode: Int, intent: Intent?): Uri? =
            when (resultCode) {
                Activity.RESULT_OK -> {
                    intent?.data?.let {
                        if (FileUtilsHelper.canRead(context, it)) {
                            it
                        } else {
                            null
                        }
                    }
                }
                else -> null
            }

    }

    private val creator = object : ActivityResultContract<Pair<String, String>, Uri?>() {
        override fun createIntent(
            context: Context,
            fileNameToMimeType: Pair<String, String>
        ): Intent =
            Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = fileNameToMimeType.second
                putExtra(Intent.EXTRA_TITLE, fileNameToMimeType.first)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    putExtra(DocumentsContract.EXTRA_INITIAL_URI, Environment.DIRECTORY_DCIM)
                }
            }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri? =
            when (resultCode) {
                Activity.RESULT_OK -> {
                    intent?.data?.let {
                        if (FileUtilsHelper.canWrite(context, it)) {
                            it
                        } else {
                            null
                        }
                    }
                }
                else -> null
            }
    }

    override fun getFilePicker(): ActivityResultContract<String, Uri?> = picker

    override fun isOpenDocumentProviderExist(mimeType: String) =
        context.packageManager?.let { packageManager ->
            picker.createIntent(context, mimeType).resolveActivity(packageManager)?.let { true }
                ?: false
        } ?: false

    override fun getFileCreator(): ActivityResultContract<Pair<String, String>, Uri?> = creator

    override fun isCreateDocumentProviderExist(fileNameToMimeType: Pair<String, String>): Boolean =
        context.packageManager?.let { packageManager ->
            creator.createIntent(context, fileNameToMimeType).resolveActivity(packageManager)
                ?.let { true } ?: false
        } ?: false

    override fun getFileNameFromUri(uriWrapper: UriWrapper): String =
        FileUtilsHelper.getName(
            context, uriWrapper.uri, uriWrapper.uri.path?.let { File(it).name }
        ) ?: DEFAULT_FILE_NAME

    override fun createTempFile(name: String): File =
        File.createTempFile(
            name,
            TEMP_FILE_EXTENSION
        )

    override fun copyFile(tempFile: File, destinationFileUri: UriWrapper) {

        interruptConversionInCaseOfCancellation()

        tempFile.inputStream().use {
            copyData(it, context.contentResolver.openOutputStream(destinationFileUri.uri))
        }
    }

    private fun copyData(inputStream: InputStream, outputStream: OutputStream?) {
        outputStream?.use {
            val buffer = ByteArray(BUFFER_SIZE)
            var length = inputStream.read(buffer)
            while (length > 0) {

                interruptConversionInCaseOfCancellation()

                outputStream.write(buffer, 0, length)
                length = inputStream.read(buffer)
            }
            outputStream.flush()
        }
    }

    private fun interruptConversionInCaseOfCancellation() {
        if (Thread.currentThread().isInterrupted) throw InterruptedException("Copying file canceled")
    }

    override fun deleteFile(file: File) = file.delete()

    override fun deleteFile(uriWrapper: UriWrapper): Boolean {
        return DocumentsContract.deleteDocument(context.contentResolver, uriWrapper.uri)
    }


}
