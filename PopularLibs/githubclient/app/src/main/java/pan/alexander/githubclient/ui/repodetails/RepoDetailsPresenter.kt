package pan.alexander.githubclient.ui.repodetails

import com.github.terrakok.cicerone.Router
import javax.inject.Inject

class RepoDetailsPresenter @Inject constructor(
    router: Router
) : RepoDetailsContract.Presenter(router) {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        viewState.showRepoDetails()
    }

}
