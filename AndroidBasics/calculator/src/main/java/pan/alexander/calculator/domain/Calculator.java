package pan.alexander.calculator.domain;

import android.util.Log;

import com.udojava.evalex.Expression;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import static pan.alexander.calculator.App.LOG_TAG;
import static pan.alexander.calculator.util.Utils.spannedStringFromHtml;

public class Calculator {
    private final Map<String, String> symbolToExpressionMap;

    @Inject
    public Calculator() {
        symbolToExpressionMap = new HashMap<>();

        fillHtmlCodeToExpressionMap();
    }


    public String calculateExpression(String expressionLine) {

        expressionLine = convertExpressionLineToMathExpression(expressionLine);

        String result = "";
        try {
            Expression expression = new Expression(expressionLine);
            result = expression.eval(false).toString();
        } catch (Exception e) {
            Log.w(LOG_TAG, "Calculator exception " + e.getMessage() + " " + expressionLine);
        }

        return result;
    }


    private void fillHtmlCodeToExpressionMap() {
        symbolToExpressionMap.put("\\%", "/100");
        symbolToExpressionMap.put("&#8730;", "SQRT");
        symbolToExpressionMap.put(spannedStringFromHtml("&#8730;"), "SQRT");
        symbolToExpressionMap.put("&#247;", "/");
        symbolToExpressionMap.put(spannedStringFromHtml("&#247;"), "/");
        symbolToExpressionMap.put("&#215;", "*");
        symbolToExpressionMap.put(spannedStringFromHtml("&#215;"), "*");
    }

    private String convertExpressionLineToMathExpression(String expressionLine) {
        for (Map.Entry<String, String> entry: symbolToExpressionMap.entrySet()) {
            expressionLine = expressionLine.replaceAll(entry.getKey(), entry.getValue());
        }
        return expressionLine;
    }
}
