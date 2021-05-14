package pan.alexander.calculator.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import pan.alexander.calculator.App;
import pan.alexander.calculator.R;
import pan.alexander.calculator.databinding.MainLayoutBinding;
import pan.alexander.calculator.domain.MainInteractor;
import pan.alexander.calculator.util.ButtonToSymbolMapping;
import pan.alexander.calculator.util.Utils;
import pan.alexander.calculator.viewmodel.MainViewModel;

import static android.view.ViewConfiguration.getLongPressTimeout;
import static pan.alexander.calculator.util.AppConstants.INTERVAL_BACKSPACE_LONG_PRESSING;
import static pan.alexander.calculator.util.AppConstants.VIEW_MODE_PREFERENCE;
import static pan.alexander.calculator.util.Utils.spannedFromHtml;
import static pan.alexander.calculator.util.Utils.spannedStringFromHtml;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    private MainViewModel mainViewModel;
    private MainLayoutBinding binding;
    private Handler touchHandlerBackspace;
    private Runnable touchActionBackspace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setViewMode();

        super.onCreate(savedInstanceState);

        binding = MainLayoutBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        hideSoftKeyboard();

        setOnClickListeners();

        initOnTouchListener();

        observeDataChanges();

        checkIncomingIntent(getIntent());

        addInputTextChangedListenerForPasteHandling();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        checkIncomingIntent(intent);
    }

    private void setViewMode() {

        MainInteractor mainInteractor = App.getInstance().getDaggerComponent().getMainInteractor();

        String viewModeValue = mainInteractor.getStringPreference(VIEW_MODE_PREFERENCE);

        Utils.setViewMode(viewModeValue);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void hideSoftKeyboard() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            binding.editTextUserInput.setShowSoftInputOnFocus(false);
        } else {
            binding.editTextUserInput.setOnTouchListener((v, event) -> {
                v.onTouchEvent(event);
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                return true;
            });
        }
    }

    private void setOnClickListeners() {
        binding.buttonOne.setOnClickListener(this);
        binding.buttonTwo.setOnClickListener(this);
        binding.buttonThree.setOnClickListener(this);
        binding.buttonFour.setOnClickListener(this);
        binding.buttonFive.setOnClickListener(this);
        binding.buttonSix.setOnClickListener(this);
        binding.buttonSeven.setOnClickListener(this);
        binding.buttonEight.setOnClickListener(this);
        binding.buttonNine.setOnClickListener(this);
        binding.buttonZero.setOnClickListener(this);

        binding.buttonDivide.setOnClickListener(this);
        binding.buttonMultiply.setOnClickListener(this);
        binding.buttonMinus.setOnClickListener(this);
        binding.buttonPlus.setOnClickListener(this);
        if (binding.buttonPercent != null) {
            binding.buttonPercent.setOnClickListener(this);
        }
        binding.buttonSQRT.setOnClickListener(this);
        binding.buttonPowered.setOnClickListener(this);
        binding.buttonEquals.setOnClickListener(this);

        binding.buttonPoint.setOnClickListener(this);
        binding.buttonClear.setOnClickListener(this);
        binding.buttonBackspace.setOnClickListener(this);
        binding.buttonBracketsOpen.setOnClickListener(this);
        binding.buttonBracketsClose.setOnClickListener(this);
        binding.buttonHistory.setOnClickListener(this);
        binding.buttonSettings.setOnClickListener(this);
    }

    private void initOnTouchListener() {
        binding.buttonBackspace.setOnTouchListener(this);

        touchActionBackspace = new Runnable() {
            @Override public void run() {
                if (isCursorVisible()) {
                    binding.editTextUserInput.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
                } else {
                    handleButtonPressed(ButtonToSymbolMapping.BUTTON_BACKSPACE);
                }

                touchHandlerBackspace.postDelayed(this, INTERVAL_BACKSPACE_LONG_PRESSING);
            }
        };
    }

    private void addInputTextChangedListenerForPasteHandling() {
        binding.editTextUserInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                boolean cursorIsVisible = binding.editTextUserInput.isFocused();

                if (cursorIsVisible) {
                    mainViewModel.handleInputTextChanged(s.toString());
                }
            }
        });
    }

    private void observeDataChanges() {
        final Observer<String> expressionObserver = expression -> {

            if (isCursorVisible()) {
                return;
            }

            setUserInputText(expression);
        };
        mainViewModel.getDisplayedExpression().observe(this, expressionObserver);

        final Observer<String> resultObserver = result ->
                binding.textIntermediateResult.setText(result);
        mainViewModel.getDisplayedResult().observe(this, resultObserver);
    }

    private void setUserInputText(String text) {
        EditText editTextUserInput = binding.editTextUserInput;
        Spanned spannedText = spannedFromHtml(text);
        editTextUserInput.setText(spannedStringFromHtml(text));
        editTextUserInput.setSelection(spannedText.length());
    }

    private void insertUserInputText(String text) {
        EditText editTextUserInput = binding.editTextUserInput;
        int cursorPosition = editTextUserInput.getSelectionStart();
        Spanned spannedText = spannedFromHtml(text);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(editTextUserInput.getText());
        spannableStringBuilder.insert(cursorPosition, spannedText);
        editTextUserInput.setText(spannableStringBuilder);
        editTextUserInput.setSelection(cursorPosition + spannedText.length());
    }

    private void checkIncomingIntent(Intent intent) {
        if (intent == null) {
            return;
        }

        CharSequence incomingExpression = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            incomingExpression = intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT);
        }

        if (incomingExpression != null && !incomingExpression.toString().isEmpty()) {
            if (isCursorVisible()) {
                binding.editTextUserInput.setText(incomingExpression.toString());
                binding.editTextUserInput.setSelection(incomingExpression.length());
            } else {
                mainViewModel.setDisplayedExpression(incomingExpression.toString());
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        hideActionBar();
    }

    private void hideActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {

        mainViewModel.saveCalculatorDataToSavedStateHandle();

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.buttonOne) {
            handleButtonPressed(ButtonToSymbolMapping.BUTTON_ONE);
        } else if (id == R.id.buttonTwo) {
            handleButtonPressed(ButtonToSymbolMapping.BUTTON_TWO);
        } else if (id == R.id.buttonThree) {
            handleButtonPressed(ButtonToSymbolMapping.BUTTON_THREE);
        } else if (id == R.id.buttonFour) {
            handleButtonPressed(ButtonToSymbolMapping.BUTTON_FOUR);
        } else if (id == R.id.buttonFive) {
            handleButtonPressed(ButtonToSymbolMapping.BUTTON_FIVE);
        } else if (id == R.id.buttonSix) {
            handleButtonPressed(ButtonToSymbolMapping.BUTTON_SIX);
        } else if (id == R.id.buttonSeven) {
            handleButtonPressed(ButtonToSymbolMapping.BUTTON_SEVEN);
        } else if (id == R.id.buttonEight) {
            handleButtonPressed(ButtonToSymbolMapping.BUTTON_EIGHT);
        } else if (id == R.id.buttonNine) {
            handleButtonPressed(ButtonToSymbolMapping.BUTTON_NINE);
        } else if (id == R.id.buttonZero) {
            handleButtonPressed(ButtonToSymbolMapping.BUTTON_ZERO);
        } else if (id == R.id.buttonDivide) {
            handleButtonPressed(ButtonToSymbolMapping.BUTTON_DIVIDE);
        } else if (id == R.id.buttonMultiply) {
            handleButtonPressed(ButtonToSymbolMapping.BUTTON_MULTIPLY);
        } else if (id == R.id.buttonMinus) {
            handleButtonPressed(ButtonToSymbolMapping.BUTTON_MINUS);
        } else if (id == R.id.buttonPlus) {
            handleButtonPressed(ButtonToSymbolMapping.BUTTON_PLUS);
        } else if (id == R.id.buttonPercent) {
            handleButtonPressed(ButtonToSymbolMapping.BUTTON_PERCENT);
        } else if (id == R.id.buttonSQRT) {
            handleButtonPressed(ButtonToSymbolMapping.BUTTON_SQRT);
        } else if (id == R.id.buttonPowered) {
            handleButtonPressed(ButtonToSymbolMapping.BUTTON_POWERED);
        } else if (id == R.id.buttonEquals) {
            resetCursorPositionIfCursorVisible();
            handleButtonPressed(ButtonToSymbolMapping.BUTTON_EQUALS);
        } else if (id == R.id.buttonPoint) {
            handleButtonPressed(ButtonToSymbolMapping.BUTTON_POINT);
        } else if (id == R.id.buttonClear) {
            resetCursorPositionIfCursorVisible();
            handleButtonPressed(ButtonToSymbolMapping.BUTTON_CLEAR);
        } else if (id == R.id.buttonBackspace) {
            if (isCursorVisible()) {
                binding.editTextUserInput.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
            } else {
                handleButtonPressed(ButtonToSymbolMapping.BUTTON_BACKSPACE);
            }
        } else if (id == R.id.buttonBracketsOpen) {
            handleButtonPressed(ButtonToSymbolMapping.BUTTON_BRACKETS_OPEN);
        } else if (id == R.id.buttonBracketsClose) {
            handleButtonPressed(ButtonToSymbolMapping.BUTTON_BRACKETS_CLOSE);
        } else if (id == R.id.buttonHistory) {
            Intent intent = new Intent(this, HistoryActivity.class);
            startActivity(intent);
        } else if (id == R.id.buttonSettings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        } else {
            toastNotImplemented();
        }
    }

    private void handleButtonPressed(String symbol) {
        if (isCursorVisible()) {
            Editable currentDisplayedExpression = binding.editTextUserInput.getText();
            if (currentDisplayedExpression == null) {
                setUserInputText(symbol);
            } else {
                insertUserInputText(symbol);
            }
        } else {
            mainViewModel.handleButtonPressed(symbol);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() != R.id.buttonBackspace) {
            return false;
        }

        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (touchHandlerBackspace != null) {
                    return true;
                }
                Looper looper = Looper.getMainLooper();
                if (looper != null) {
                    touchHandlerBackspace = new Handler();
                    touchHandlerBackspace.postDelayed(touchActionBackspace, getLongPressTimeout());
                }
                break;
            case MotionEvent.ACTION_UP:
                if (touchHandlerBackspace == null) {
                    return true;
                }
                touchHandlerBackspace.removeCallbacks(touchActionBackspace);
                touchHandlerBackspace = null;
                break;
        }
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (touchHandlerBackspace != null) {
            touchHandlerBackspace.removeCallbacks(touchActionBackspace);
        }
    }

    private boolean isCursorVisible() {
        return binding.editTextUserInput.isFocused();
    }

    private void resetCursorPositionIfCursorVisible() {
        if (isCursorVisible()) {
            binding.editTextUserInput.clearFocus();
        }
    }

    private void toastNotImplemented() {
        Toast.makeText(this, R.string.toastNotImplemented, Toast.LENGTH_SHORT).show();
    }
}
