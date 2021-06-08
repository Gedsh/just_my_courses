package pan.alexander.notes.data;

import androidx.lifecycle.LiveData;

import java.util.List;

import dagger.Lazy;
import io.reactivex.Completable;
import io.reactivex.Single;
import pan.alexander.notes.App;
import pan.alexander.notes.data.database.TrashDao;
import pan.alexander.notes.domain.TrashRepository;
import pan.alexander.notes.domain.entities.Note;
import pan.alexander.notes.domain.entities.Trash;

public class TrashRepositoryImplementation implements TrashRepository {
    private final Lazy<TrashDao> dao = App.getInstance().getDaggerComponent().getTrashDao();

    @Override
    public LiveData<List<Note>> getAllTrashesFromDB() {
        return dao.get().getAllTrashes();
    }

    @Override
    public Completable addTrashToDB(Trash trash) {
        return dao.get().insertTrash(trash);
    }

    @Override
    public Completable addTrashesToDB(List<Trash> trashes) {
        return dao.get().insertTrashes(trashes);
    }

    @Override
    public Completable removeTrashFromDB(Trash trash) {
        return dao.get().deleteTrash(trash);
    }

    @Override
    public Completable removeTrashesFromDB(List<Trash> trashes) {
        return dao.get().deleteTrashes(trashes);
    }

    @Override
    public Single<Integer> getTrashSize() {
        return dao.get().getTrashSize();
    }

    @Override
    public Completable trimTrashToSize(int trashCapacity) {
        return dao.get().trimTrash(trashCapacity);
    }

    @Override
    public Completable removeAllTrashesFromDB() {
        return dao.get().deleteAllTrashes();
    }
}
