package pan.alexander.notes.presentation.activities;

import android.content.Context;
import android.content.Intent;
import android.util.Pair;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import pan.alexander.notes.App;

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
        return null;
    }
}
