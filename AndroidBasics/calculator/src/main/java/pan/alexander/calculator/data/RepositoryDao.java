package pan.alexander.calculator.data;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import pan.alexander.calculator.domain.entities.CalculatorData;

public class RepositoryDao {

    private static final String SCREEN_DATA_SAVED = "SCREEN_DATA";
    private final SharedPreferences sharedPreferences;

    public RepositoryDao(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    CalculatorData getData() {
        String screenData = sharedPreferences.getString(SCREEN_DATA_SAVED, "");

        return new CalculatorData(screenData);
    }

    void saveData(@NonNull CalculatorData calculatorData) {
        sharedPreferences.edit()
                .putString(SCREEN_DATA_SAVED, calculatorData.getScreenData())
                .apply();
    }

    void clearData() {
        sharedPreferences.edit()
                .putString(SCREEN_DATA_SAVED, "")
                .apply();
    }
}
