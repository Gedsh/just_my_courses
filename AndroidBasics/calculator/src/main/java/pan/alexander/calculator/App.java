package pan.alexander.calculator;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;

import pan.alexander.calculator.di.ApplicationComponent;
import pan.alexander.calculator.di.DaggerApplicationComponent;
import pan.alexander.calculator.di.RepositoryModule;
import pan.alexander.calculator.di.RoomModule;
import pan.alexander.calculator.di.SettingsModule;

public class App extends Application {

    static {
        System.setProperty("rx2.purge-enabled", "false");
        System.setProperty("rx2.purge-period-seconds", "60");
        System.setProperty("rx2.computation-threads", "1");
    }

    public final static String LOG_TAG = "alexander.calculator";
    private static App instance;

    private ApplicationComponent daggerComponent;
    private SharedPreferences sharedPreferences;
    private Handler handler;

    @Override
    public void onCreate() {

        super.onCreate();

        instance = this;

        if (daggerComponent == null) {
            initSharedPreferences();
            initDaggerComponents();
            initGlobalHandler();
        }
    }

    private void initDaggerComponents() {
        daggerComponent = DaggerApplicationComponent
                .builder()
                .roomModule(new RoomModule(instance))
                .repositoryModule(new RepositoryModule())
                .settingsModule(new SettingsModule(sharedPreferences))
                .build();
    }

    private void initSharedPreferences() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
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

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public Handler getHandler() {
        return handler;
    }
}
