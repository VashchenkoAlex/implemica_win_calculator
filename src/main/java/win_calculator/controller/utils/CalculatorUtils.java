package win_calculator.controller.utils;

import win_calculator.model.nodes.events.Event;
import win_calculator.model.nodes.events.EventType;
import win_calculator.model.nodes.events.Number;
import win_calculator.model.nodes.events.extra_operations.ExtraOperation;
import win_calculator.model.nodes.events.extra_operations.Percent;
import win_calculator.model.nodes.events.main_operations.MainOperation;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.LinkedList;

import static win_calculator.model.nodes.events.EventType.*;

public abstract class CalculatorUtils {

    private static final String COMA = ",";
    private static final String DOT = ".";
    private static final String IS_TEXT_REGEX = "^[^0-9]+";
    private static final String MINUS_REGEX = "^-.[0-9,]*";
    private static final String MINUS = "-";
    private static final String HISTORY_PATTERN = "################.################";
    private static final String SIMPLE_E_SEPARATOR = "e";
    private static final String FOURTEEN_DECIMAL_PART = "#.##############";
    private static final String INTEGER_AND_DECIMAL_PART = "0.0000000000000000";
    private static final int MAX_ROUND = 16;
    private static final int MAX_DECIMAL = 17;
    private static final String ARE_ZEROS_FIRST_REGEX = "^0.0[0-9]+";
    private static final String IS_ZERO_FIRST_REGEX = "^0.+";
    private static final String E_PART_OF_FORMAT = "#E0";
    private static final int SCALE = 16;
    private static final String PLUS = "+";
    private static final String BRACKET = " )";
    private static final String SPACE = "  ";
    private static final String E = "E";
    private static final String SPACES_REGEX = "\\s\\s";
    private static final String ADD_EXTRA_OPERATION_REGEX = "^.+[\\s{2}][^\\s{2}]+$";

    public static boolean isComaAbsent(String number) {

        return !number.contains(COMA);
    }


    public static String replaceDotToComa(String string) {

        return string.replace(DOT, COMA);
    }

    public static boolean isNotNumber(String string) {

        return string.matches(IS_TEXT_REGEX);
    }

    public static String addCapacity(String string) {

        String[] parts = {string, ""};
        String minus = "";
        String coma = "";
        if (string.matches(MINUS_REGEX)) {
            minus = MINUS;
            parts[0] = string.substring(1);
        }
        if (parts[0].contains(COMA)) {
            coma = COMA;
            parts = parts[0].split(COMA);
        }
        parts[0] = String.format("%,d", Long.parseLong(parts[0].replaceAll(" ", "")));
        String result;
        if (parts.length > 1) {
            result = minus + parts[0] + coma + parts[1];
        } else {
            result = minus + parts[0] + coma;
        }
        return result;
    }

    public static String getHistoryString(LinkedList<Event> history) {

        String result = "";
        for (Event event : history) {
            EventType type = event.getType();
            if (EXTRA_OPERATION.equals(type)) {
                result = addExtraOperationToString(result, ((ExtraOperation) event).getValue());
            } else if (NUMBER.equals(type)) {
                result += convertToString(((Number) event).getBigDecimalValue(), HISTORY_PATTERN);
            } else if (NEGATE.equals(type)) {
                result = addExtraOperationToString(result, ((ExtraOperation) event).getValue());
            } else if (PERCENT.equals(type)) {
                result += ((Percent) event).getValue();
            } else {
                result += ((MainOperation) event).getValue();
            }
        }
        return result;
    }

    public static String convertToString(BigDecimal incomeNumber, String pattern) {

        String result;
        if (incomeNumber != null) {
            BigDecimal number = incomeNumber;
            int scale = selectScale(number);
            number = setNewScale(number, scale);
            number = number.stripTrailingZeros();
            String thePattern = selectPattern(number, pattern);
            result = initFormatter(thePattern, selectSeparator(number)).format(number);
        } else {
            result = "0";
        }
        return result;
    }

    private static DecimalFormat initFormatter(String pattern, String separator) {

        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setExponentSeparator(separator);
        DecimalFormat formatter = new DecimalFormat(pattern);
        formatter.setDecimalFormatSymbols(symbols);
        return formatter;
    }

    private static String selectSeparator(BigDecimal number) {

        String separator = SIMPLE_E_SEPARATOR;
        if (getUnscaledLength(number) < 2) {
            separator = COMA + separator;
        }
        if (number.toString().contains(PLUS)) {
            separator += PLUS;
        }
        return separator;
    }

    private static String selectPattern(BigDecimal number, String pattern) {

        String currentPattern = pattern;
        String numberStr = number.toString();
        if (containsE(number) && !numberStr.matches(IS_ZERO_FIRST_REGEX)) {
            String[] parts = numberStr.split(E);
            int expCount = Math.abs(Integer.parseInt(parts[1]));
            if (number.toPlainString().contains(".")){
                number = setNewScale(number, SCALE + expCount);
                number = number.stripTrailingZeros();
            }
            int unscaledLnth = getUnscaledLength(number);
            if (parts[1].contains(MINUS)) {
                if (expCount > MAX_DECIMAL - unscaledLnth) {
                    currentPattern = preparePattern(unscaledLnth) + E_PART_OF_FORMAT;
                }
            } else if (parts[1].contains(PLUS)) {
                if (getWholeLength(number) > MAX_ROUND) {
                    currentPattern = preparePattern(unscaledLnth) + E_PART_OF_FORMAT;
                }
            }
        } else if (numberStr.matches(IS_ZERO_FIRST_REGEX)) {
            if (numberStr.matches(ARE_ZEROS_FIRST_REGEX) && isCorrectExponent(number)) {
                currentPattern = preparePattern(getUnscaledLength(number)) + E_PART_OF_FORMAT;
            }
        }
        return currentPattern;
    }

    private static int selectScale(BigDecimal number) {

        int scale = SCALE;
        String result = number.abs().toString();
        if (containsE(number) && !result.matches(IS_ZERO_FIRST_REGEX)) {
            --scale;
            String[] parts = result.split(E);
            scale += Math.abs(Integer.parseInt(parts[1]));
        } else if (result.matches(IS_ZERO_FIRST_REGEX)) {
            if (result.matches(ARE_ZEROS_FIRST_REGEX) && isCorrectExponent(number)) {
                scale += getExponent(number) - 1;
            }
        } else {
            scale -= getWholeLength(number);
        }
        return scale;
    }


    private static String preparePattern(int count) {

        return INTEGER_AND_DECIMAL_PART.substring(0, INTEGER_AND_DECIMAL_PART.length() - MAX_DECIMAL + count);
    }

    private static boolean isCorrectExponent(BigDecimal number) {

        DecimalFormat formatter = new DecimalFormat(FOURTEEN_DECIMAL_PART + E_PART_OF_FORMAT);
        String[] parts = formatter.format(number).split(E);
        boolean result = false;
        if (parts.length > 1) {
            int exponent = Math.abs(Integer.parseInt(parts[1]));
            result = exponent > 3 && parts[0].length() + exponent > MAX_ROUND;
        }
        return result;
    }

    private static int getExponent(BigDecimal number) {

        DecimalFormat formatter = new DecimalFormat(FOURTEEN_DECIMAL_PART + E_PART_OF_FORMAT);
        String[] parts = formatter.format(number).split(E);
        return Math.abs(Integer.parseInt(parts[1]));
    }

    private static int getUnscaledLength(BigDecimal number) {

        return number.abs().unscaledValue().toString().length();
    }

    private static int getWholeLength(BigDecimal number) {

        return number.abs().toBigInteger().toString().length();
    }

    private static BigDecimal setNewScale(BigDecimal number, int scale) {

        return number.setScale(scale, RoundingMode.HALF_UP);
    }

    private static boolean containsE(BigDecimal number) {

        return number.toString().contains(E);
    }


    private static String addExtraOperationToString(String currentStr, String symbol) {

        StringBuilder resultStr = new StringBuilder();
        if (currentStr.contains(SPACE)) {
            if (currentStr.matches(ADD_EXTRA_OPERATION_REGEX)) {
                String[] parts = currentStr.split(SPACES_REGEX);
                for (int i = 0; i < parts.length - 1; i++) {
                    resultStr.append(parts[i]).append(SPACE);
                }
                resultStr.append(symbol).append(parts[parts.length - 1]);
            } else {
                resultStr = new StringBuilder(currentStr + symbol);
            }
        } else if (!"".equals(currentStr)) {
            resultStr = new StringBuilder(symbol + currentStr);
        }
        return resultStr + BRACKET;
    }
}
