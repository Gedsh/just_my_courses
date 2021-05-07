package pan.alexander.calculator.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import pan.alexander.calculator.App;
import pan.alexander.calculator.domain.MainInteractor;
import pan.alexander.calculator.domain.entities.CalculatorData;
import pan.alexander.calculator.util.ButtonToSymbolMapping;

import static pan.alexander.calculator.App.LOG_TAG;

public class MainViewModel extends ViewModel {
    private static final String SAVED_DATA = "SAVED_DATA";

    private final MainInteractor mainInteractor = App.getInstance().getDaggerComponent().getMainInteractor();

    private final SavedStateHandle calculatorDataSavedStateHandle;

    private final Observable<String> loadDataObservable = Observable.fromCallable(() ->
            mainInteractor.getSavedData().getScreenData());

    private final CompositeDisposable disposables = new CompositeDisposable();

    private BehaviorSubject<String> expressionSubjectRx;

    private MutableLiveData<String> displayedExpression;
    private MutableLiveData<String> displayedResult;


    public MainViewModel(SavedStateHandle savedStateHandle) {
        this.calculatorDataSavedStateHandle = savedStateHandle;
    }

    public LiveData<String> getDisplayedExpression() {
        if (displayedExpression == null) {
            displayedExpression = new MutableLiveData<>();
        }

        if (expressionSubjectRx == null) {
            initExpressionObservable();
            loadSavedData();
        }

        return displayedExpression;
    }

    private void initExpressionObservable() {
        expressionSubjectRx = BehaviorSubject.createDefault("");
        disposables.add(expressionSubjectRx.toFlowable(BackpressureStrategy.LATEST)
                .subscribe(expression -> displayedExpression.setValue(expression)));

        disposables.add(expressionSubjectRx.toFlowable(BackpressureStrategy.LATEST)
                .observeOn(Schedulers.computation())
                .map(mainInteractor::calculateExpression)
                .filter(computationResult -> !computationResult.isEmpty())
                .subscribe(computationResult -> displayedResult.postValue(computationResult)));
    }

    public LiveData<String> getDisplayedResult() {
        if (displayedResult == null) {
            displayedResult = new MutableLiveData<>();
        }
        return displayedResult;
    }

    private void loadSavedData() {
        CalculatorData calculatorData = calculatorDataSavedStateHandle.get(SAVED_DATA);

        if (expressionSubjectRx.getValue() != null && !expressionSubjectRx.getValue().isEmpty()) {
            return;
        }

        if (calculatorData == null) {
            disposables.add(loadDataObservable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(savedData -> {
                        if (expressionSubjectRx.getValue().isEmpty()) {
                            expressionSubjectRx.onNext(savedData);
                        }
                    }));
        } else {
            expressionSubjectRx.onNext(calculatorData.getScreenData());
        }

    }

    public void setCalculatorData() {
        String screenData = "";
        if (expressionSubjectRx != null) {
            screenData = expressionSubjectRx.getValue() != null ? expressionSubjectRx.getValue() : "";
        }

        calculatorDataSavedStateHandle.set(SAVED_DATA, new CalculatorData(screenData));
    }

    public void handleButtonPressed(String symbol, int position) {
        switch (symbol) {
            case ButtonToSymbolMapping.BUTTON_ONE:
            case ButtonToSymbolMapping.BUTTON_TWO:
            case ButtonToSymbolMapping.BUTTON_THREE:
            case ButtonToSymbolMapping.BUTTON_FOUR:
            case ButtonToSymbolMapping.BUTTON_FIVE:
            case ButtonToSymbolMapping.BUTTON_SIX:
            case ButtonToSymbolMapping.BUTTON_SEVEN:
            case ButtonToSymbolMapping.BUTTON_EIGHT:
            case ButtonToSymbolMapping.BUTTON_NINE:
            case ButtonToSymbolMapping.BUTTON_ZERO:
            case ButtonToSymbolMapping.BUTTON_DIVIDE:
            case ButtonToSymbolMapping.BUTTON_MULTIPLY:
            case ButtonToSymbolMapping.BUTTON_MINUS:
            case ButtonToSymbolMapping.BUTTON_PLUS:
            case ButtonToSymbolMapping.BUTTON_PERCENT:
            case ButtonToSymbolMapping.BUTTON_SQRT:
            case ButtonToSymbolMapping.BUTTON_POWERED:
            case ButtonToSymbolMapping.BUTTON_POINT:
            case ButtonToSymbolMapping.BUTTON_BRACKETS_OPEN:
            case ButtonToSymbolMapping.BUTTON_BRACKETS_CLOSE:
                updateDisplayedExpression(symbol, position);
                break;
            case ButtonToSymbolMapping.BUTTON_EQUALS:
                handleEqualsPressed();
                break;
            case ButtonToSymbolMapping.BUTTON_BACKSPACE:
                handleBackspacePressed(position);
                break;
            case ButtonToSymbolMapping.BUTTON_CLEAR:
                clearDisplayedExpression();
                break;
            default:
                Log.e(LOG_TAG, "MainViewModel handleButtonPressed unknown button " + symbol);
        }
    }

    public void updateDisplayedExpression(String expression) {
        expressionSubjectRx.onNext(expression);
    }

    private void updateDisplayedExpression(String symbol, int position) {
        String currentExpression = expressionSubjectRx.getValue();

        if (currentExpression == null) {
            currentExpression = "";
        }

        if (position < 0 || position > currentExpression.length()) {
            expressionSubjectRx.onNext(currentExpression + symbol);
        } else {
            String expressionPartOne = currentExpression.substring(0, position);
            String expressionPartTwo = currentExpression.substring(position);
            expressionSubjectRx.onNext(expressionPartOne + symbol + expressionPartTwo);
        }
    }

    private void clearDisplayedExpression() {
        expressionSubjectRx.onNext("");
        displayedResult.setValue("");
    }

    private void handleBackspacePressed(int position) {
        String currentExpression = expressionSubjectRx.getValue();

        if (currentExpression != null && !currentExpression.isEmpty()) {

            if (position > 0 && currentExpression.length() > 1) {
                String expressionPartOne = currentExpression.substring(0, position - 1);
                String expressionPartTwo = currentExpression.substring(position);
                currentExpression = expressionPartOne + expressionPartTwo;
                expressionSubjectRx.onNext(currentExpression);
            } else if (position != 0 && currentExpression.length() > 1) {
                currentExpression = currentExpression.substring(0, currentExpression.length() - 1);
                expressionSubjectRx.onNext(currentExpression);
            } else if (position != 0) {
                clearDisplayedExpression();
            }
        }
    }

    private void handleEqualsPressed() {
        String resultText = displayedResult.getValue();
        if (resultText != null) {
            expressionSubjectRx.onNext(resultText);
        }
    }

    @Override
    protected void onCleared() {

        String expression = expressionSubjectRx.getValue();

        if (expression != null) {
            mainInteractor.saveData(new CalculatorData(expression));
        }

        disposables.dispose();

        super.onCleared();
    }
}
