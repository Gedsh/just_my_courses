package pan.alexander.filmrevealer.presentation.fragments

import android.view.View
import androidx.fragment.app.Fragment
import pan.alexander.filmrevealer.R
import pan.alexander.filmrevealer.presentation.Failure
import pan.alexander.filmrevealer.presentation.viewmodels.BaseViewModel
import pan.alexander.filmrevealer.showSnackBar
import pan.alexander.filmrevealer.utils.Utils

abstract class BaseFragment : Fragment() {

    protected fun observeLoadingFailure(viewModel: BaseViewModel, view: View) {
        viewModel.failureLiveData.observe(viewLifecycleOwner, { failure ->

            context?.let { context ->
                if (Utils.isInternetAvailable(context)) {
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
}
