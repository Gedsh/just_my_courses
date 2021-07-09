package pan.alexander.training.domain

import javax.inject.Inject

class MainInteractor @Inject constructor(private val contactsRepository: ContactsRepository) {
    fun getAllContacts() = contactsRepository.getAllContacts()
}
