package win_calculator.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public abstract class StringUtils {

    private static final String COMA = ",";
    private static final String DOT = ".";
    private static final String ZERO = "0";
    private static final String MINUS_REGEX = "^-";
    private static final String BRACKET = " )";
    private static final String SPACE = "  ";
    private static final String SPACES_REGEX = "\\s\\s";
    private static final String ADD_EXTRA_OPERATION_REGEX = "^.+[\\s{2}][^\\s{2}]+$";
    private static final String ARE_ZEROS_FIRST_REGEX = "^0.0[0-9]+";
    private static final String IS_ZERO_FIRST_REGEX = "^0.+";
    private static final String E_PART_OF_FORMAT = "#E0";
    private static final int SCALE = 15;
    private static final int MAX_ROUND = 16;
    private static final int MAX_DECIMAL = 17;
    private static final int MAX_EXPONENT = 9999;
    private static final String FOURTEEN_DECIMAL_PART = "#.##############";
    private static final String INTEGER_AND_DECIMAL_PART = "#,##0.0000000000000000";
    private static final String E = "E";

    public static boolean isComaAbsent(String number) {

        return !number.contains(COMA);
    }

    public static String addExtraOperationToString(String currentStr, String symbol) {

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

    public static String addNegateToString(String currentStr, String symbol, String extraSymbol) {

        String resultStr = "";

        return resultStr;
    }

    public static boolean isOverflow(BigDecimal number) {

        boolean result = false;
        String current = number.toString();
        String[] parts;
        if (current.contains(E)) {
            parts = current.split(E);
            int rate = Math.abs(Integer.parseInt(parts[1]));
            if (rate > MAX_EXPONENT && !parts[0].equals(ZERO)) {
                result = true;
            }
        } else {
            if (number.toBigInteger().toString().length() > 10000) {
                result = true;
            }
        }
        return result;
    }

    public static String convertToString(BigDecimal response, String pattern) {

        String currentPattern = pattern;
        int scale = SCALE;
        String exponentSeparator = "e";
        response = response.setScale(10000, RoundingMode.HALF_UP).stripTrailingZeros();
        String result = response.abs().toString();
        int unscaledLength = response.unscaledValue().toString().length();
        if (result.matches(IS_ZERO_FIRST_REGEX)) {
            if (result.matches(ARE_ZEROS_FIRST_REGEX) && isCorrectExponent(response)) {
                currentPattern = "0." + addDigits(unscaledLength) + E_PART_OF_FORMAT;
                scale += unscaledLength;
            } else {
                ++scale;
                currentPattern += "#";
            }
        } else if (result.contains(E)) {
            String[] parts = result.split(E);
            int expCount = Math.abs(Integer.parseInt(parts[1]));
            response = response.setScale(SCALE + expCount, RoundingMode.HALF_UP).stripTrailingZeros();
            unscaledLength = response.unscaledValue().toString().length();
            if (parts[1].contains("-")) {
                if (expCount > MAX_DECIMAL - unscaledLength) {
                    currentPattern = "0." + addDigits(unscaledLength) + E_PART_OF_FORMAT;
                    scale = scale + expCount;
                    if (unscaledLength == 1) {
                        exponentSeparator = ",e";
                    }
                } else {
                    currentPattern = pattern + "#";
                    ++scale;
                }
            } else if (parts[1].contains("+")) {
                String str = response.abs().toBigInteger().toString();
                int roundLength = str.length();
                if (roundLength > 16) {
                    currentPattern = "0." + addDigits(unscaledLength) + E_PART_OF_FORMAT;
                    scale = scale + expCount;
                    if (unscaledLength < 2) {
                        exponentSeparator = ",e+";
                    }
                } else {
                    currentPattern = pattern + "#";
                    ++scale;
                }
            }

        }
        response = response.setScale(scale, RoundingMode.HALF_UP).stripTrailingZeros();
        unscaledLength = response.unscaledValue().toString().length();
        int count = response.abs().toBigInteger().toString().length();
        if (unscaledLength > 16 && !response.toString().contains(E)) {
            if (result.matches(IS_ZERO_FIRST_REGEX)) {
                currentPattern = FOURTEEN_DECIMAL_PART + E_PART_OF_FORMAT;
            } else if (result.contains(DOT)) {
                currentPattern = preparePattern(count);
            } else {
                currentPattern = FOURTEEN_DECIMAL_PART + E_PART_OF_FORMAT;
                exponentSeparator = "e+";
            }
        }
        DecimalFormat format = new DecimalFormat(currentPattern);
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setExponentSeparator(exponentSeparator);
        format.setDecimalFormatSymbols(symbols);
        result = format.format(response);
        return result;
    }

    private static String addDigits(int count) {

        String result = "";
        for (int i = 1; i < count - 1; i++) {
            result += "0";
        }
        return result;
    }

    private static String preparePattern(int count) {

        return INTEGER_AND_DECIMAL_PART.substring(0, INTEGER_AND_DECIMAL_PART.length() - count);
    }

    private static boolean isCorrectExponent(BigDecimal number) {

        DecimalFormat format = new DecimalFormat(FOURTEEN_DECIMAL_PART + E_PART_OF_FORMAT);
        String[] parts = format.format(number).split(E);
        boolean result = false;
        if (parts.length > 1) {
            int exponent = Math.abs(Integer.parseInt(parts[1]));
            result = exponent > 3 && parts[0].length() + exponent > MAX_ROUND;
        }
        return result;
    }

    public static String addCapacity(String string) {

        String[] parts = {string, ""};
        String minus = "";
        String coma = "";
        if (string.matches(MINUS_REGEX)) {
            minus = "-";
            parts[0] = string.substring(1);
        }
        if (parts[0].contains(COMA)) {
            coma = COMA;
            parts = parts[0].split(COMA);
        }
        parts[0] = String.format("%,d", Long.parseLong(parts[0].replaceAll(" ", "")));
        return minus + parts[0] + coma + parts[1];
    }
}
