package win_calculator.controller.utils;

import win_calculator.model.operations.Operation;
import win_calculator.model.operations.OperationKind;
import win_calculator.model.operations.OperationType;
import win_calculator.model.operations.Number;
import win_calculator.model.operations.extra_operations.ExtraOperation;
import win_calculator.model.operations.main_operations.MainOperation;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.LinkedList;

import static win_calculator.model.operations.OperationKind.*;
import static win_calculator.model.operations.OperationType.*;

/**
 * Abstract class with utils for converting data to String
 * Changing strings for different needs
 */
public abstract class ControllerUtils {

    /**
     * Constants with String representation of symbols
     */
    private static final String COMA = ",";
    private static final String DOT = ".";
    private static final String ZERO_STR = "0";
    private static final String SPACE = " ";
    private static final String PLUS = "+";
    private static final String MINUS = "-";
    private static final String E = "E";

    /**
     * Constants with regexes
     */
    private static final String SPACES_REGEX = "\\s\\s";
    private static final String IS_TEXT_REGEX = "^[^0-9]+";
    private static final String MINUS_REGEX = "^-.[0-9,]*";
    private static final String ARE_ZEROS_FIRST_REGEX = "^0.0[0-9]+";
    private static final String IS_ZERO_FIRST_REGEX = "^0.+";
    private static final String ADD_EXTRA_OPERATION_REGEX = "^.+[\\s{2}][^\\s{2}]+$";
    private static final String NUMBER_REGEX_FOR_STR_FORMAT = "%,d";

    /**
     * Constants for DecimalFormat
     */
    private static final String SIMPLE_E_SEPARATOR = "e";
    private static final String E_PART_OF_FORMAT = "#E0";
    private static final String HISTORY_PATTERN = "################.################";
    private static final String FOURTEEN_DECIMAL_PART = "#.##############";
    private static final String INTEGER_AND_DECIMAL_PART = "0.0000000000000000";

    /**
     * Constants of operation String values at history
     */
    private static final String OPERATION_SEPARATOR = "  ";
    private static final String BRACKET = " )";
    private static final String FRACTION_SYMBOL = "1/( ";
    private static final String NEGATE_SYMBOL = "negate( ";
    private static final String SQR_SYMBOL = "sqr( ";
    private static final String SQRT_SYMBOL = "√( ";
    private static final String ADD_SYMBOL = "  +  ";
    private static final String DIVIDE_SYMBOL = "  \u00F7  ";
    private static final String MULTIPLY_SYMBOL = "  \u00D7  ";
    private static final String SUBTRACT_SYMBOL = "  -  ";

    /**
     * Number length constants
     */
    private static final int MIN_EXPONENT = 3;
    private static final int MAX_NUMBER_LENGTH = 16;
    private static final int MAX_DECIMAL_PART = 17;

    /**
     * Verifies is String value of number contains coma
     * @param number - String value of number for check
     * @return true if given number doesn't contain coma
     */
    public static boolean isComaAbsent(String number) {

        return !number.contains(COMA);
    }

    /**
     * Method replace dot to coma at String value of number
     * @param number - String value of number
     * @return changed value
     */
    public static String replaceDotToComa(String number) {

        return number.replace(DOT, COMA);
    }

    /**
     * Verifies is current string not number
     * @param string - given String
     * @return true if given String is not number
     */
    public static boolean isNotNumber(String string) {

        return string.matches(IS_TEXT_REGEX);
    }

    /**
     * Adds capacity to the given String value of number
     * @param string - the given String value of number
     * @return String value of number with capacity
     */
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
        long number = Long.parseLong(parts[0].replaceAll(SPACE, ""));
        parts[0] = String.format(NUMBER_REGEX_FOR_STR_FORMAT,number);
        String result;
        if (parts.length > 1) {
            result = minus + parts[0] + coma + parts[1];
        } else {
            result = minus + parts[0] + coma;
        }
        return result;
    }

    /**
     *Converts given history of operations at LinkedList to the String
     * @param history - given history of operations at LinkedList
     * @return String of converted history
     */
    public static String convertHistoryToString(LinkedList<Operation> history) {

        String result = "";
        for (Operation operation : history) {
            OperationType type = operation.getType();
            if (EXTRA_OPERATION.equals(type)) {
                String value = selectExtraOperationSymbol((ExtraOperation) operation);
                result = addExtraOperationToString(result, value);
            } else if (NUMBER.equals(type)) {
                result += convertDisplayNumberToString(((Number) operation).getBigDecimalValue(), HISTORY_PATTERN);
            } else if (NEGATE.equals(type)) {
                result = addExtraOperationToString(result, NEGATE_SYMBOL);
            } else if (PERCENT.equals(type)) {
                result += "";
            } else {
                result += selectMainOperationSymbol((MainOperation)operation);
            }
        }
        return result;
    }

    /**
     * Converts given BigDecimal number to String by {@link DecimalFormat}
     * @param givenNumber - given BigDecimal number for converting
     * @param pattern - String pattern for {@link DecimalFormat}
     * @return String of given number
     */
    public static String convertDisplayNumberToString(BigDecimal givenNumber, String pattern) {

        String result;
        if (givenNumber != null) {
            BigDecimal number = givenNumber;
            int scale = selectScale(number);
            number = setNewScale(number, scale);
            number = number.stripTrailingZeros();
            String thePattern = preparePattern(number, pattern);
            result = initFormatter(thePattern, selectSeparator(number)).format(number);
        } else {
            result = ZERO_STR;
        }
        return result;
    }

    /**
     * Selects symbol for given {@link MainOperation}
     * @param mainOperation - given {@link MainOperation}
     * @return symbol for given {@link MainOperation}
     */
    private static String selectMainOperationSymbol(MainOperation mainOperation){

        String symbol = "";
        OperationKind kind = mainOperation.getKind();
        if (ADD.equals(kind)){
            symbol = ADD_SYMBOL;
        } else if (DIVIDE.equals(kind)){
            symbol = DIVIDE_SYMBOL;
        } else if (MULTIPLY.equals(kind)){
            symbol = MULTIPLY_SYMBOL;
        } else if (SUBTRACT.equals(kind)){
            symbol = SUBTRACT_SYMBOL;
        }
        return symbol;
    }

    /**
     * Selects symbol for given {@link ExtraOperation}
     * @param extraOperation - given {@link ExtraOperation}
     * @return symbol for given {@link ExtraOperation}
     */
    private static String selectExtraOperationSymbol(ExtraOperation extraOperation){

        String symbol = "";
        OperationKind kind = extraOperation.getKind();
        if (FRACTION.equals(kind)){
            symbol = FRACTION_SYMBOL;
        } else if (SQR.equals(kind)){
            symbol = SQR_SYMBOL;
        } else if (SQRT.equals(kind)){
            symbol = SQRT_SYMBOL;
        }
        return symbol;
    }

    /**
     * Initializes {@link DecimalFormat}
     * @param pattern - given pattern for {@link DecimalFormat}
     * @param separator - given separator for {@link DecimalFormat}
     * @return initialized instance of {@link DecimalFormat}
     */
    private static DecimalFormat initFormatter(String pattern, String separator) {

        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setExponentSeparator(separator);
        DecimalFormat formatter = new DecimalFormat(pattern);
        formatter.setDecimalFormatSymbols(symbols);
        return formatter;
    }

    /**
     * Selects separator for {@link DecimalFormat} depends on given number
     * @param number - given number
     * @return separator for {@link DecimalFormat}
     */
    private static String selectSeparator(BigDecimal number) {

        String separator = SIMPLE_E_SEPARATOR;
        if (getUnscaledLength(number) == 1) {
            separator = COMA + separator;
        }
        if (number.toString().contains(PLUS)) {
            separator += PLUS;
        }
        return separator;
    }

    /**
     * Prepares String pattern for {@link DecimalFormat} by given number
     * and given String pattern
     * @param number - given BigDecimal number
     * @param pattern - given pattern
     * @return prepared String pattern
     */
    private static String preparePattern(BigDecimal number, String pattern) {

        String currentPattern = pattern;
        String numberStr = number.toString();
        if (containsE(number) && !numberStr.matches(IS_ZERO_FIRST_REGEX)) {
            String[] parts = numberStr.split(E);
            int expCount = Math.abs(Integer.parseInt(parts[1]));
            if (number.toPlainString().contains(DOT)){
                number = setNewScale(number, MAX_NUMBER_LENGTH + expCount);
                number = number.stripTrailingZeros();
            }
            int unscaledLnth = getUnscaledLength(number);
            if (parts[1].contains(MINUS)) {
                if (expCount > MAX_DECIMAL_PART - unscaledLnth) {
                    currentPattern = truncatePattern(unscaledLnth) + E_PART_OF_FORMAT;
                }
            } else if (parts[1].contains(PLUS)) {
                if (getWholeLength(number) > MAX_NUMBER_LENGTH) {
                    currentPattern = truncatePattern(unscaledLnth) + E_PART_OF_FORMAT;
                }
            }
        } else if (numberStr.matches(IS_ZERO_FIRST_REGEX)) {
            if (numberStr.matches(ARE_ZEROS_FIRST_REGEX) && isCorrectExponent(number)) {
                currentPattern = truncatePattern(getUnscaledLength(number)) + E_PART_OF_FORMAT;
            }
        }
        return currentPattern;
    }

    /**
     * Selects correct scale by given number
     * @param number - given BigDecimal number
     * @return correct Integer scale for given number
     */
    private static int selectScale(BigDecimal number) {

        int scale = MAX_NUMBER_LENGTH;
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

    /**
     *Truncates basic pattern for {@link DecimalFormat} depends on
     * given int count
     * @param count - given int count for truncate
     * @return truncated String pattern
     */
    private static String truncatePattern(int count) {

        int index = INTEGER_AND_DECIMAL_PART.length() - MAX_DECIMAL_PART + count;
        return INTEGER_AND_DECIMAL_PART.substring(0,index);
    }

    /**
     * Verifies is given BigDecimal number has correct exponent
     * @param number - given BigDecimal number
     * @return true if given BigDecimal number has correct exponent
     */
    private static boolean isCorrectExponent(BigDecimal number) {

        DecimalFormat formatter = new DecimalFormat(FOURTEEN_DECIMAL_PART + E_PART_OF_FORMAT);
        String[] parts = formatter.format(number).split(E);
        boolean result = false;
        if (parts.length > 1) {
            int exponent = Math.abs(Integer.parseInt(parts[1]));
            result = exponent > MIN_EXPONENT && parts[0].length() + exponent > MAX_NUMBER_LENGTH;
        }
        return result;
    }

    /**
     * Gets an exponent for given BigDecimal number by {@link DecimalFormat}
     * @param number - given BigDecimal number
     * @return int exponent value for given BigDecimal number
     */
    private static int getExponent(BigDecimal number) {

        DecimalFormat formatter = new DecimalFormat(FOURTEEN_DECIMAL_PART + E_PART_OF_FORMAT);
        String[] parts = formatter.format(number).split(E);
        return Math.abs(Integer.parseInt(parts[1]));
    }

    /**
     * Gets unscaled length for given BigDecimal number
     * @param number - given BigDecimal number
     * @return int value of unscaled length for given BigDecimal number
     */
    private static int getUnscaledLength(BigDecimal number) {

        return number.abs().unscaledValue().toString().length();
    }

    /**
     * Gets whole length for given BigDecimal number
     * @param number - given BigDecimal number
     * @return int value of whole length for given BigDecimal number
     */
    private static int getWholeLength(BigDecimal number) {

        return number.abs().toBigInteger().toString().length();
    }

    /**
     * Sets scaling for given BigDecimal number
     * @param number - given BigDecimal number
     * @param scale - given scale
     * @return BigDecimal value of scaled given number
     */
    private static BigDecimal setNewScale(BigDecimal number, int scale) {

        return number.setScale(scale, RoundingMode.HALF_UP);
    }

    /**
     * Verifies is given BigDecimal number contains exponent
     * @param number - given BigDecimal number
     * @return true if given BigDecimal number contains exponent
     */
    private static boolean containsE(BigDecimal number) {

        return number.toString().contains(E);
    }

    /**
     * Adds given symbol of extra operation to the given String of history
     * @param currentStr - given String of history
     * @param symbol - given symbol of extra operation
     * @return given String with symbol
     */
    private static String addExtraOperationToString(String currentStr, String symbol) {

        StringBuilder resultStr = new StringBuilder();
        if (currentStr.contains(OPERATION_SEPARATOR)) {
            if (currentStr.matches(ADD_EXTRA_OPERATION_REGEX)) {
                String[] parts = currentStr.split(SPACES_REGEX);
                for (int i = 0; i < parts.length - 1; i++) {
                    resultStr.append(parts[i]).append(OPERATION_SEPARATOR);
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
