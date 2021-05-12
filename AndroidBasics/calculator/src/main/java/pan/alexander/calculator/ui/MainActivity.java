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
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import pan.alexander.calculator.App;
import pan.alexander.calculator.R;
import pan.alexander.calculator.databinding.MainLayoutBinding;
import pan.alexander.calculator.domain.MainInteractor;
import pan.alexander.calculator.domain.entities.UserInputState;
import pan.alexander.calculator.util.ButtonToSymbolMapping;
import pan.alexander.calculator.util.Utils;
import pan.alexander.calculator.viewmodel.MainViewModel;

import static pan.alexander.calculator.util.AppConstants.DEFAULT_USER_INPUT_CURSOR_POSITION;
import static pan.alexander.calculator.util.AppConstants.VIEW_MODE_PREFERENCE;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MainViewModel mainViewModel;
    private MainLayoutBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setViewMode();

        super.onCreate(savedInstanceState);

        binding = MainLayoutBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        hideSoftKeyboard();

        setOnClickListeners();

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
                    int cursorPosition = binding.editTextUserInput.getSelectionStart();
                    mainViewModel.handleInputTextChanged(s.toString(), cursorPosition);
                }
            }
        });
    }

    private void observeDataChanges() {
        final Observer<UserInputState> userInputStateObserver = inputState -> {

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                binding.editTextUserInput.setText(Html.fromHtml(inputState.getExpression(), Html.FROM_HTML_MODE_LEGACY));
            } else {
                binding.editTextUserInput.setText(Html.fromHtml(inputState.getExpression()));
            }

            int cursorPosition = inputState.getCursorPosition();
            if (binding.editTextUserInput.isFocused() && cursorPosition >= 0) {
                binding.editTextUserInput.setSelection(cursorPosition);
            }
        };
        mainViewModel.getDisplayedExpression().observe(this, userInputStateObserver);

        final Observer<String> resultObserver = result ->
                binding.textIntermediateResult.setText(result);
        mainViewModel.getDisplayedResult().observe(this, resultObserver);
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
            mainViewModel.updateDisplayedExpression(incomingExpression.toString());
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
        boolean cursorIsVisible = binding.editTextUserInput.isFocused();

        int cursorPosition = DEFAULT_USER_INPUT_CURSOR_POSITION;
        if (cursorIsVisible) {
            cursorPosition = binding.editTextUserInput.getSelectionStart();
        }

        if (id == R.id.buttonOne) {
            mainViewModel.handleButtonPressed(ButtonToSymbolMapping.BUTTON_ONE, cursorPosition);
        } else if (id == R.id.buttonTwo) {
            mainViewModel.handleButtonPressed(ButtonToSymbolMapping.BUTTON_TWO, cursorPosition);
        } else if (id == R.id.buttonThree) {
            mainViewModel.handleButtonPressed(ButtonToSymbolMapping.BUTTON_THREE, cursorPosition);
        } else if (id == R.id.buttonFour) {
            mainViewModel.handleButtonPressed(ButtonToSymbolMapping.BUTTON_FOUR, cursorPosition);
        } else if (id == R.id.buttonFive) {
            mainViewModel.handleButtonPressed(ButtonToSymbolMapping.BUTTON_FIVE, cursorPosition);
        } else if (id == R.id.buttonSix) {
            mainViewModel.handleButtonPressed(ButtonToSymbolMapping.BUTTON_SIX, cursorPosition);
        } else if (id == R.id.buttonSeven) {
            mainViewModel.handleButtonPressed(ButtonToSymbolMapping.BUTTON_SEVEN, cursorPosition);
        } else if (id == R.id.buttonEight) {
            mainViewModel.handleButtonPressed(ButtonToSymbolMapping.BUTTON_EIGHT, cursorPosition);
        } else if (id == R.id.buttonNine) {
            mainViewModel.handleButtonPressed(ButtonToSymbolMapping.BUTTON_NINE, cursorPosition);
        } else if (id == R.id.buttonZero) {
            mainViewModel.handleButtonPressed(ButtonToSymbolMapping.BUTTON_ZERO, cursorPosition);
        } else if (id == R.id.buttonDivide) {
            mainViewModel.handleButtonPressed(ButtonToSymbolMapping.BUTTON_DIVIDE, cursorPosition);
        } else if (id == R.id.buttonMultiply) {
            mainViewModel.handleButtonPressed(ButtonToSymbolMapping.BUTTON_MULTIPLY, cursorPosition);
        } else if (id == R.id.buttonMinus) {
            mainViewModel.handleButtonPressed(ButtonToSymbolMapping.BUTTON_MINUS, cursorPosition);
        } else if (id == R.id.buttonPlus) {
            mainViewModel.handleButtonPressed(ButtonToSymbolMapping.BUTTON_PLUS, cursorPosition);
        } else if (id == R.id.buttonPercent) {
            mainViewModel.handleButtonPressed(ButtonToSymbolMapping.BUTTON_PERCENT, cursorPosition);
        } else if (id == R.id.buttonSQRT) {
            mainViewModel.handleButtonPressed(ButtonToSymbolMapping.BUTTON_SQRT, cursorPosition);
        } else if (id == R.id.buttonPowered) {
            mainViewModel.handleButtonPressed(ButtonToSymbolMapping.BUTTON_POWERED, cursorPosition);
        } else if (id == R.id.buttonEquals) {
            resetCursorPositionIfCursorVisible(cursorIsVisible);
            mainViewModel.handleButtonPressed(ButtonToSymbolMapping.BUTTON_EQUALS, cursorPosition);
        } else if (id == R.id.buttonPoint) {
            mainViewModel.handleButtonPressed(ButtonToSymbolMapping.BUTTON_POINT, cursorPosition);
        } else if (id == R.id.buttonClear) {
            resetCursorPositionIfCursorVisible(cursorIsVisible);
            mainViewModel.handleButtonPressed(ButtonToSymbolMapping.BUTTON_CLEAR, cursorPosition);
        } else if (id == R.id.buttonBackspace) {
            mainViewModel.handleButtonPressed(ButtonToSymbolMapping.BUTTON_BACKSPACE, cursorPosition);
        } else if (id == R.id.buttonBracketsOpen) {
            mainViewModel.handleButtonPressed(ButtonToSymbolMapping.BUTTON_BRACKETS_OPEN, cursorPosition);
        } else if (id == R.id.buttonBracketsClose) {
            mainViewModel.handleButtonPressed(ButtonToSymbolMapping.BUTTON_BRACKETS_CLOSE, cursorPosition);
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

    private void resetCursorPositionIfCursorVisible(boolean cursorIsVisible) {
        if (cursorIsVisible) {
            binding.editTextUserInput.clearFocus();
        }
    }

    private void toastNotImplemented() {
        Toast.makeText(this, R.string.toastNotImplemented, Toast.LENGTH_SHORT).show();
    }
}
