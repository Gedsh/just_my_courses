package pan.alexander.githubclient.di.app

import dagger.Binds
import dagger.Module
import pan.alexander.githubclient.utils.navigation.ApplicationScreens
import pan.alexander.githubclient.utils.navigation.Screens

@Module
abstract class NavigationModule {

    @Binds
    abstract fun provideScreens(screens: ApplicationScreens): Screens
}
