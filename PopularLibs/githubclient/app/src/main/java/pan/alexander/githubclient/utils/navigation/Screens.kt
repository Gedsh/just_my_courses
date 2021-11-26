package pan.alexander.githubclient.utils.navigation

import com.github.terrakok.cicerone.Screen
import pan.alexander.githubclient.domain.entities.UserGithubRepo

interface Screens {
    fun users(): Screen
    fun userRepos(userId: Long, reposUrl: String): Screen
    fun repoDetails(repo: UserGithubRepo): Screen
}
