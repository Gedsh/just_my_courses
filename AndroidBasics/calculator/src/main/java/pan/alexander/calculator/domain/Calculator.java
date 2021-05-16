package pan.alexander.calculator.domain;

import android.util.Log;

import com.udojava.evalex.Expression;

import java.math.BigDecimal;

import javax.inject.Inject;

import pan.alexander.calculator.util.ExpressionConverter;

import static pan.alexander.calculator.App.LOG_TAG;

public class Calculator {

    @Inject
    public Calculator() {
    }

    public String calculateExpression(String expressionLine, int precision) {

        String preparedExpression = "";
        String result = "";

        try {
            preparedExpression = convertExpressionLineToMathExpression(expressionLine);
            Expression expression = new Expression(preparedExpression).setPrecision(precision);
            BigDecimal rawResult = expression.eval().stripTrailingZeros();
            result = rawResult.toPlainString();
            if (result.length() - 5 /*experimental value*/ > precision) {
                result = rawResult.toEngineeringString();
            }
        } catch (Throwable e) {
            if (preparedExpression.isEmpty()) {
                Log.w(LOG_TAG, "Calculator exception: " + e.getMessage() + " " + expressionLine);
            } else {
                Log.w(LOG_TAG, "Calculator exception: " + e.getMessage() + " " + preparedExpression);
            }
        }

        return result;
    }

    private String convertExpressionLineToMathExpression(String expressionLine) {
        ExpressionConverter expressionConverter = new ExpressionConverter(expressionLine);
        expressionConverter.handleExpressionWithPercents()
                .replaceHtmlCodesWithExpression()
                .appendBracketsToSQRT();
        return expressionConverter.getResult();
    }
}
