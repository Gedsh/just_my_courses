package pan.alexander.notes.data;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;

import dagger.Lazy;
import io.reactivex.Completable;
import pan.alexander.notes.App;
import pan.alexander.notes.data.database.NotesDao;
import pan.alexander.notes.domain.AccountRepository;
import pan.alexander.notes.domain.account.User;

public class AccountRepositoryImplementation implements AccountRepository {

    Context context = App.getInstance().getBaseContext();
    Lazy<NotesDao> notesDao = App.getInstance().getDaggerComponent().getNotesDao();

    @Nullable
    @Override
    public User getUser() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser == null) {
            return null;
        }

        String uid = firebaseUser.getUid();
        String name = firebaseUser.getDisplayName();
        String email = firebaseUser.getEmail();
        Uri firebasePhotoUri = firebaseUser.getPhotoUrl();
        Uri googlePhotoUri = getUserPhotoByGoogleAccount();
        boolean isAnonymous = firebaseUser.isAnonymous();

        User user = new User(uid,
                name != null ? name : "",
                email != null ? email : "",
                isAnonymous);

        user.setPhotoUri(googlePhotoUri != null ? googlePhotoUri : firebasePhotoUri);

        return user;
    }

    private Uri getUserPhotoByGoogleAccount() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(context);
        Uri personalPhoto = null;
        if (account != null) {
            personalPhoto = account.getPhotoUrl();
        }
        return personalPhoto;
    }

    @NonNull
    @Override
    public Intent signIn(boolean allowAnonymousUsersAutoUpgrade) {
        AuthUI.SignInIntentBuilder builder = AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(Collections.singletonList(
                        new AuthUI.IdpConfig.GoogleBuilder().build()))
                .setIsSmartLockEnabled(false);

        if (allowAnonymousUsersAutoUpgrade) {
            builder.enableAnonymousUsersAutoUpgrade();
        }
        return builder.build();
    }

    @NonNull
    @Override
    public Completable signInAnonymously() {
        return Completable.create(emitter -> FirebaseAuth.getInstance().signInAnonymously()
                .addOnSuccessListener(result -> {
                    if (!emitter.isDisposed()) {
                        emitter.onComplete();
                    }
                }).addOnFailureListener(e -> {
                    if (!emitter.isDisposed()) {
                        emitter.tryOnError(e);
                    }
                }));
    }

    @NotNull
    @Override
    public Completable signOut() {
        return Completable.create(emitter -> AuthUI.getInstance()
                .signOut(context)
                .addOnSuccessListener(result -> {
                    if (!emitter.isDisposed()) {
                        emitter.onComplete();
                    }
                }).addOnFailureListener(e -> {
                    if (!emitter.isDisposed()) {
                        emitter.tryOnError(e);
                    }
                }));
    }

    @NonNull
    @Override
    public Completable deleteCurrentUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            return Completable.complete();
        }

        return Completable.create(emitter -> user.delete()
                .addOnSuccessListener(result -> {
                    if (!emitter.isDisposed()) {
                        emitter.onComplete();
                    }
                }).addOnFailureListener(e -> {
                    if (!emitter.isDisposed()) {
                        emitter.tryOnError(e);
                    }
                }));
    }

    @Override
    public void setUser(User user) {
        notesDao.get().setUser(user);
    }

}
