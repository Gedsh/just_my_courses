package pan.alexander.fileconverter.utils.files

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.*
import javax.inject.Inject

private const val IMAGE_COMPRESS_QUALITY = 100

class FileConverter @Inject constructor(
    private val context: Context,
) {
    fun convertJpegToPng(jpegFileUri: UriWrapper, pngFile: File): File =
        context.contentResolver.openFileDescriptor(jpegFileUri.uri, "r")?.use {
            val fileDescriptor: FileDescriptor = it.fileDescriptor
            val image: Bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor)

            pngFile.also { file ->
                file.outputStream().use { tempFileOutputStream ->
                    image.compress(
                        Bitmap.CompressFormat.PNG,
                        IMAGE_COMPRESS_QUALITY,
                        tempFileOutputStream
                    )
                }
            }

        } ?: throw IOException("Unable to convert file ${jpegFileUri.uri}")

}
