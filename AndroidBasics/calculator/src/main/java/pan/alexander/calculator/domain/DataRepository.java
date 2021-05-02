package pan.alexander.calculator.domain;

import pan.alexander.calculator.domain.entities.CalculatorData;

public interface DataRepository {
    CalculatorData getSavedData();
    void saveData(CalculatorData data);
    void clearData();
}
