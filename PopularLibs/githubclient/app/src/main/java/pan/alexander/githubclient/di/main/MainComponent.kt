package pan.alexander.githubclient.di.main

import dagger.Subcomponent
import pan.alexander.githubclient.di.ActivityScope
import pan.alexander.githubclient.ui.MainActivity
import pan.alexander.githubclient.ui.repodetails.RepoDetailsFragment
import pan.alexander.githubclient.ui.userrepos.ReposFragment
import pan.alexander.githubclient.ui.users.UsersFragment

@ActivityScope
@Subcomponent(modules = [MainPresentersModule::class, MainInteractorsModule::class])
interface MainComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): MainComponent
    }

    fun inject(mainMainActivity: MainActivity)
    fun inject(usersFragment: UsersFragment)
    fun inject(reposFragment: ReposFragment)
    fun inject(repoDetailsFragment: RepoDetailsFragment)
}
