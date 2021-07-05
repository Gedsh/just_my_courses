package pan.alexander.filmrevealer.domain

import pan.alexander.filmrevealer.data.web.pojo.FilmsPage
import retrofit2.Call

interface RemoteRepository {
    fun loadNowPlayingFilms(page: Int): Call<FilmsPage>
    fun loadUpcomingFilms(page: Int): Call<FilmsPage>
    fun loadTopRatedFilms(page: Int): Call<FilmsPage>
    fun loadPopularFilms(page: Int): Call<FilmsPage>
}
