package pan.alexander.fileconverter.ui

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.plusAssign
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.subjects.PublishSubject
import moxy.InjectViewState
import pan.alexander.fileconverter.domain.MainInteractor
import pan.alexander.fileconverter.ui.dialog.PleaseWaitDialogFragment
import pan.alexander.fileconverter.utils.eventbus.EventBus
import pan.alexander.fileconverter.utils.files.FileUtils
import pan.alexander.fileconverter.utils.files.UriWrapper
import pan.alexander.fileconverter.utils.logger.AppLogger
import pan.alexander.fileconverter.utils.rxschedulers.SchedulersProvider
import pan.alexander.fileconverter.utils.eventbus.AppError
import java.io.File
import java.lang.IllegalStateException
import javax.inject.Inject

@InjectViewState
class MainPresenter @Inject constructor(
    private val mainInteractor: MainInteractor,
    private val fileUtils: FileUtils,
    private val errorEventBus: EventBus<AppError>,
    private val schedulers: SchedulersProvider
) : MainContract.Presenter() {

    private val disposables by lazy { CompositeDisposable() }

    private val openFileSubject by lazy { PublishSubject.create<UriWrapper>() }
    private val createFileSubject by lazy { PublishSubject.create<UriWrapper>() }

    private var currentFileName: String? = null
    private var currentTempFile: File? = null
    private var destinationFileUri: UriWrapper? = null

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        initErrorListener()

        observeFileOpened()
        observeFileCreated()
    }

    private fun initErrorListener() {
        errorEventBus.get()
            .observeOn(schedulers.ui())
            .subscribeBy(
                onNext = {
                    AppLogger.logE(it.message, it.throwable)
                    viewState.showError(it.message)
                }
            ).autoDispose()
    }

    private fun observeFileOpened() {
        openFileSubject.switchMapSingle {
            if (it.isTaskCancellationUri()) {
                viewState.dismissDialog()
                clearCurrentData()
                Single.never()
            } else {
                viewState.showDialog(PleaseWaitDialogFragment.Message.CONVERTING_FILE)
                val fileName = fileUtils.getFileNameFromUri(it)
                    .replace(Regex("(\\.jpg|\\.jpeg)$"), ".png")
                currentFileName = fileName
                convertJpegToPng(it, fileName)
            }
        }
            .subscribeBy(
                onNext = { file ->
                    currentTempFile = file
                    currentFileName?.let { viewState.launchCreateFileActivity(it) }
                    viewState.dismissDialog()
                },
                onError = {
                    viewState.dismissDialog()
                    errorEventBus.post(AppError("Unable to open file $currentFileName", it))
                    clearCurrentData()
                }
            ).autoDispose()
    }

    private fun convertJpegToPng(uriWrapper: UriWrapper, tempFileName: String) =
        mainInteractor.getConvertedPngFromJpegUri(uriWrapper, tempFileName)

    override fun onFileSelected(uriWrapper: UriWrapper) {
        openFileSubject.onNext(uriWrapper)
    }

    override fun onFileCreated(uriWrapper: UriWrapper) {
        createFileSubject.onNext(uriWrapper)
    }

    private fun observeFileCreated() {
        createFileSubject.switchMapCompletable { uriWrapper ->
            if (uriWrapper.isTaskCancellationUri()) {
                currentTempFile?.let {
                    mainInteractor.deleteTempFile(it)
                        .andThen(mainInteractor.deleteFile(destinationFileUri))
                } ?: Completable.complete()
            } else {
                destinationFileUri = uriWrapper
                viewState.showDialog(PleaseWaitDialogFragment.Message.SAVING_FILE)
                currentTempFile?.let { file ->
                    mainInteractor.moveTempFile(file, uriWrapper)
                        .doOnComplete {
                            viewState.dismissDialog()
                            clearCurrentData()
                        }
                } ?: Completable.error(IllegalStateException("Current png temp file is null"))
            }
        }
            .subscribeBy(
                onComplete = {
                    viewState.dismissDialog()
                    clearCurrentData()
                },
                onError = {
                    viewState.dismissDialog()
                    errorEventBus.post(AppError("Unable to process file $currentFileName", it))
                    clearCurrentData()
                }
            ).autoDispose()
    }

    override fun onFileConversionCanceled() {
        openFileSubject.onNext(UriWrapper.getTaskCancellationUri())
    }

    override fun onFileSavingCancelled() {
        createFileSubject.onNext(UriWrapper.getTaskCancellationUri())
    }

    private fun clearCurrentData() {
        currentFileName = null
        currentTempFile = null
        destinationFileUri = null
    }

    private fun Disposable.autoDispose() {
        disposables += this
    }

    override fun onDestroy() {
        disposables.clear()

        super.onDestroy()
    }
}
