package pan.alexander.githubclient.ui.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import pan.alexander.githubclient.databinding.FragmentUsersBinding
import pan.alexander.githubclient.ui.MainActivity
import pan.alexander.githubclient.ui.base.BaseFragment
import pan.alexander.githubclient.ui.users.adapter.UsersAdapter
import pan.alexander.githubclient.utils.image.ImageLoader
import javax.inject.Inject
import javax.inject.Provider

class UsersFragment : BaseFragment(), UsersContract.View {

    @InjectPresenter
    lateinit var presenter: UsersPresenter

    @Inject
    lateinit var presenterProvider: Provider<UsersPresenter>

    @ProvidePresenter
    fun providePresenter(): UsersPresenter = presenterProvider.get()

    @Inject
    lateinit var imageLoader: ImageLoader<ImageView>

    private val binding get() = baseBinding as FragmentUsersBinding

    private var adapter: UsersAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        (activity as MainActivity).mainComponent.inject(this)

        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        baseBinding = FragmentUsersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initRecycler() {
        binding.usersRecycler.layoutManager = LinearLayoutManager(context)
        adapter = UsersAdapter(presenter.usersListPresenter, imageLoader)
        binding.usersRecycler.adapter = adapter
    }

    override fun setState(state: UsersContract.ViewState) {
        when (state) {
            is UsersContract.ViewState.Idle -> setIdleState()
            is UsersContract.ViewState.Loading -> setLoadingState()
            is UsersContract.ViewState.Success -> setSuccessState()
        }
    }

    private fun setIdleState() = with(binding) {
        usersRecycler.visibility = View.VISIBLE
        usersLoadingProgressBar.visibility = View.GONE
    }

    private fun setLoadingState() = with(binding) {
        usersRecycler.visibility = View.GONE
        usersLoadingProgressBar.visibility = View.VISIBLE
    }

    private fun setSuccessState() = with(binding) {
        usersRecycler.visibility = View.VISIBLE
        usersLoadingProgressBar.visibility = View.GONE
    }

    override fun updateUsers() {
        adapter?.notifyDataSetChanged()
    }

    override fun onBackPressed() = presenter.onBackPressed()

    companion object {
        fun newInstance() = UsersFragment()
    }
}
