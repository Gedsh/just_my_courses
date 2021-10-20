package pan.alexander.fileconverter.data

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import pan.alexander.fileconverter.domain.MainRepository
import pan.alexander.fileconverter.utils.eventbus.AppError
import pan.alexander.fileconverter.utils.eventbus.EventBus
import pan.alexander.fileconverter.utils.files.FileUtils
import pan.alexander.fileconverter.utils.files.UriWrapper
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val TEST_DELAY_SEC = 5L

class MainRepositoryImpl @Inject constructor(
    private val mainDataSource: MainDataSource,
    private val eventBus: EventBus<AppError>,
    private val fileUtils: FileUtils
) : MainRepository {
    override fun createTempFile(name: String): Single<File> =
        Single.fromCallable { mainDataSource.createTempFile(name) }

    override fun convertJpegToPng(jpegFileUri: UriWrapper, tempPngFile: File): Single<File> =
        Single.fromCallable { mainDataSource.convertJpegToPng(jpegFileUri, tempPngFile) }

    override fun copyTempFile(tempFile: File, destinationFileUri: UriWrapper): Completable =
        Completable.fromAction {
            try {
                TimeUnit.SECONDS.sleep(TEST_DELAY_SEC)
                mainDataSource.copyFile(tempFile, destinationFileUri)
            } catch (e: InterruptedException) {
                eventBus.post(AppError("Saving file canceled", e))
            }
        }

    override fun deleteTempFile(tempFile: File): Completable =
        Completable.fromAction {
            if (!mainDataSource.deleteTempFile(tempFile)) {
                throw IOException("Unable to delete file ${tempFile.name}")
            }
        }

    override fun deleteFile(uriWrapper: UriWrapper?): Completable =
        Completable.fromAction {
            uriWrapper?.let {
                if (!mainDataSource.deleteFile(uriWrapper)) {
                    throw IOException(
                        "Unable to delete file ${fileUtils.getFileNameFromUri(uriWrapper)}"
                    )
                }
            } ?: throw IOException("Unable to delete file null")
        }

}
