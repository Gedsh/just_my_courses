package pan.alexander.notes.domain.account;

import android.net.Uri;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import java.util.Objects;

@Keep
public class User {
    private final String uid;
    private final String name;
    private final String email;
    private Uri photoUri;
    private final boolean isAnonymous;

    public User(String uid, String name, String email, boolean isAnonymous) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.isAnonymous = isAnonymous;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Uri getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(Uri photoUri) {
        this.photoUri = photoUri;
    }

    public boolean isAnonymous() {
        return isAnonymous;
    }
}
