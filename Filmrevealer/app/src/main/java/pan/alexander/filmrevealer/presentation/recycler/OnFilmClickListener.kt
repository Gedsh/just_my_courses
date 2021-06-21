package pan.alexander.filmrevealer.presentation.recycler

import android.view.View
import pan.alexander.filmrevealer.domain.entities.Film

interface OnFilmClickListener {
    fun onFilmClicked(view: View?, film: Film)
}
