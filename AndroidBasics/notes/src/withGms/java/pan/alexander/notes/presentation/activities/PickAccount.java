package pan.alexander.notes.presentation.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.Pair;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;

import org.jetbrains.annotations.NotNull;

import pan.alexander.notes.App;
import pan.alexander.notes.R;

import static pan.alexander.notes.App.LOG_TAG;

public class PickAccount extends ActivityResultContract<Boolean, Pair<String, Integer>> {

    public static final int ANONYMOUS_UPGRADE_MERGE_CONFLICT = 5;


    @NonNull
    @Override
    public Intent createIntent(@NonNull @NotNull Context context, Boolean input) {
        return App.getInstance().getDaggerComponent().getAccountInteractor().get().signIn(input);
    }

    @Nullable
    @Override
    public Pair<String, Integer> parseResult(int resultCode, @Nullable Intent intent) {
        Context context = App.getInstance().getBaseContext();

        IdpResponse response = IdpResponse.fromResultIntent(intent);

        if (resultCode == Activity.RESULT_OK) {
            // User is registered
            return new Pair<>(null, 0);
        }

        if (response == null) {
            // User pressed the back button.
            return new Pair<>("", 0);
        }

        String message = context.getString(R.string.message_unknown_error);
        int errorCode = 0;

        if (response.getError() != null) {
            errorCode = response.getError().getErrorCode();
            if (errorCode == ErrorCodes.NO_NETWORK) {
                message = context.getString(R.string.message_no_network);
            } else if (response.getError().getMessage() != null) {
                message = message + " " + response.getError().getMessage();
            }
            Log.e(LOG_TAG, "PickAccount sign-in error.", response.getError());
        }
        return new Pair<>(message, errorCode);
    }
}
