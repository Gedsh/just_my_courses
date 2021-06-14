package pan.alexander.notes.di;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.room.Room;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import pan.alexander.notes.data.database.AppDatabase;
import pan.alexander.notes.data.database.TrashDao;

@Module
public class RoomModule {
    private final static String APP_DATABASE_NAME = "app-database";

    private final AppDatabase db;

    public RoomModule(@NonNull Application appContext) {
        db = Room.databaseBuilder(appContext, AppDatabase.class, APP_DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build();
    }

    @Provides
    @Singleton
    AppDatabase providesRoomDatabase() {
        return db;
    }

    @Provides
    @Singleton
    TrashDao providesTrashDao() {
        return db.getTrashDao();
    }
}
