package pan.alexander.calculator.viewmodel;

import android.os.Handler;
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
import pan.alexander.calculator.domain.entities.HistoryData;
import pan.alexander.calculator.util.ButtonToSymbolMapping;

import static pan.alexander.calculator.App.LOG_TAG;
import static pan.alexander.calculator.util.AppConstants.DELAY_BEFORE_STOP_RX_SCHEDULERS;
import static pan.alexander.calculator.util.Utils.spannedStringFromHtml;

public class MainViewModel extends ViewModel {
    private static final String SAVED_STATE_HANDLE = "SAVED_STATE_HANDLE";
    private static final String HTML_CODE_START_SYMBOL = "&";
    private static final char HTML_CODE_END_SYMBOL = ";".charAt(0);

    private final CompositeDisposable disposables = new CompositeDisposable();

    private final MainInteractor mainInteractor;
    private final SavedStateHandle calculatorDataSavedStateHandle;
    private final Handler globalHandler;

    private BehaviorSubject<String> inputExpressionSubject;
    private Observable<String> loadDataObservable;
    private MutableLiveData<String> displayedExpression;
    private MutableLiveData<String> displayedResult;
    private MutableLiveData<Boolean> successfulCalculation;



    public MainViewModel(SavedStateHandle savedStateHandle) {
        this.calculatorDataSavedStateHandle = savedStateHandle;
        this.mainInteractor = App.getInstance().getDaggerComponent().getMainInteractor();
        this.globalHandler = App.getInstance().getHandler();
        stopPreviousGlobalHandlerTasks();
        startRxSchedulers();
        initLoadDataObservable();
    }

    private void stopPreviousGlobalHandlerTasks() {
        if (globalHandler != null) {
            globalHandler.removeCallbacksAndMessages(null);
        }
    }

    private void startRxSchedulers() {
        Schedulers.start();
    }

    private void initLoadDataObservable() {
        loadDataObservable = Observable.fromCallable(() -> {
            HistoryData historyData = mainInteractor.getLastHistory();
            if (!historyData.getExpression().isEmpty()) {
                mainInteractor.deleteHistoryEntry(historyData).subscribe();
            }
            return historyData.getExpression();
        });
    }

    public LiveData<String> getDisplayedExpression() {
        if (displayedExpression == null) {
            displayedExpression = new MutableLiveData<>();
        }

        if (inputExpressionSubject == null) {
            initExpressionObservable();
            loadSavedData();
        }

        return displayedExpression;
    }

    private void initExpressionObservable() {
        inputExpressionSubject = BehaviorSubject
                .createDefault("");
        disposables.add(inputExpressionSubject.toFlowable(BackpressureStrategy.LATEST)
                .subscribe(inputState -> displayedExpression.setValue(inputState)));

        disposables.add(inputExpressionSubject.toFlowable(BackpressureStrategy.LATEST)
                .observeOn(Schedulers.computation())
                .map(mainInteractor::calculateExpression)
                .subscribe(this::expressionCalculated));
    }

    private void expressionCalculated(String computationResult) {
        if (displayedExpression == null) {
            return;
        }

        String expression = displayedExpression.getValue();

        if (expression == null || expression.isEmpty()) {
            return;
        }

        if (computationResult.isEmpty()) {
            successfulCalculation.postValue(false);
        } else {
            displayedResult.postValue(computationResult);
            successfulCalculation.postValue(true);
        }
    }

    public LiveData<String> getDisplayedResult() {
        if (displayedResult == null) {
            displayedResult = new MutableLiveData<>();
        }
        return displayedResult;
    }

    public LiveData<Boolean> getCalculationResultSuccess() {
        if (successfulCalculation == null) {
            successfulCalculation = new MutableLiveData<>();
        }
        return successfulCalculation;
    }

    private void loadSavedData() {
        HistoryData historyData = calculatorDataSavedStateHandle.get(SAVED_STATE_HANDLE);

        String currentExpression = inputExpressionSubject.getValue();

        if (currentExpression != null && !currentExpression.isEmpty()) {
            return;
        }

        if (historyData == null) {
            disposables.add(loadDataObservable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(savedData -> {
                        if (inputExpressionSubject.getValue().isEmpty()) {
                            inputExpressionSubject.onNext(savedData);
                        }
                    }));
        } else {
            inputExpressionSubject.onNext(historyData.getExpression());
        }

    }

    public void saveCalculatorDataToSavedStateHandle() {
        String screenData = "";
        String resultData = "";
        if (inputExpressionSubject != null && displayedResult != null) {
            screenData = inputExpressionSubject.getValue() != null ? inputExpressionSubject.getValue() : "";
            resultData = displayedResult.getValue() != null ? displayedResult.getValue() : "";
        }

        HistoryData historyData = new HistoryData(screenData, resultData, System.currentTimeMillis());

        calculatorDataSavedStateHandle.set(SAVED_STATE_HANDLE, historyData);
    }

    public void handleButtonPressed(String symbol) {
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
                updateDisplayedExpression(symbol);
                break;
            case ButtonToSymbolMapping.BUTTON_EQUALS:
                handleEqualsPressed();
                break;
            case ButtonToSymbolMapping.BUTTON_BACKSPACE:
                handleBackspacePressed();
                break;
            case ButtonToSymbolMapping.BUTTON_CLEAR:
                clearDisplayedExpression();
                break;
            default:
                Log.e(LOG_TAG, "MainViewModel handleButtonPressed unknown button " + symbol);
        }
    }

    public void setDisplayedExpression(String expression) {
        inputExpressionSubject.onNext(expression);
    }

    private void updateDisplayedExpression(String symbol) {
        String currentExpression = inputExpressionSubject.getValue();

        if (currentExpression == null) {
            currentExpression = "";
        }

        inputExpressionSubject.onNext(currentExpression + symbol);
    }

    private void clearDisplayedExpression() {
        inputExpressionSubject.onNext("");
        displayedResult.setValue("");
        successfulCalculation.setValue(true);
    }

    private void handleBackspacePressed() {
        String currentExpression = inputExpressionSubject.getValue();

        if (currentExpression == null) {
            return;
        }

        if (currentExpression.isEmpty()) {
            return;
        }

        if (currentExpression.length() > 1) {
            String result = removePreviousSymbol(currentExpression);
            if (result.isEmpty()) {
                clearDisplayedExpression();
            } else {
                inputExpressionSubject.onNext(result);
            }
        } else {
            clearDisplayedExpression();
        }
    }

    private String removePreviousSymbol(String expression) {
        StringBuilder expressionStringBuilder = new StringBuilder(expression);
        int expressionEndSymbolPosition = expressionStringBuilder.length() - 1;

        if (expressionStringBuilder.charAt(expressionEndSymbolPosition) == HTML_CODE_END_SYMBOL) {
            int htmlStartSymbolPosition = expressionStringBuilder.lastIndexOf(HTML_CODE_START_SYMBOL);
            if (htmlStartSymbolPosition >= 0) {
                expressionStringBuilder.delete(htmlStartSymbolPosition, expressionEndSymbolPosition + 1);
            }
        } else {
            expressionStringBuilder.deleteCharAt(expressionEndSymbolPosition);
        }

        return expressionStringBuilder.toString();
    }

    private void handleEqualsPressed() {

        if (displayedResult == null || successfulCalculation == null) {
            return;
        }

        String resultText = displayedResult.getValue();

        Boolean successCalculation = successfulCalculation.getValue();

        if (resultText == null || successCalculation == null) {
            return;
        }

        if (successCalculation) {
            saveHistory();
            inputExpressionSubject.onNext(resultText);
        }
    }

    public void handleInputTextChanged(String displayedExpression) {
        String currentExpression = inputExpressionSubject.getValue();

        if (currentExpression == null) {
            return;
        }

        currentExpression = spannedStringFromHtml(currentExpression);

        if (!currentExpression.equals(displayedExpression)) {
            if (displayedExpression.isEmpty()) {
                clearDisplayedExpression();
            } else {
                inputExpressionSubject.onNext(displayedExpression);
            }
        }
    }

    private void saveHistory() {
        String currentExpression = displayedExpression.getValue();
        String result = displayedResult.getValue();

        if (currentExpression != null && result != null) {
            HistoryData historyData = new HistoryData(currentExpression, result,
                    System.currentTimeMillis());
            mainInteractor.saveHistory(historyData);
        }
    }

    public void stopRxSchedulersDelayed() {
        if (globalHandler != null) {
            globalHandler.postDelayed(Schedulers::shutdown, DELAY_BEFORE_STOP_RX_SCHEDULERS);
        }
    }

    @Override
    protected void onCleared() {

        saveHistory();

        disposables.clear();

        stopRxSchedulersDelayed();

        mainInteractor.clearDisposablesDelayed();

        super.onCleared();
    }
}
