package pan.alexander.fileconverter.domain

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import pan.alexander.fileconverter.utils.files.UriWrapper
import java.io.File

interface MainRepository {
    fun createTempFile(name: String): Single<File>
    fun convertJpegToPng(jpegFileUri: UriWrapper, tempPngFile: File): Single<File>
    fun copyTempFile(tempFile: File, destinationFileUri: UriWrapper): Completable
    fun deleteTempFile(tempFile: File): Completable
    fun deleteFile(uriWrapper: UriWrapper?): Completable
}
