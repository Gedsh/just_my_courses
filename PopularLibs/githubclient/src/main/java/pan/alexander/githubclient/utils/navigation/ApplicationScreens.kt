package pan.alexander.githubclient.utils.navigation

import com.github.terrakok.cicerone.Screen
import com.github.terrakok.cicerone.androidx.FragmentScreen
import pan.alexander.githubclient.domain.entities.GithubUser
import pan.alexander.githubclient.ui.userdetails.DetailsFragment
import pan.alexander.githubclient.ui.users.UsersFragment
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApplicationScreens @Inject constructor() : Screens {

    override fun users(): Screen = FragmentScreen { UsersFragment.newInstance() }

    override fun userDetails(user: GithubUser): Screen = FragmentScreen {
        DetailsFragment.newInstance(user)
    }
}
