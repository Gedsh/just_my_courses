package pan.alexander.notes.data.database;

import com.google.firebase.firestore.FirebaseFirestore;

import javax.inject.Inject;

public class FirestoreDatabase {

    @Inject
    public FirestoreDatabase() {
    }

    public FirebaseFirestore getFirestoreDatabase() {
        return FirebaseFirestore.getInstance();
    }
}
