package pan.alexander.filmrevealer.data.remote

import pan.alexander.filmrevealer.data.remote.pojo.FilmPreciseDetailsJson
import pan.alexander.filmrevealer.domain.entities.FilmDetails
import javax.inject.Inject

class ServerResponseToFilmDetailsMapper @Inject constructor() {
    fun map(details: FilmPreciseDetailsJson) = FilmDetails(details)
}
