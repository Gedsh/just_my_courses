package pan.alexander.fileconverter.domain

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import pan.alexander.fileconverter.utils.files.UriWrapper
import java.io.File

interface MainInteractor {

    fun getConvertedPngFromJpegUri(
        sourceFileUri: UriWrapper,
        destinationTempFileName: String
    ): Single<File>

    fun moveTempFile(
        tempFile: File,
        destinationFileUri: UriWrapper
    ): Completable

    fun deleteTempFile(tempFile: File): Completable

    fun deleteFile(uriWrapper: UriWrapper?): Completable
}
