package pan.alexander.notes.presentation.activities;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

@Retention(SOURCE)
@IntDef({
        MainActivityActionRequest.NO_ACTION,
        MainActivityActionRequest.OPEN_NAV_DRAWER,
        MainActivityActionRequest.LAUNCH_ACCOUNT_CHOOSER
})

public @interface MainActivityActionRequest {
    int NO_ACTION = 0;
    int OPEN_NAV_DRAWER = 1;
    int LAUNCH_ACCOUNT_CHOOSER = 2;
}
