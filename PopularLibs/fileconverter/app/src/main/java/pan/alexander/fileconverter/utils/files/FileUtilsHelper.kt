package pan.alexander.fileconverter.utils.files

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.DocumentsContract

private const val FLAG_VIRTUAL_DOCUMENT = 1 shl 9

object FileUtilsHelper {

    fun isVirtual(context: Context, uri: Uri): Boolean {
        if (!DocumentsContract.isDocumentUri(context, uri)) {
            return false
        }

        return (getFlags(context, uri) and FLAG_VIRTUAL_DOCUMENT) != 0
    }

    fun getName(context: Context, uri: Uri, defaultValue: String?): String? =
        queryForString(
            context, uri, DocumentsContract.Document.COLUMN_DISPLAY_NAME, defaultValue
        )

    private fun getRawType(context: Context, uri: Uri): String? =
        queryForString(context, uri, DocumentsContract.Document.COLUMN_MIME_TYPE, null)

    fun getType(context: Context, uri: Uri): String? {
        val rawType = getRawType(context, uri)
        return if (DocumentsContract.Document.MIME_TYPE_DIR == rawType) {
            null
        } else {
            rawType
        }
    }

    private fun getFlags(context: Context, uri: Uri) =
        queryForInt(context, uri, DocumentsContract.Document.COLUMN_FLAGS, 0)

    fun isDirectory(context: Context, uri: Uri) =
        DocumentsContract.Document.MIME_TYPE_DIR == getRawType(context, uri)

    fun isFile(context: Context, uri: Uri): Boolean {
        val type = getRawType(context, uri)

        if (type == null || type.isEmpty()) {
            return false
        }

        return DocumentsContract.Document.MIME_TYPE_DIR != type
    }

    fun lastModified(context: Context, uri: Uri): Long =
        queryForLong(context, uri, DocumentsContract.Document.COLUMN_LAST_MODIFIED, 0)

    fun length(context: Context, uri: Uri): Long =
        queryForLong(context, uri, DocumentsContract.Document.COLUMN_SIZE, 0)

    private fun queryForString(
        context: Context,
        uri: Uri,
        column: String,
        defaultValue: String?
    ): String? {
        context.contentResolver.query(
            uri, arrayOf(column), null, null, null, null
        )?.use { cursor ->
            return if (cursor.moveToFirst() && !cursor.isNull(0)) {
                cursor.getString(0)
            } else {
                defaultValue
            }
        }
        return defaultValue
    }

    private fun queryForInt(
        context: Context,
        uri: Uri,
        column: String,
        defaultValue: Int
    ): Int = queryForLong(
        context = context,
        uri = uri,
        column = column,
        defaultValue = defaultValue.toLong()
    ).toInt()

    private fun queryForLong(
        context: Context,
        uri: Uri,
        column: String,
        defaultValue: Long
    ): Long {
        context.contentResolver.query(
            uri, arrayOf(column), null, null, null, null
        )?.use { cursor ->
            return if (cursor.moveToFirst() && !cursor.isNull(0)) {
                cursor.getLong(0)
            } else {
                defaultValue
            }
        }
        return defaultValue
    }

    fun canRead(context: Context, uri: Uri): Boolean {
        if (context.checkCallingOrSelfUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            return false
        }

        return getRawType(context, uri)?.isNotEmpty() ?: false
    }

    fun canWrite(context: Context, uri: Uri): Boolean {
        if (context.checkCallingOrSelfUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            return false
        }

        val type = getRawType(context, uri)
        val flags = queryForInt(context, uri, DocumentsContract.Document.COLUMN_FLAGS, 0)

        if (type == null || type.isEmpty()) {
            return false
        }

        if ((flags and DocumentsContract.Document.FLAG_SUPPORTS_DELETE) != 0) {
            return true
        }

        if (DocumentsContract.Document.MIME_TYPE_DIR == type
            && (flags and DocumentsContract.Document.FLAG_DIR_SUPPORTS_CREATE) != 0
        ) {
            return true
        } else if ((flags and DocumentsContract.Document.FLAG_SUPPORTS_WRITE) != 0) {
            return true
        }

        return false
    }

    fun isExists(context: Context, uri: Uri): Boolean {
        context.contentResolver.query(
            uri,
            arrayOf(DocumentsContract.Document.COLUMN_DOCUMENT_ID),
            null, null, null, null
        )?.use { cursor ->
            cursor.count > 0
        }
        return false
    }
}
