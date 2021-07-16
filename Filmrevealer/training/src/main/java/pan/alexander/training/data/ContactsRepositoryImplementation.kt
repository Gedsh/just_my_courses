package pan.alexander.training.data

import androidx.lifecycle.LiveData
import pan.alexander.training.domain.ContactsRepository
import pan.alexander.training.domain.entities.Contact
import javax.inject.Inject

class ContactsRepositoryImplementation @Inject constructor(
    private val contactsLiveData: dagger.Lazy<ContactsLiveData>
) : ContactsRepository {
    override fun getAllContacts(): LiveData<List<Contact>> = contactsLiveData.get()
}
