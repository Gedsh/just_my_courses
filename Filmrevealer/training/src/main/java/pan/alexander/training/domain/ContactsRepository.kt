package pan.alexander.training.domain

import androidx.lifecycle.LiveData
import pan.alexander.training.domain.entities.Contact

interface ContactsRepository {
    fun getAllContacts(): LiveData<List<Contact>>
}
