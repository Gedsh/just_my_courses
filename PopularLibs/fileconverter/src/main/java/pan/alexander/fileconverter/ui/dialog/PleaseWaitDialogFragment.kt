package pan.alexander.fileconverter.ui.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import pan.alexander.fileconverter.R

class PleaseWaitDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?) =
        AlertDialog.Builder(requireContext())
            .setView(
                ProgressBar(requireContext(), null, android.R.attr.progressBarStyleInverse).apply {
                    isIndeterminate = true
                }
            )
            .setTitle(R.string.please_wait_title)
            .setMessage(
                when (arguments?.getSerializable(MESSAGE_ARG)) {
                    Message.CONVERTING_FILE -> R.string.converting_file_message
                    Message.SAVING_FILE -> R.string.saving_file_message
                    else -> R.string.please_wait_title
                }
            )
            .setNegativeButton(R.string.cancel) { _: DialogInterface, _: Int ->
                (activity as? OnCancelButtonClickListener)?.let {
                    arguments?.getSerializable(MESSAGE_ARG)?.let { message ->
                        it.onCancel(message as Message)
                    }
                }
            }
            .create()
            .apply {
                isCancelable = false
                setCanceledOnTouchOutside(false)
            }


    companion object {
        fun newInstance(message: Message) = PleaseWaitDialogFragment().apply {
            arguments = Bundle().apply {
                putSerializable(MESSAGE_ARG, message)
            }
        }

        private const val MESSAGE_ARG = "MESSAGE"

        const val TAG = "PleaseWaitDialogFragment"
    }

    interface OnCancelButtonClickListener {
        fun onCancel(message: Message)
    }

    enum class Message {
        CONVERTING_FILE,
        SAVING_FILE
    }

}
