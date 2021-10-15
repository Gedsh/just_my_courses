package pan.alexander.githubclient.di

import dagger.Component
import pan.alexander.githubclient.ui.MainActivity
import pan.alexander.githubclient.ui.userdetails.DetailsFragment
import pan.alexander.githubclient.ui.users.UsersFragment
import javax.inject.Singleton

@Singleton
@Component(
    modules = [RepositoryModule::class, InteractorsModule::class, CiceroneModule::class,
        NavigationModule::class, PresentersModule::class, EventBusModule::class]
)
interface ApplicationComponent {
    fun inject(mainMainActivity: MainActivity)
    fun inject(usersFragment: UsersFragment)
    fun inject(detailsFragment: DetailsFragment)
}
