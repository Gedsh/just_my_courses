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
import pan.alexander.calculator.domain.entities.UserInputState;
import pan.alexander.calculator.util.ButtonToSymbolMapping;

import static pan.alexander.calculator.App.LOG_TAG;
import static pan.alexander.calculator.util.AppConstants.DEFAULT_USER_INPUT_CURSOR_POSITION;
import static pan.alexander.calculator.util.AppConstants.DELAY_BEFORE_STOP_RX_SCHEDULERS;

public class MainViewModel extends ViewModel {
    private static final String SAVED_STATE_HANDLE = "SAVED_STATE_HANDLE";

    private final CompositeDisposable disposables = new CompositeDisposable();

    private final MainInteractor mainInteractor;
    private final SavedStateHandle calculatorDataSavedStateHandle;
    private final Handler globalHandler;

    private BehaviorSubject<UserInputState> inputStateSubject;
    private Observable<String> loadDataObservable;
    private MutableLiveData<UserInputState> displayedExpression;
    private MutableLiveData<String> displayedResult;



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

    public LiveData<UserInputState> getDisplayedExpression() {
        if (displayedExpression == null) {
            displayedExpression = new MutableLiveData<>();
        }

        if (inputStateSubject == null) {
            initExpressionObservable();
            loadSavedData();
        }

        return displayedExpression;
    }

    private void initExpressionObservable() {
        inputStateSubject = BehaviorSubject
                .createDefault(new UserInputState());
        disposables.add(inputStateSubject.toFlowable(BackpressureStrategy.LATEST)
                .subscribe(inputState -> displayedExpression.setValue(inputState)));

        disposables.add(inputStateSubject.toFlowable(BackpressureStrategy.LATEST)
                .observeOn(Schedulers.computation())
                .map(userInputState -> mainInteractor.calculateExpression(userInputState.getExpression()))
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
        HistoryData historyData = calculatorDataSavedStateHandle.get(SAVED_STATE_HANDLE);

        if (inputStateSubject.getValue() != null
                && !inputStateSubject.getValue().getExpression().isEmpty()) {
            return;
        }

        if (historyData == null) {
            disposables.add(loadDataObservable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(savedData -> {
                        if (inputStateSubject.getValue().getExpression().isEmpty()) {
                            UserInputState inputState = new UserInputState(savedData,
                                    DEFAULT_USER_INPUT_CURSOR_POSITION);
                            inputStateSubject.onNext(inputState);
                        }
                    }));
        } else {
            UserInputState inputState = new UserInputState(historyData.getExpression(),
                    DEFAULT_USER_INPUT_CURSOR_POSITION);
            inputStateSubject.onNext(inputState);
        }

    }

    public void saveCalculatorDataToSavedStateHandle() {
        String screenData = "";
        String resultData = "";
        if (inputStateSubject != null && displayedResult != null) {
            screenData = inputStateSubject.getValue() != null ? inputStateSubject.getValue().getExpression() : "";
            resultData = displayedResult.getValue() != null ? displayedResult.getValue() : "";
        }

        HistoryData historyData = new HistoryData(screenData, resultData, System.currentTimeMillis());

        calculatorDataSavedStateHandle.set(SAVED_STATE_HANDLE, historyData);
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
        UserInputState inputState = new UserInputState(expression,
                DEFAULT_USER_INPUT_CURSOR_POSITION);
        inputStateSubject.onNext(inputState);
    }

    private void updateDisplayedExpression(String symbol, int position) {
        UserInputState currentInputState = inputStateSubject.getValue();

        if (currentInputState == null) {
            currentInputState = new UserInputState();
        }

        UserInputState inputState;
        if (position < 0 || position > currentInputState.getExpression().length()) {
            inputState = new UserInputState(currentInputState.getExpression() + symbol,
                    DEFAULT_USER_INPUT_CURSOR_POSITION);
        } else {
            String expression = currentInputState.getExpression();
            String expressionPartOne = expression.substring(0, position);
            String expressionPartTwo = expression.substring(position);
            inputState = new UserInputState(expressionPartOne + symbol + expressionPartTwo,
                    position + symbol.length());
        }
        inputStateSubject.onNext(inputState);
    }

    private void clearDisplayedExpression() {
        inputStateSubject.onNext(new UserInputState());
        displayedResult.setValue("");
    }

    private void handleBackspacePressed(int position) {
        UserInputState inputState = inputStateSubject.getValue();

        if (inputState == null) {
            return;
        }

        String expression = inputState.getExpression();

        if (expression.isEmpty() || position > expression.length()) {
            return;
        }

        if (position > 0 && expression.length() > 1) {
            String expressionPartOne = expression.substring(0, position - 1);
            String expressionPartTwo = expression.substring(position);
            inputState = new UserInputState(expressionPartOne + expressionPartTwo, --position);
            inputStateSubject.onNext(inputState);
        } else if (position != 0 && expression.length() > 1) {
            inputState = new UserInputState(expression.substring(0, expression.length() - 1), --position);
            inputStateSubject.onNext(inputState);
        } else if (position != 0) {
            clearDisplayedExpression();
        }
    }

    private void handleEqualsPressed() {
        String resultText = displayedResult.getValue();
        if (resultText != null) {
            saveHistory();
            UserInputState inputState = new UserInputState(resultText,
                    DEFAULT_USER_INPUT_CURSOR_POSITION);
            inputStateSubject.onNext(inputState);
        }
    }

    private void saveHistory() {
        UserInputState inputState = displayedExpression.getValue();
        String result = displayedResult.getValue();

        if (inputState != null && result != null) {
            HistoryData historyData = new HistoryData(inputState.getExpression(), result,
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
