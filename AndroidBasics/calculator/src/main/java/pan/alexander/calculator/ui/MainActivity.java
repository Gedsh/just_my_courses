package pan.alexander.calculator.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import pan.alexander.calculator.R;
import pan.alexander.calculator.databinding.MainLayoutBinding;
import pan.alexander.calculator.util.ButtonToSymbolMapping;
import pan.alexander.calculator.viewmodel.MainViewModel;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MainViewModel mainViewModel;
    private MainLayoutBinding binding;
    private int savedCursorPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = MainLayoutBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        hideSoftKeyboard();

        setOnClickListeners();

        observeDataChanges();

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
        binding.buttonAux.setOnClickListener(this);
    }

    private void observeDataChanges() {
        final Observer<String> displayedExpressionObserver = displayedExpression -> {

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                binding.editTextUserInput.setText(Html.fromHtml(displayedExpression, Html.FROM_HTML_MODE_LEGACY));
            } else {
                binding.editTextUserInput.setText(Html.fromHtml(displayedExpression));
            }

            if (binding.editTextUserInput.isFocused() && savedCursorPosition >= 0) {
                binding.editTextUserInput.setSelection(savedCursorPosition);
            }
        };
        mainViewModel.getDisplayedExpression().observe(this, displayedExpressionObserver);

        final Observer<String> resultObserver = result ->
                binding.textIntermediateResult.setText(result);
        mainViewModel.getDisplayedResult().observe(this, resultObserver);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {

        mainViewModel.setCalculatorData();

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        boolean cursorIsVisible = binding.editTextUserInput.isFocused();

        int cursorPosition = -1;
        if (cursorIsVisible) {
            cursorPosition = binding.editTextUserInput.getSelectionStart();
            savedCursorPosition = cursorPosition;
        }

        if (id == R.id.buttonOne) {
            shiftCursorForwardIfCursorVisible(cursorIsVisible);
            mainViewModel.handleButtonPressed(ButtonToSymbolMapping.buttonOne, cursorPosition);
        } else if (id == R.id.buttonTwo) {
            shiftCursorForwardIfCursorVisible(cursorIsVisible);
            mainViewModel.handleButtonPressed(ButtonToSymbolMapping.buttonTwo, cursorPosition);
        } else if (id == R.id.buttonThree) {
            shiftCursorForwardIfCursorVisible(cursorIsVisible);
            mainViewModel.handleButtonPressed(ButtonToSymbolMapping.buttonThree, cursorPosition);
        } else if (id == R.id.buttonFour) {
            shiftCursorForwardIfCursorVisible(cursorIsVisible);
            mainViewModel.handleButtonPressed(ButtonToSymbolMapping.buttonFour, cursorPosition);
        } else if (id == R.id.buttonFive) {
            shiftCursorForwardIfCursorVisible(cursorIsVisible);
            mainViewModel.handleButtonPressed(ButtonToSymbolMapping.buttonFive, cursorPosition);
        } else if (id == R.id.buttonSix) {
            shiftCursorForwardIfCursorVisible(cursorIsVisible);
            mainViewModel.handleButtonPressed(ButtonToSymbolMapping.buttonSix, cursorPosition);
        } else if (id == R.id.buttonSeven) {
            shiftCursorForwardIfCursorVisible(cursorIsVisible);
            mainViewModel.handleButtonPressed(ButtonToSymbolMapping.buttonSeven, cursorPosition);
        } else if (id == R.id.buttonEight) {
            shiftCursorForwardIfCursorVisible(cursorIsVisible);
            mainViewModel.handleButtonPressed(ButtonToSymbolMapping.buttonEight, cursorPosition);
        } else if (id == R.id.buttonNine) {
            shiftCursorForwardIfCursorVisible(cursorIsVisible);
            mainViewModel.handleButtonPressed(ButtonToSymbolMapping.buttonNine, cursorPosition);
        } else if (id == R.id.buttonZero) {
            shiftCursorForwardIfCursorVisible(cursorIsVisible);
            mainViewModel.handleButtonPressed(ButtonToSymbolMapping.buttonZero, cursorPosition);
        } else if (id == R.id.buttonDivide) {
            shiftCursorForwardIfCursorVisible(cursorIsVisible);
            mainViewModel.handleButtonPressed(ButtonToSymbolMapping.buttonDivide, cursorPosition);
        } else if (id == R.id.buttonMultiply) {
            shiftCursorForwardIfCursorVisible(cursorIsVisible);
            mainViewModel.handleButtonPressed(ButtonToSymbolMapping.buttonMultiply, cursorPosition);
        } else if (id == R.id.buttonMinus) {
            shiftCursorForwardIfCursorVisible(cursorIsVisible);
            mainViewModel.handleButtonPressed(ButtonToSymbolMapping.buttonMinus, cursorPosition);
        } else if (id == R.id.buttonPlus) {
            shiftCursorForwardIfCursorVisible(cursorIsVisible);
            mainViewModel.handleButtonPressed(ButtonToSymbolMapping.buttonPlus, cursorPosition);
        } else if (id == R.id.buttonPercent) {
            shiftCursorForwardIfCursorVisible(cursorIsVisible);
            mainViewModel.handleButtonPressed(ButtonToSymbolMapping.buttonPercent, cursorPosition);
        } else if (id == R.id.buttonSQRT) {
            shiftCursorForwardIfCursorVisible(cursorIsVisible, ButtonToSymbolMapping.buttonSQRT.length());
            mainViewModel.handleButtonPressed(ButtonToSymbolMapping.buttonSQRT, cursorPosition);
        } else if (id == R.id.buttonPowered) {
            shiftCursorForwardIfCursorVisible(cursorIsVisible);
            mainViewModel.handleButtonPressed(ButtonToSymbolMapping.buttonPowered, cursorPosition);
        } else if (id == R.id.buttonEquals) {
            resetCursorPositionIfCursorVisible(cursorIsVisible);
            mainViewModel.handleButtonPressed(ButtonToSymbolMapping.buttonEquals, cursorPosition);
        } else if (id == R.id.buttonPoint) {
            shiftCursorForwardIfCursorVisible(cursorIsVisible);
            mainViewModel.handleButtonPressed(ButtonToSymbolMapping.buttonPoint, cursorPosition);
        } else if (id == R.id.buttonClear) {
            resetCursorPositionIfCursorVisible(cursorIsVisible);
            mainViewModel.handleButtonPressed(ButtonToSymbolMapping.buttonClear, cursorPosition);
        } else if (id == R.id.buttonBackspace) {
            shiftCursorBackwardIfCursorVisible(cursorIsVisible);
            mainViewModel.handleButtonPressed(ButtonToSymbolMapping.buttonBackspace, cursorPosition);
        } else if (id == R.id.buttonBracketsOpen) {
            shiftCursorForwardIfCursorVisible(cursorIsVisible);
            mainViewModel.handleButtonPressed(ButtonToSymbolMapping.buttonBracketsOpen, cursorPosition);
        } else if (id == R.id.buttonBracketsClose) {
            shiftCursorForwardIfCursorVisible(cursorIsVisible);
            mainViewModel.handleButtonPressed(ButtonToSymbolMapping.buttonBracketsClose, cursorPosition);
        } else if (id == R.id.buttonHistory) {
            toastNotImplemented();
        } else if (id == R.id.buttonAux) {
            toastNotImplemented();
        } else {
            toastNotImplemented();
        }
    }

    private void shiftCursorForwardIfCursorVisible(boolean cursorIsVisible) {
        if (cursorIsVisible) {
            savedCursorPosition++;
        }
    }

    private void shiftCursorForwardIfCursorVisible(boolean cursorIsVisible, int step) {
        if (cursorIsVisible) {
            savedCursorPosition += step;
        }
    }

    private void shiftCursorBackwardIfCursorVisible(boolean cursorIsVisible) {
        if (cursorIsVisible) {
            savedCursorPosition--;
        }
    }

    private void resetCursorPositionIfCursorVisible(boolean cursorIsVisible) {
        if (cursorIsVisible) {
            binding.editTextUserInput.clearFocus();
            savedCursorPosition = -1;
        }
    }

    private void toastNotImplemented() {
        Toast.makeText(this, R.string.toastNotImplemented, Toast.LENGTH_SHORT).show();
    }
}
