package pan.alexander.training.data

import androidx.lifecycle.LiveData
import pan.alexander.training.App
import pan.alexander.training.domain.ContactsRepository
import pan.alexander.training.domain.entities.Contact

class ContactsRepositoryImplementation : ContactsRepository {

    private val contactsLiveData = App.instance.daggerComponent.getContactsLiveData()

    override fun getAllContacts(): LiveData<List<Contact>> = contactsLiveData.get()

}
