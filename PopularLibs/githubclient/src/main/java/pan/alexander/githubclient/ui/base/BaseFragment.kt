package pan.alexander.githubclient.ui.base

import androidx.viewbinding.ViewBinding
import moxy.MvpAppCompatFragment
import pan.alexander.githubclient.ui.OnBackButtonListener

abstract class BaseFragment : MvpAppCompatFragment(), OnBackButtonListener {
    var baseBinding: ViewBinding? = null

    override fun onDestroyView() {
        super.onDestroyView()

        baseBinding = null
    }
}
