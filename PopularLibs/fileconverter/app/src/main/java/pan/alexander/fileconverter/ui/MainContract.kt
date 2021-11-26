package pan.alexander.fileconverter.ui

import moxy.MvpPresenter
import moxy.MvpView
import moxy.viewstate.strategy.alias.Skip
import pan.alexander.fileconverter.ui.dialog.PleaseWaitDialogFragment
import pan.alexander.fileconverter.utils.files.UriWrapper

class MainContract {

    interface View : MvpView {
        @Skip
        fun launchCreateFileActivity(fileName: String)

        @Skip
        fun showDialog(message: PleaseWaitDialogFragment.Message)

        @Skip
        fun dismissDialog()

        @Skip
        fun showError(message: String)
    }

    abstract class Presenter : MvpPresenter<View>() {
        abstract fun onFileSelected(uriWrapper: UriWrapper)
        abstract fun onFileCreated(uriWrapper: UriWrapper)
        abstract fun onFileConversionCanceled()
        abstract fun onFileSavingCancelled()
    }
}
