package pan.alexander.githubclient.ui.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import pan.alexander.githubclient.databinding.FragmentUsersBinding
import pan.alexander.githubclient.ui.base.BaseFragment
import pan.alexander.githubclient.ui.users.adapter.UsersAdapter
import pan.alexander.githubclient.utils.app
import javax.inject.Inject
import javax.inject.Provider

class UsersFragment : BaseFragment(), UsersContract.View {

    @InjectPresenter
    lateinit var presenter: UsersPresenter

    @Inject
    lateinit var presenterProvider: Provider<UsersPresenter>

    @ProvidePresenter
    fun providePresenter(): UsersPresenter = presenterProvider.get()

    private val binding get() = baseBinding as FragmentUsersBinding

    private var adapter: UsersAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        requireContext().app.daggerComponent.inject(this)

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
        adapter = UsersAdapter(presenter.usersListPresenter)
        binding.usersRecycler.adapter = adapter
    }

    override fun updateUsers() {
        adapter?.notifyDataSetChanged()
    }

    override fun onBackPressed() = presenter.onBackPressed()

    companion object {
        fun newInstance() = UsersFragment()
    }
}
