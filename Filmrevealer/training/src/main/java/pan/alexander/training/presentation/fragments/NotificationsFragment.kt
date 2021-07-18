package pan.alexander.training.presentation.fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import pan.alexander.training.databinding.FragmentNotificationsBinding

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                if (it.action == FIREBASE_ACTION) {
                    it.getStringExtra(EXTRA_PARAM_TITLE)
                        ?.takeIf { title -> title.isNotBlank() }
                        ?.let { title ->
                            binding.firebaseMessageTitle.text = title
                        }
                    it.getStringExtra(EXTRA_PARAM_MESSAGE)
                        ?.takeIf { message -> message.isNotBlank() }
                        ?.let { message ->
                            binding.firebaseMessage.text = message
                        }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerFirebaseBroadcastReceiver()
    }

    private fun registerFirebaseBroadcastReceiver() {
        context?.let {
            LocalBroadcastManager.getInstance(it)
                .registerReceiver(broadcastReceiver, IntentFilter(FIREBASE_ACTION))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        unregisterFirebaseBroadcastReceiver()

        _binding = null
    }

    private fun unregisterFirebaseBroadcastReceiver() {
        context?.let {
            LocalBroadcastManager.getInstance(it)
                .unregisterReceiver(broadcastReceiver)
        }
    }

    companion object {
        const val FIREBASE_ACTION =
            "pan.alexander.training.presentation.fragments.action.FIREBASE_ACTION"

        const val EXTRA_PARAM_TITLE =
            "pan.alexander.training.presentation.fragments.extra.PARAM_TITLE"

        const val EXTRA_PARAM_MESSAGE =
            "pan.alexander.training.presentation.fragments.extra.PARAM_MESSAGE"
    }
}
