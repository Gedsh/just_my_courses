package pan.alexander.fileconverter.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.DialogFragment
import moxy.MvpAppCompatActivity
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import pan.alexander.fileconverter.databinding.ActivityMainBinding
import pan.alexander.fileconverter.ui.dialog.PleaseWaitDialogFragment
import pan.alexander.fileconverter.utils.files.JPEG_MIME_TYPE
import pan.alexander.fileconverter.utils.files.UriWrapper
import pan.alexander.fileconverter.utils.app
import pan.alexander.fileconverter.utils.files.FileUtils
import pan.alexander.fileconverter.utils.files.PNG_MIME_TYPE
import javax.inject.Inject
import javax.inject.Provider


class MainActivity : MvpAppCompatActivity(), MainContract.View,
    PleaseWaitDialogFragment.OnCancelButtonClickListener {

    @InjectPresenter
    lateinit var presenter: MainPresenter

    @Inject
    lateinit var presenterProvider: Provider<MainPresenter>

    @ProvidePresenter
    fun providePresenter(): MainPresenter = presenterProvider.get()

    private lateinit var binding: ActivityMainBinding

    private var pickFileLauncher: ActivityResultLauncher<String>? = null
    private var createFileLauncher: ActivityResultLauncher<Pair<String, String>>? = null

    @Inject
    lateinit var fileUtils: dagger.Lazy<FileUtils>

    override fun onCreate(savedInstanceState: Bundle?) {

        app.daggerComponent.inject(this)

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initPickFileResultListener()

        initCreateFileResultListener()

        initSelectFileButtonClickListener()
    }

    private fun initPickFileResultListener() {
        pickFileLauncher = registerForActivityResult(fileUtils.get().getFilePicker()) { uri ->
            uri?.let {
                presenter.onFileSelected(UriWrapper(it))
            }
        }
    }

    private fun initSelectFileButtonClickListener() {
        binding.selectJpgFileButton.setOnClickListener {
            launchPickJpegFileActivity()
        }
    }

    private fun initCreateFileResultListener() {
        createFileLauncher = registerForActivityResult(fileUtils.get().getFileCreator()) { uri ->
            uri?.let {
                presenter.onFileCreated(UriWrapper(it))
            }
        }
    }

    private fun launchPickJpegFileActivity() {
        if (fileUtils.get().isOpenDocumentProviderExist(JPEG_MIME_TYPE)) {
            pickFileLauncher?.launch(JPEG_MIME_TYPE)
        }
    }

    override fun launchCreateFileActivity(fileName: String) {
        if (fileUtils.get().isCreateDocumentProviderExist(fileName to PNG_MIME_TYPE)) {
            createFileLauncher?.launch(fileName to PNG_MIME_TYPE)
        }
    }

    override fun showDialog(message: PleaseWaitDialogFragment.Message) {
        dismissDialog()
        showPleaseWaitDialog(message)
    }

    override fun dismissDialog() {
        supportFragmentManager.findFragmentByTag(PleaseWaitDialogFragment.TAG)?.let {
            (it as DialogFragment).dismiss()
        }
    }

    private fun showPleaseWaitDialog(message: PleaseWaitDialogFragment.Message) {
        PleaseWaitDialogFragment.newInstance(message)
            .show(supportFragmentManager, PleaseWaitDialogFragment.TAG)
    }

    override fun onCancel(message: PleaseWaitDialogFragment.Message) {
        when (message) {
            PleaseWaitDialogFragment.Message.CONVERTING_FILE -> presenter.onFileConversionCanceled()
            PleaseWaitDialogFragment.Message.SAVING_FILE -> presenter.onFileSavingCancelled()
        }

    }

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

}
