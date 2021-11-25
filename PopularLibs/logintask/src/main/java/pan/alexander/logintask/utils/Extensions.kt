package pan.alexander.logintask.utils

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager

fun Activity.hideSoftKeyboard() {
    val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    val view: View = currentFocus ?: View(this)
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}
