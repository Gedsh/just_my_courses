package pan.alexander.filmrevealer.presentation.fragments

import android.view.View
import androidx.fragment.app.Fragment
import pan.alexander.filmrevealer.R
import pan.alexander.filmrevealer.presentation.Failure
import pan.alexander.filmrevealer.presentation.viewmodels.BaseViewModel
import pan.alexander.filmrevealer.utils.showSnackBar
import pan.alexander.filmrevealer.utils.ConnectionUtils
import pan.alexander.filmrevealer.utils.FILMS_UPDATE_DEFAULT_PERIOD_MILLISECONDS

abstract class BaseFragment : Fragment() {

    protected fun observeLoadingFailure(viewModel: BaseViewModel, view: View) {
        viewModel.failureLiveData.observe(viewLifecycleOwner, { failure ->

            context?.let { context ->
                if (ConnectionUtils.isInternetAvailable(context)) {
                    when (failure) {
                        is Failure.WithMessageAndAction ->
                            failure.message.takeIf { it.isNotBlank() }?.let { message ->
                                view.showSnackBar(message, R.string.retry, {
                                    failure.block()
                                })
                            }

                        is Failure.WithMessage ->
                            failure.message.takeIf { it.isNotBlank() }?.let { message ->
                                view.showSnackBar(message)
                            }
                    }
                }
            }
        })
    }

    protected fun isUpdateRequired(filmTimestamp: Long): Boolean {
        return System.currentTimeMillis() - filmTimestamp > FILMS_UPDATE_DEFAULT_PERIOD_MILLISECONDS.toLong()
    }
}
