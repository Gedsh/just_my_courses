package pan.alexander.fileconverter.data

import pan.alexander.fileconverter.utils.files.FileConverter
import pan.alexander.fileconverter.utils.files.FileUtils
import pan.alexander.fileconverter.utils.files.UriWrapper
import java.io.File
import javax.inject.Inject

class MainDataSourceImpl @Inject constructor(
    private val fileConverter: FileConverter,
    private val fileUtils: FileUtils
) : MainDataSource {

    override fun createTempFile(name: String): File =
        fileUtils.createTempFile(name)

    override fun convertJpegToPng(jpegFileUri: UriWrapper, tempPngFile: File) =
        fileConverter.convertJpegToPng(jpegFileUri, tempPngFile)

    override fun copyFile(tempFile: File, destinationFileUri: UriWrapper) =
        fileUtils.copyFile(tempFile, destinationFileUri)

    override fun deleteTempFile(file: File): Boolean =
        fileUtils.deleteFile(file)

    override fun deleteFile(uriWrapper: UriWrapper): Boolean =
        fileUtils.deleteFile(uriWrapper)
}
