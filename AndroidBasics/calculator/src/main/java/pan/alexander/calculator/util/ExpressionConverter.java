package pan.alexander.calculator.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static pan.alexander.calculator.util.ButtonToSymbolMapping.BUTTON_DIVIDE;
import static pan.alexander.calculator.util.ButtonToSymbolMapping.BUTTON_MULTIPLY;
import static pan.alexander.calculator.util.ButtonToSymbolMapping.BUTTON_SQRT;
import static pan.alexander.calculator.util.Utils.spannedStringFromHtml;

public class ExpressionConverter {
    private static final Pattern patternSQRT = Pattern.compile("SQRT(\\d+(?:\\.\\d+)?)");
    private static final Pattern patternPercentComplex = Pattern.compile("(^.+?)[+\\\\-]\\d+(?:\\.\\d+)?%");
    private static final Pattern patternPercentCommon = Pattern.compile("(\\d+(?:\\.\\d+)?)%");

    private static final Map<String, String> symbolToExpressionMap = new HashMap<String, String>() {{
        put(BUTTON_SQRT, "SQRT");
        put(spannedStringFromHtml(BUTTON_SQRT), "SQRT");
        put(BUTTON_DIVIDE, "/");
        put(spannedStringFromHtml(BUTTON_DIVIDE), "/");
        put(BUTTON_MULTIPLY, "*");
        put(spannedStringFromHtml(BUTTON_MULTIPLY), "*");
    }};

    private String expression;

    public ExpressionConverter(String expression) {
        this.expression = expression;
    }

    public ExpressionConverter handleExpressionWithPercents() throws InterruptedException {
        StringBuilder expressionLineStringBuilder = new StringBuilder(expression);
        while (expressionLineStringBuilder.indexOf("%") >= 0) {

            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException("ExpressionConverter interrupted");
            }

            Matcher matcherPercent1 = patternPercentComplex.matcher(expressionLineStringBuilder);
            Matcher matcherPercent2 = patternPercentCommon.matcher(expressionLineStringBuilder);

            if (matcherPercent1.find()) {
                String expressionBuilder = matcherPercent1.group() + "*(" + matcherPercent1.group(1) + ")";
                expressionLineStringBuilder.replace(0, matcherPercent1.group().length(),
                        replacePercentsWithExpression(expressionBuilder));
            } else if (matcherPercent2.find()) {
                expressionLineStringBuilder = new StringBuilder(replacePercentsWithExpression(expressionLineStringBuilder.toString()));
            } else {
                throw new ArithmeticException("ExpressionConverter cannot handle expression with percents " + expression);
            }
        }

        expression = expressionLineStringBuilder.toString();

        return this;
    }

    private String replacePercentsWithExpression(String line) {
        Matcher matcherPercent = patternPercentCommon.matcher(line);
        if (matcherPercent.find()) {
            return matcherPercent.replaceAll("($1/100)");
        }
        return line;
    }

    public ExpressionConverter replaceHtmlCodesWithExpression() throws InterruptedException {
        for (Map.Entry<String, String> entry : symbolToExpressionMap.entrySet()) {

            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException("ExpressionConverter interrupted");
            }

            expression = expression.replace(entry.getKey(), entry.getValue());
        }

        return this;
    }

    public void appendBracketsToSQRT() {
        Matcher matcherSQRT = patternSQRT.matcher(expression);
        if (matcherSQRT.find()) {
            expression = matcherSQRT.replaceAll("SQRT($1)");
        }
    }

    public String getResult() {
        return expression;
    }
}
