package pan.alexander.githubclient.utils.navigation

import com.github.terrakok.cicerone.Screen
import pan.alexander.githubclient.domain.entities.GithubUser

interface Screens {
    fun users(): Screen
    fun userDetails(user: GithubUser): Screen
}
