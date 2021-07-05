package pan.alexander.filmrevealer.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RatingsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is ratings Fragment"
    }
    val text: LiveData<String> = _text
}
