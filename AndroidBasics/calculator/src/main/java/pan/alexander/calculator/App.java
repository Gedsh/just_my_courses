package pan.alexander.calculator;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import pan.alexander.calculator.di.ApplicationComponent;
import pan.alexander.calculator.di.DaggerApplicationComponent;
import pan.alexander.calculator.di.RepositoryModule;
import pan.alexander.calculator.di.RoomModule;
import pan.alexander.calculator.di.SettingsModule;

public class App extends Application {
    public final static String LOG_TAG = "alexander.calculator";
    private static App instance;

    private ApplicationComponent daggerComponent;
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate() {

        super.onCreate();

        instance = this;

        if (daggerComponent == null) {
            initSharedPreferences();
            initDaggerComponents();
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

    public static App getInstance() {
        return instance;
    }

    public ApplicationComponent getDaggerComponent() {
        return daggerComponent;
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }
}
