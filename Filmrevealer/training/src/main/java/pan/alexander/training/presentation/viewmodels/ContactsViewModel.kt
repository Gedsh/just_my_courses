package pan.alexander.training.presentation.viewmodels

import androidx.lifecycle.ViewModel
import pan.alexander.training.App

class ContactsViewModel : ViewModel() {

    private val mainInteractor = App.instance.daggerComponent.getMainInteractor()

    fun getContactsLiveData() = mainInteractor.get().getAllContacts()
}
