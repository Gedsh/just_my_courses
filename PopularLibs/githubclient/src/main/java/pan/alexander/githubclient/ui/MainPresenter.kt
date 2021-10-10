package pan.alexander.githubclient.ui

import com.github.terrakok.cicerone.Router
import pan.alexander.githubclient.utils.navigation.Screens
import javax.inject.Inject

class MainPresenter @Inject constructor(
    val router: Router,
    val screens: Screens
) : MainContract.Presenter() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        router.replaceScreen(screens.users())
    }

    override fun onBackPressed() {
        router.exit()
    }
}
