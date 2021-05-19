package pan.alexander.calculator.di;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.room.Room;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import pan.alexander.calculator.data.database.AppDatabase;
import pan.alexander.calculator.data.database.HistoryDao;

@Module
public class RoomModule {
    private final AppDatabase db;

    public RoomModule(@NonNull Application appContext) {
        db = Room
                .databaseBuilder(appContext, AppDatabase.class, "app-database")
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
    HistoryDao providesHistoryDao() {
        return db.historyDao();
    }
}
