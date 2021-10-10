package pan.alexander.githubclient.ui.userdetails

import com.github.terrakok.cicerone.Router
import javax.inject.Inject

class DetailsPresenter @Inject constructor(
    router: Router
): DetailsContract.Presenter(router) {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        viewState.showUserDetails()
    }

}
