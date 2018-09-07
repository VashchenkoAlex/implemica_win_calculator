package win_calculator.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public abstract class StringUtils {

    private static final String COMA = ",";
    private static final String DOT = ".";
    private static final String ZERO = "0";
    private static final String PLUS = "+";
    private static final String MINUS = "-";
    private static final String MINUS_REGEX = "^-";
    private static final String BRACKET = " )";
    private static final String SPACE = "  ";
    private static final String SPACES_REGEX = "\\s\\s";
    private static final String ADD_EXTRA_OPERATION_REGEX = "^.+[\\s{2}][^\\s{2}]+$";
    private static final String ARE_ZEROS_FIRST_REGEX = "^0.0[0-9]+";
    private static final String IS_ZERO_FIRST_REGEX = "^0.+";
    private static final String E_PART_OF_FORMAT = "#E0";
    private static final int SCALE = 16;
    private static final int MAX_ROUND = 16;
    private static final int MAX_DECIMAL = 17;
    private static final int MAX_EXPONENT = 9999;
    private static final String FOURTEEN_DECIMAL_PART = "#.##############";
    private static final String INTEGER_AND_DECIMAL_PART = "0.0000000000000000";
    private static final String E = "E";
    private static final String SIMPLE_E_SEPARATOR = "e";

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

    public static boolean isOverflow(BigDecimal number) {

        boolean result = false;
        String strNumber = number.toString();
        if (strNumber.contains(E)) {
            String[] numberParts = strNumber.split(E);
            if (Math.abs(Integer.parseInt(numberParts[1])) > MAX_EXPONENT && !numberParts[0].equals(ZERO)) {
                result = true;
            }
        } else {
            if (number.toBigInteger().toString().length() > MAX_EXPONENT + 1) {
                result = true;
            }
        }
        return result;
    }

    public static String convertToString(BigDecimal response, String pattern) {

        BigDecimal number = response;
        int scale = selectScale(number);//SCALE;
        number = setNewScale(number, scale);
        String eSeparator = selectSeparator(number);
        String thePattern = selectPattern(number, pattern);
        return initFormatter(thePattern, eSeparator).format(number);
    }

    private static String preparePattern(int count) {

        count = MAX_DECIMAL - count;
        return INTEGER_AND_DECIMAL_PART.substring(0, INTEGER_AND_DECIMAL_PART.length() - count);
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
        return minus + parts[0] + coma + parts[1];
    }

    private static DecimalFormat initFormatter(String pattern, String separator) {

        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setExponentSeparator(separator);
        DecimalFormat formatter = new DecimalFormat(pattern);
        formatter.setDecimalFormatSymbols(symbols);
        return formatter;
    }

    private static int getUnscaledLength(BigDecimal number) {

        int result = number.unscaledValue().toString().length();
        if (result > MAX_ROUND) {
            result -= 1;
        }
        return result;
    }

    private static int getWholeLength(BigDecimal number) {

        return number.abs().toBigInteger().toString().length();
    }

    private static BigDecimal setNewScale(BigDecimal number, int scale) {

        return number.setScale(scale, RoundingMode.HALF_UP).stripTrailingZeros();
    }

    private static boolean containsE(BigDecimal number) {

        return number.toString().contains(E);
    }

    private static String selectSeparator(BigDecimal number) {

        String separator = SIMPLE_E_SEPARATOR;
        int unscaledLength = getUnscaledLength(number);
        String numberStr = number.toString();
        if (unscaledLength > MAX_ROUND) {
            if (!numberStr.contains(DOT)) {
                separator += PLUS;
            }
        } else {
            if (unscaledLength < 2) {
                separator = COMA + separator;
            }
            if (numberStr.contains(PLUS)) {
                separator += PLUS;
            }
        }
        return separator;
    }

    private static String selectPattern(BigDecimal number, String pattern) {

        String currentPattern = pattern;
        int unscaledLength = getUnscaledLength(number);
        String numberStr = number.toString();
        if (containsE(number) && !numberStr.matches(IS_ZERO_FIRST_REGEX)) {
            String[] parts = numberStr.split(E);
            int expCount = Math.abs(Integer.parseInt(parts[1]));
            number = setNewScale(number, SCALE + expCount);
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
                int unscaledLnth = getUnscaledLength(number);
                currentPattern = preparePattern(unscaledLnth) + E_PART_OF_FORMAT;
            }
        }

        if (containsE(number) && getWholeLength(number) > MAX_ROUND) {
            currentPattern = FOURTEEN_DECIMAL_PART + E_PART_OF_FORMAT;
        } else {
            if (unscaledLength > MAX_ROUND) {
                if (numberStr.matches(IS_ZERO_FIRST_REGEX)) {
                    currentPattern = FOURTEEN_DECIMAL_PART + E_PART_OF_FORMAT;
                } else if (numberStr.contains(DOT)) {
                    currentPattern = "#,##" + preparePattern(unscaledLength);
                } else {
                    currentPattern = FOURTEEN_DECIMAL_PART + E_PART_OF_FORMAT;
                }
            }
        }

        return currentPattern;
    }

    private static int selectScale(BigDecimal number){

        int scale = SCALE;
        String result = number.abs().toString();
        if (containsE(number) && !result.matches(IS_ZERO_FIRST_REGEX)) {
            --scale;
            String[] parts = result.split(E);
            scale += Math.abs(Integer.parseInt(parts[1]));
        } else if (result.matches(IS_ZERO_FIRST_REGEX)) {
            if (result.matches(ARE_ZEROS_FIRST_REGEX) && isCorrectExponent(number)) {
                scale += getUnscaledLength(number);
            }
        } else {
            scale -= getWholeLength(number);
        }
        return scale;
    }
}
