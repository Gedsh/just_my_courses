package pan.alexander.githubclient.ui.userrepos

import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.kotlin.subscribeBy
import pan.alexander.githubclient.domain.NetworkRepository
import pan.alexander.githubclient.domain.entities.UserGithubRepo
import pan.alexander.githubclient.domain.repos.UserReposInteractor
import pan.alexander.githubclient.utils.eventbus.Error
import pan.alexander.githubclient.utils.eventbus.EventBus
import pan.alexander.githubclient.utils.logger.AppLogger
import pan.alexander.githubclient.utils.navigation.Screens
import javax.inject.Inject

class ReposPresenter @Inject constructor(
    private val userReposInteractor: UserReposInteractor,
    private val networkRepository: NetworkRepository,
    private val router: Router,
    private val screens: Screens,
    private val errorEventBus: EventBus<Error>
) : ReposContract.Presenter(router) {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        viewState.setState(ReposContract.ViewState.Idle)

        initRecycler()

        getUserData()
    }

    private fun initRecycler() {
        viewState.initRecycler()
    }

    private fun getUserData() {
        viewState.getUserData()
    }

    override fun onUserDataReady(userId: Long, reposUrl: String) {
        getRepos(userId)

        if (networkRepository.isInternetAvailable()) {
            updateRepos(reposUrl)
        }
    }

    private fun getRepos(userId: Long) {
        userReposInteractor.getUserRepos(userId)
            .doOnError { throwable ->
                throwable.message?.let { errorEventBus.post(Error(it)) }
                viewState.setState(ReposContract.ViewState.Idle)
            }
            .subscribeBy(
                onNext = {
                    if (it.isNotEmpty()) {
                        viewState.updateRepos(it)
                        viewState.setState(ReposContract.ViewState.Success)
                    } else {
                        viewState.setState(ReposContract.ViewState.Loading)
                    }
                },
                onError = {
                    AppLogger.logE("Get user repos failure", it)
                }
            ).autoDispose()
    }

    private fun updateRepos(reposUrl: String) {
        userReposInteractor.updateUserRepos(reposUrl)
            .doOnError { throwable ->
                throwable.message?.let { errorEventBus.post(Error(it)) }
                viewState.setState(ReposContract.ViewState.Idle)
            }
            .subscribeBy(
                onComplete = {
                    viewState.setState(ReposContract.ViewState.Success)
                },
                onError = {
                    AppLogger.logE("Update user repos failure", it)
                }
            ).autoDispose()
    }

    override fun onRepoClicked(repo: UserGithubRepo) {
        router.navigateTo(screens.repoDetails(repo))
    }
}
