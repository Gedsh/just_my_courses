package pan.alexander.training.data

import android.content.ContentResolver
import android.content.res.AssetFileDescriptor
import android.database.ContentObserver
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.ContactsContract
import android.util.Log
import androidx.lifecycle.LiveData
import kotlinx.coroutines.*
import pan.alexander.training.App
import pan.alexander.training.App.Companion.LOG_TAG
import pan.alexander.training.domain.entities.Contact
import java.io.FileNotFoundException
import java.io.IOException
import javax.inject.Inject

class ContactsLiveData @Inject constructor(
    private val contentResolver: ContentResolver,
    mainScope: CoroutineScope,
    private val dispatcherIO: CoroutineDispatcher,
) : LiveData<List<Contact>>() {

    private val liveDataScope = mainScope + CoroutineName("ContactsLiveData")

    private var dao = App.instance.daggerComponent.getContactsDao()

    private val contentObserver = object : ContentObserver(null) {
        override fun onChange(selfChange: Boolean) {
            super.onChange(selfChange)

            getContacts()
        }
    }

    override fun onActive() {
        super.onActive()

        registerContentObserver()
        getContacts()
    }

    override fun onInactive() {
        super.onInactive()
        liveDataScope.cancel()
        unregisterContentObserver()
    }

    private fun registerContentObserver() {
        contentResolver.registerContentObserver(
            ContactsContract.Contacts.CONTENT_URI,
            true,
            contentObserver
        )
    }

    private fun unregisterContentObserver() {
        try {
            contentResolver.unregisterContentObserver(contentObserver)
        } catch (ignored: Exception) {
        }
    }

    private fun getContacts() {
        liveDataScope.launch {
            var cursor: Cursor? = null
            try {
                cursor = dao.get().getNamesWithThumbnails()
                value = when (cursor?.count) {
                    null, 0 -> emptyList()
                    else -> getContactsFromCursor(cursor)
                }
            } catch (e: Exception) {
                Log.e(LOG_TAG, "Get contacts exception", e)
            } finally {
                cursor?.close()
            }
        }
    }

    private suspend fun getContactsFromCursor(cursor: Cursor?): List<Contact> {

        return withContext(dispatcherIO) {
            val contacts = mutableListOf<Contact>()

            cursor?.let {
                val idIndex = it.getColumnIndexOrThrow(ContactsContract.Contacts._ID)
                val lookupKeyIndex = it.getColumnIndexOrThrow(ContactsContract.Contacts.LOOKUP_KEY)
                val nameIndex =
                    it.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)
                val photoIndex =
                    it.getColumnIndexOrThrow(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI)

                while (it.moveToNext()) {
                    val id = it.getLong(idIndex)
                    val lookupKey = it.getString(lookupKeyIndex)
                    val name = it.getString(nameIndex)
                    val phoneNumbers = getNumbers(id.toString())
                    val photoUri = it.getString(photoIndex)

                    val contactUri = ContactsContract.Contacts.getLookupUri(id, lookupKey)
                    val photo = photoUri?.let { uri -> loadContactPhotoThumbnail(uri) }

                    contacts.add(
                        Contact(
                            name = name,
                            phoneNumbers = phoneNumbers,
                            contactUri = contactUri,
                            photo = photo
                        )
                    )
                }
            }

            contacts
        }
    }

    private suspend fun getNumbers(contactId: String): List<String> {
        var cursor: Cursor? = null
        try {
            cursor = dao.get().getPhoneNumbers(contactId)
            return when (cursor?.count) {
                null, 0 -> emptyList()
                else -> getNumbersFromCursor(cursor)
            }
        } catch (e: Exception) {
            Log.e(LOG_TAG, "Get numbers exception", e)
        } finally {
            cursor?.close()
        }
        return emptyList()
    }

    private suspend fun getNumbersFromCursor(cursor: Cursor?): List<String> {

        return withContext(dispatcherIO) {

            val numbers = mutableListOf<String>()

            cursor?.let {
                val phoneNumberIndex =
                    it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

                while (it.moveToNext()) {
                    val phoneNumber = it.getString(phoneNumberIndex)
                    phoneNumber.takeIf { number -> number.isNotBlank() }?.let { number ->
                        numbers.add(number)
                    }
                }
            }

            numbers
        }

    }

    private fun loadContactPhotoThumbnail(photoData: String): Bitmap? {
        var afd: AssetFileDescriptor? = null
        return try {
            val thumbUri: Uri = Uri.parse(photoData)

            afd = contentResolver.openAssetFileDescriptor(thumbUri, "r")
            afd?.fileDescriptor?.let { fileDescriptor ->
                BitmapFactory.decodeFileDescriptor(fileDescriptor, null, null)
            }
        } catch (e: FileNotFoundException) {
            null
        } finally {
            try {
                afd?.close()
            } catch (ignored: IOException) {
            }
        }
    }
}
