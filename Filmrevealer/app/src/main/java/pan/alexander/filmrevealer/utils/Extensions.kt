package pan.alexander.filmrevealer.utils

import android.annotation.SuppressLint
import android.view.View
import com.google.android.material.snackbar.Snackbar

private const val SNACKBAR_DISPLAY_DURATION = 5000

fun View.showSnackBar(
    text: Int,
    actionText: Int,
    action: (View) -> Unit,
    length: Int = Snackbar.LENGTH_LONG
) {
    Snackbar.make(this, text, length).setAction(actionText, action).show()
}

@SuppressLint("WrongConstant")
fun View.showSnackBar(
    text: String,
    actionText: Int,
    action: (View) -> Unit,
    length: Int = SNACKBAR_DISPLAY_DURATION
) {
    Snackbar.make(this, text, length).setAction(actionText, action).show()
}

fun View.showSnackBar(
    text: String,
    length: Int = Snackbar.LENGTH_SHORT
) {
    Snackbar.make(this, text, length).show()
}
