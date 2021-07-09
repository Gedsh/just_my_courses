package pan.alexander.training.data

import android.content.ContentResolver
import android.database.Cursor
import android.provider.ContactsContract
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ContactsDao @Inject constructor(
    private val dispatcherIO: CoroutineDispatcher,
    private val contentResolver: dagger.Lazy<ContentResolver>
) {
    suspend fun getNamesWithThumbnails(): Cursor? {
        val projection: Array<String> = arrayOf(
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.LOOKUP_KEY,
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
            ContactsContract.Contacts.PHOTO_THUMBNAIL_URI
        )
        return withContext(dispatcherIO) {
            contentResolver.get().query(
                ContactsContract.Contacts.CONTENT_URI,
                projection,
                null,
                null,
                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " ASC"
            )
        }
    }

    suspend fun getPhoneNumbers(contactId: String): Cursor? {
        val projection: Array<String> = arrayOf(
            ContactsContract.CommonDataKinds.Phone.NUMBER
        )
        val selectionClause = "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?"
        val selectionArgs = arrayOf(contactId)

        return withContext(dispatcherIO) {
            contentResolver.get().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                projection,
                selectionClause,
                selectionArgs,
                null
            )
        }
    }
}
