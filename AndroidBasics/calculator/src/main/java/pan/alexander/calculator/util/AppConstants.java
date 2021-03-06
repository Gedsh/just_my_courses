package pan.alexander.calculator.util;

public interface AppConstants {
    int CALCULATION_TIMEOUT_SEC = 3;
    int MAX_HISTORY_ENTRIES = 10;
    int DELAY_BEFORE_CLEAR_DISPOSABLES = 10000;
    int DELAY_BEFORE_STOP_RX_SCHEDULERS = 5000;
    int INTERVAL_BACKSPACE_LONG_PRESSING = 150;

    String VIEW_MODE_PREFERENCE = "viewModePreference";
    String MODE_NIGHT_FOLLOW_SYSTEM_PREFERENCE_VALUE = "1";
    String MODE_NIGHT_NO_PREFERENCE_VALUE = "2";
    String MODE_NIGHT_YES_PREFERENCE_VALUE = "3";
}
