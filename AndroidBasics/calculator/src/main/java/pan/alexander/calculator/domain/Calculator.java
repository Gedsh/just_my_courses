package pan.alexander.calculator.domain;

import android.util.Log;

import com.udojava.evalex.Expression;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import static pan.alexander.calculator.App.LOG_TAG;

public class Calculator {
    private final Map<String, String> symbolToExpressionMap;

    @Inject
    public Calculator() {
        symbolToExpressionMap = new HashMap<>();

        fillUnicodeToExpressionMap();
    }

    private void fillUnicodeToExpressionMap() {
        symbolToExpressionMap.put("\\%", "/100");
    }

    public String calculateExpression(String expressionLine) {

        expressionLine = convertExpressionLineToMathExpression(expressionLine);

        String result = "";
        try {
            Expression expression = new Expression(expressionLine);
            result = expression.eval(false).toString();
        } catch (Exception e) {
            Log.w(LOG_TAG, "Calculator exception " + e.getMessage());
        }

        return result;
    }

    private String convertExpressionLineToMathExpression(String expressionLine) {
        for (Map.Entry<String, String> entry: symbolToExpressionMap.entrySet()) {
            expressionLine = expressionLine.replaceAll(entry.getKey(), entry.getValue());
        }
        return expressionLine;
    }
}
