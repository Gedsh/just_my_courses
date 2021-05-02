package pan.alexander.calculator;

import android.app.Application;
import android.preference.PreferenceManager;

import pan.alexander.calculator.di.ApplicationComponent;
import pan.alexander.calculator.di.DaggerApplicationComponent;
import pan.alexander.calculator.di.DataModule;

public class App extends Application {
    public final static String LOG_TAG = "alexander.calculator";
    private static App instance;

    private ApplicationComponent daggerComponent;

    @Override
    public void onCreate() {

        super.onCreate();

        instance = this;

        if (daggerComponent == null) {
            initDaggerComponents();
        }
    }

    private void initDaggerComponents() {
        daggerComponent = DaggerApplicationComponent
                .builder()
                .dataModule(new DataModule(PreferenceManager.getDefaultSharedPreferences(instance)))
                .build();
    }

    public static App getInstance() {
        return instance;
    }

    public ApplicationComponent getDaggerComponent() {
        return daggerComponent;
    }
}
