package pan.alexander.filmrevealer.presentation.fragments

import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import pan.alexander.filmrevealer.R
import pan.alexander.filmrevealer.domain.entities.Film
import pan.alexander.filmrevealer.presentation.viewmodels.BaseViewModel

abstract class FilmsBaseFragment : BaseFragment() {
    private val baseViewModel by lazy {
        ViewModelProvider(this).get(BaseViewModel::class.java)
    }

    protected val onFilmClickListener by lazy {
        fun(view: View?, film: Film) {
            val bundle = bundleOf(FilmDetailsFragment.BUNDLE_EXTRA to film)
            view?.findNavController()?.navigate(R.id.navigation_to_film_details, bundle)
        }
    }

    protected val onLikeClickListener by lazy {
        fun(film: Film) {
            baseViewModel.toggleFilmLike(film)
        }
    }
}
