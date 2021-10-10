package pan.alexander.githubclient.ui.userdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import pan.alexander.githubclient.databinding.FragmentDetailsBinding
import pan.alexander.githubclient.domain.entities.GithubUser
import pan.alexander.githubclient.ui.base.BaseFragment
import pan.alexander.githubclient.utils.app
import javax.inject.Inject
import javax.inject.Provider

class DetailsFragment : BaseFragment(), DetailsContract.View {

    @InjectPresenter
    lateinit var presenter: DetailsPresenter

    @Inject
    lateinit var presenterProvider: Provider<DetailsPresenter>

    @ProvidePresenter
    fun providePresenter(): DetailsPresenter = presenterProvider.get()

    private val binding get() = baseBinding as FragmentDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        requireContext().app.daggerComponent.inject(this)

        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        baseBinding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onBackPressed() = presenter.onBackPressed()

    override fun showUserDetails() {
        arguments?.getParcelable<GithubUser>(USER_BUNDLE)?.let {
            binding.userDetailsLoginTextView.text = it.login
        }
    }

    companion object {
        fun newInstance(user: GithubUser) = DetailsFragment().apply {
            arguments = Bundle().apply {
                putParcelable(USER_BUNDLE, user)
            }
        }

        private const val USER_BUNDLE = "USER_BUNDLE"
    }

}
