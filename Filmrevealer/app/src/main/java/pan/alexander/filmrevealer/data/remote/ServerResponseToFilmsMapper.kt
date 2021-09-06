package pan.alexander.filmrevealer.data.remote

import pan.alexander.filmrevealer.data.remote.pojo.FilmsPageJson
import pan.alexander.filmrevealer.domain.entities.Film
import javax.inject.Inject

class ServerResponseToFilmsMapper @Inject constructor() {
    fun map(filmsPage: FilmsPageJson, section: Film.Section): List<Film> {
        val results = filmsPage.results
        val films = mutableListOf<Film>()
        if (results.isNotEmpty()) {
            results.forEach { filmDetails ->
                val film = Film(filmDetails).also {
                    it.section = section.value
                    it.page = filmsPage.page
                    it.totalPages = filmsPage.totalPages
                }

                films.add(film)
            }
        }
        return films
    }
}
