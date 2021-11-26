package pan.alexander.githubclient.ui.repodetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import com.google.android.material.chip.Chip
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import pan.alexander.githubclient.databinding.FragmentDetailsBinding
import pan.alexander.githubclient.domain.entities.UserGithubRepo
import pan.alexander.githubclient.ui.MainActivity
import pan.alexander.githubclient.ui.base.BaseFragment
import javax.inject.Inject
import javax.inject.Provider

class RepoDetailsFragment : BaseFragment(), RepoDetailsContract.View {

    @InjectPresenter
    lateinit var presenter: RepoDetailsPresenter

    @Inject
    lateinit var presenterProvider: Provider<RepoDetailsPresenter>

    @ProvidePresenter
    fun providePresenter(): RepoDetailsPresenter = presenterProvider.get()

    private val binding get() = baseBinding as FragmentDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        (activity as MainActivity).mainComponent.inject(this)

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

    override fun showRepoDetails() {
        arguments?.getParcelable<UserGithubRepo>(REPO_DETAILS_ARG)?.let {
            with(binding) {
                repoDetailsNameTextView.text = it.name
                repoDetailsDescriptionTextView.text = it.description
                repoDetailsCreatedValueTextView.text = it.createdAt
                repoDetailsUpdatedValueTextView.text = it.updatedAt
                repoDetailsForksValueTextView.text = it.forksCount.toString()
                repoDetailsIssuesValueTextView.text = it.openIssuesCount.toString()
                repoDetailsWatchersValueTextView.text = it.watchers.toString()
            }

            if (it.description.isBlank()) {
                binding.repoDetailsDescriptionTextView.visibility = GONE
            }

            addTopicChips(it.topics)
        }
    }

    private fun addTopicChips(topics: List<String>) {
        topics.forEach {
            binding.repoDetailsTopicsChipGroup.addView(
                Chip(context).apply {
                    this.text = it
                }
            )
        }
    }

    companion object {
        private const val REPO_DETAILS_ARG = "REPO_DETAILS"

        fun newInstance(repo: UserGithubRepo) = RepoDetailsFragment().apply {
            arguments = Bundle().apply {
                putParcelable(REPO_DETAILS_ARG, repo)
            }
        }
    }

}
