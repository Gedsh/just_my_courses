package pan.alexander.notes;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.multidex.MultiDexApplication;

import pan.alexander.notes.di.ApplicationComponent;
import pan.alexander.notes.di.DaggerApplicationComponent;
import pan.alexander.notes.di.RepositoryModule;
import pan.alexander.notes.di.RoomModule;

public class App extends MultiDexApplication {

    static {
        System.setProperty("rx2.purge-enabled", "false");
        System.setProperty("rx2.purge-period-seconds", "60");
        System.setProperty("rx2.computation-threads", "1");
    }

    public final static String LOG_TAG = "alexander.notes";

    private static App instance;

    private ApplicationComponent daggerComponent;
    private Handler handler;

    @Override
    public void onCreate() {

        super.onCreate();

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        instance = this;

        if (daggerComponent == null) {
            initDaggerComponents();
            initGlobalHandler();
        }
    }

    private void initDaggerComponents() {
        daggerComponent = DaggerApplicationComponent.builder()
                .roomModule(new RoomModule(instance))
                .repositoryModule(new RepositoryModule())
                .build();
    }

    private void initGlobalHandler() {
        Looper looper = Looper.getMainLooper();
        if (looper != null) {
            handler = new Handler(looper);
        }
    }

    public static App getInstance() {
        return instance;
    }

    public ApplicationComponent getDaggerComponent() {
        return daggerComponent;
    }

    public Handler getHandler() {
        return handler;
    }
}
