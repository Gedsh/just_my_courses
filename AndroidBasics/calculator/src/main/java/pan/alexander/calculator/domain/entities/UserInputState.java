package pan.alexander.calculator.domain.entities;

import androidx.annotation.NonNull;

import static pan.alexander.calculator.util.AppConstants.DEFAULT_USER_INPUT_CURSOR_POSITION;

public class UserInputState {
    private final String expression;
    private final int cursorPosition;

    public UserInputState(@NonNull String expression, int cursorPosition) {
        this.expression = expression;
        this.cursorPosition = cursorPosition;
    }

    public UserInputState() {
        this.expression = "";
        this.cursorPosition = DEFAULT_USER_INPUT_CURSOR_POSITION;
    }

    @NonNull
    public String getExpression() {
        return expression;
    }

    public int getCursorPosition() {
        return cursorPosition;
    }
}
