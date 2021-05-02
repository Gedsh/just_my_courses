package pan.alexander.calculator.domain;

import javax.inject.Inject;

import pan.alexander.calculator.domain.entities.CalculatorData;

public class MainInteractor {
    private final DataRepository dataRepository;
    private final Calculator calculator;

    @Inject
    public MainInteractor(DataRepository dataRepository, Calculator calculator) {
        this.dataRepository = dataRepository;
        this.calculator = calculator;
    }

    public String calculateExpression(String expression) {
        return calculator.calculateExpression(expression);
    }

    public CalculatorData getSavedData() {
        return dataRepository.getSavedData();
    }

    public void saveData(CalculatorData calculatorData) {
        dataRepository.saveData(calculatorData);
    }

    public void clearData() {
        dataRepository.clearData();
    }
}
