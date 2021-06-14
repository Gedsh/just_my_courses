package pan.alexander.notes.data;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Completable;
import pan.alexander.notes.domain.AccountRepository;
import pan.alexander.notes.domain.account.User;

public class AccountRepositoryImplementation implements AccountRepository {

    @Nullable
    @Override
    public User getUser() {
        return null;
    }

    @NonNull
    @Override
    public Intent signIn(boolean allowAnonymousUsersAutoUpgrade) {
        return new Intent();
    }

    @NonNull
    @Override
    public Completable signInAnonymously() {
        return Completable.complete();
    }

    @NotNull
    @Override
    public Completable signOut() {
        return Completable.complete();
    }

    @NonNull
    @Override
    public Completable deleteCurrentUser() {
        return Completable.complete();
    }

    @Override
    public void setUser(User user) {
        //Stub
    }

}
