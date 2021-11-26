package pan.alexander.githubclient.utils.navigation

import com.github.terrakok.cicerone.Screen
import com.github.terrakok.cicerone.androidx.FragmentScreen
import pan.alexander.githubclient.domain.entities.UserGithubRepo
import pan.alexander.githubclient.ui.repodetails.RepoDetailsFragment
import pan.alexander.githubclient.ui.userrepos.ReposFragment
import pan.alexander.githubclient.ui.users.UsersFragment
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApplicationScreens @Inject constructor() : Screens {

    override fun users(): Screen = FragmentScreen { UsersFragment.newInstance() }

    override fun userRepos(userId: Long, reposUrl: String): Screen = FragmentScreen {
        ReposFragment.newInstance(userId, reposUrl)
    }

    override fun repoDetails(repo: UserGithubRepo): Screen = FragmentScreen {
        RepoDetailsFragment.newInstance(repo)
    }

}
