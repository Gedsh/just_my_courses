package pan.alexander.githubclient.ui.userrepos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import pan.alexander.githubclient.databinding.FragmentReposBinding
import pan.alexander.githubclient.domain.entities.UserGithubRepo
import pan.alexander.githubclient.ui.MainActivity
import pan.alexander.githubclient.ui.base.BaseFragment
import pan.alexander.githubclient.ui.userrepos.adapter.ReposAdapter
import javax.inject.Inject
import javax.inject.Provider

class ReposFragment : BaseFragment(), ReposContract.View {

    @InjectPresenter
    lateinit var presenter: ReposPresenter

    @Inject
    lateinit var presenterProvider: Provider<ReposPresenter>

    @ProvidePresenter
    fun providePresenter(): ReposPresenter = presenterProvider.get()

    private var adapter: ReposAdapter? = null

    private val binding get() = baseBinding as FragmentReposBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        (activity as MainActivity).mainComponent.inject(this)

        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        baseBinding = FragmentReposBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initRecycler() {
        binding.reposRecycler.layoutManager = LinearLayoutManager(context)
        adapter = ReposAdapter { presenter.onRepoClicked(it) }
        binding.reposRecycler.adapter = adapter
    }

    override fun getUserData() {
        arguments?.let {
            val userId = it.getLong(USER_ID_ARG)
            val reposUrl = it.getString(USER_REPOS_URL_ARG) ?: ""
            presenter.onUserDataReady(userId, reposUrl)
        }
    }

    override fun setState(state: ReposContract.ViewState) {

        when (state) {
            is ReposContract.ViewState.Idle -> setIdleState()
            is ReposContract.ViewState.Loading -> setLoadingState()
            is ReposContract.ViewState.Success -> setSuccessState()
        }
    }

    private fun setIdleState() = with(binding) {
        reposRecycler.visibility = View.VISIBLE
        reposLoadingProgressBar.visibility = View.GONE
    }

    private fun setLoadingState() = with(binding) {
        reposRecycler.visibility = View.GONE
        reposLoadingProgressBar.visibility = View.VISIBLE
    }

    private fun setSuccessState() = with(binding) {
        reposRecycler.visibility = View.VISIBLE
        reposLoadingProgressBar.visibility = View.GONE
    }

    override fun updateRepos(repos: List<UserGithubRepo>) {
        adapter?.updateRepos(repos)
    }

    override fun onBackPressed() = presenter.onBackPressed()

    companion object {
        private const val USER_ID_ARG = "USER_ID"
        private const val USER_REPOS_URL_ARG = "USER_REPOS_URL"

        fun newInstance(userId: Long, userReposUrl: String): ReposFragment =
            ReposFragment().apply {
                arguments = Bundle().apply {
                    putLong(USER_ID_ARG, userId)
                    putString(USER_REPOS_URL_ARG, userReposUrl)
                }
            }
    }
}
