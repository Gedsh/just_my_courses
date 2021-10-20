package pan.alexander.fileconverter.domain

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import pan.alexander.fileconverter.utils.files.UriWrapper
import pan.alexander.fileconverter.utils.rxschedulers.SchedulersProvider
import java.io.File
import javax.inject.Inject

class MainInteractorImpl @Inject constructor(
    private val mainRepository: MainRepository,
    private val schedulers: SchedulersProvider
) : MainInteractor {

    override fun getConvertedPngFromJpegUri(
        sourceFileUri: UriWrapper,
        destinationTempFileName: String
    ): Single<File> = mainRepository.createTempFile(destinationTempFileName)
        .flatMap { mainRepository.convertJpegToPng(sourceFileUri, it) }
        .subscribeOn(schedulers.io())
        .observeOn(schedulers.ui())

    override fun moveTempFile(
        tempFile: File,
        destinationFileUri: UriWrapper
    ): Completable = mainRepository.copyTempFile(tempFile, destinationFileUri)
        .andThen(mainRepository.deleteTempFile(tempFile))
        .subscribeOn(schedulers.io())
        .observeOn(schedulers.ui())

    override fun deleteTempFile(tempFile: File): Completable =
        mainRepository.deleteTempFile(tempFile)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())

    override fun deleteFile(uriWrapper: UriWrapper?): Completable =
        mainRepository.deleteFile(uriWrapper)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())

}
