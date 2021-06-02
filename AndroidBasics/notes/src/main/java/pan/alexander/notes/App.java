package pan.alexander.notes;

import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;

import pan.alexander.notes.di.ApplicationComponent;
import pan.alexander.notes.di.DaggerApplicationComponent;
import pan.alexander.notes.di.RepositoryModule;
import pan.alexander.notes.di.RoomModule;

public class App extends Application {

    public final static String LOG_TAG = "alexander.notes";

    private static App instance;

    private ApplicationComponent daggerComponent;

    @Override
    public void onCreate() {

        super.onCreate();

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        instance = this;

        if (daggerComponent == null) {
            initDaggerComponents();
        }
    }

    private void initDaggerComponents() {
        daggerComponent = DaggerApplicationComponent.builder()
                .roomModule(new RoomModule(instance))
                .repositoryModule(new RepositoryModule())
                .build();
    }

    public static App getInstance() {
        return instance;
    }

    public ApplicationComponent getDaggerComponent() {
        return daggerComponent;
    }
}
