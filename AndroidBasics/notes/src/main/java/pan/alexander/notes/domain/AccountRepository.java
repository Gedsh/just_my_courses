package pan.alexander.notes.domain;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import io.reactivex.Completable;
import pan.alexander.notes.data.database.NotesDao;
import pan.alexander.notes.domain.account.User;

public interface AccountRepository {
    @Nullable
    User getUser();

    @NonNull
    Intent signIn(boolean allowAnonymousUsersAutoUpgrade);

    @NonNull
    Completable signInAnonymously();

    @NonNull
    Completable signOut();

    @NonNull
    Completable deleteCurrentUser();

    void setUser(User user);
}
