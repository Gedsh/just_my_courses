package pan.alexander.calculator.util;

import androidx.appcompat.app.AppCompatDelegate;

public class Utils {
    public static void setViewMode(String viewModeValue) {

        switch (viewModeValue) {
            case "1":
            default:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
            case "2":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case "3":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
        }
    }
}
