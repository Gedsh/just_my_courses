package pan.alexander.calculator.util;

import android.text.Html;
import android.text.Spanned;

import androidx.appcompat.app.AppCompatDelegate;

import static pan.alexander.calculator.util.AppConstants.MODE_NIGHT_FOLLOW_SYSTEM_PREFERENCE_VALUE;
import static pan.alexander.calculator.util.AppConstants.MODE_NIGHT_NO_PREFERENCE_VALUE;
import static pan.alexander.calculator.util.AppConstants.MODE_NIGHT_YES_PREFERENCE_VALUE;

public class Utils {
    @SuppressWarnings("deprecation")
    public static String spannedStringFromHtml(String text) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            return Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY).toString();
        } else {
            return Html.fromHtml(text).toString();
        }
    }

    @SuppressWarnings("deprecation")
    public static Spanned spannedFromHtml(String text) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            return Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(text);
        }
    }

    public static void setViewMode(String viewModeValue) {

        switch (viewModeValue) {
            case MODE_NIGHT_FOLLOW_SYSTEM_PREFERENCE_VALUE:
            default:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
            case MODE_NIGHT_NO_PREFERENCE_VALUE:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case MODE_NIGHT_YES_PREFERENCE_VALUE:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
        }
    }
}
