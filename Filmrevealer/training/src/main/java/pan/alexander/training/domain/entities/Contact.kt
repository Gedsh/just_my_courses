package pan.alexander.training.domain.entities

import android.graphics.Bitmap
import android.net.Uri

data class Contact(
    val name: String,
    val phoneNumbers: List<String>,
    val contactUri: Uri,
    val photo: Bitmap?
)
