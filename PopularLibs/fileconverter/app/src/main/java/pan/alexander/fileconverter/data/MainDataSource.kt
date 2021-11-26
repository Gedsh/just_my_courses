package pan.alexander.fileconverter.data

import pan.alexander.fileconverter.utils.files.UriWrapper
import java.io.File

interface MainDataSource {
    fun createTempFile(name: String): File
    fun convertJpegToPng(jpegFileUri: UriWrapper, tempPngFile: File): File
    fun copyFile(tempFile: File, destinationFileUri: UriWrapper)
    fun deleteTempFile(file: File): Boolean
    fun deleteFile(uriWrapper: UriWrapper): Boolean
}
