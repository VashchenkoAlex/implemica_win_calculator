package win_calculator.model.utils;

import win_calculator.model.exceptions.OperationException;

import java.math.BigDecimal;

import static win_calculator.model.exceptions.ExceptionType.OVERFLOW;

public abstract class ModelUtils {

    private static final int ROUND_MODULE = 4;
    private static final int MAX_EXPONENT = 9999;
    private static final String E = "E";
    private static final BigDecimal MAX_ABS_VALUE = (new BigDecimal("9.9999999999999995e+9999")).subtract(BigDecimal.valueOf(0.000000000000001));
    private static final BigDecimal MIN_ABS_VALUE = new BigDecimal("1.e-9999");
    private static final BigDecimal ROUND_VALUE = new BigDecimal("1.e-10030");


    public static BigDecimal roundNumber(BigDecimal number) {

        BigDecimal result = number;
        if (result != null) {
            String string = number.toString();
            String[] parts = {""};
            if (string.contains(E)) {
                result = roundNumberWithExponent(number);
            } else if (string.contains(".")) {
                parts = string.split("[.]");
                int decimalLength = 0;
                if (parts.length > 1) {
                    decimalLength = parts[1].length();
                }
                if (decimalLength > MAX_EXPONENT && decimalPartNotZero(parts[1]) && hasToBeRounded(string)) {
                    result = result.add(ROUND_VALUE);
                }
            } else {
                parts[0] = string;
                int wholeLength = parts[0].length();
                if (wholeLength > MAX_EXPONENT && numberIsLarge(parts[0])) {
                    result = roundWholeNumber(result);

                }
            }
        }
        return result;
    }


    private static boolean numberIsLarge(String string) {

        int length = string.replaceAll("0+$", "").length();
        if (string.contains("-")) {
            --length;
        }
        return length > 16;
    }

    private static BigDecimal roundWholeNumber(BigDecimal number) {

        String string = number.toBigInteger().toString();
        String minus = "";
        if (number.compareTo(BigDecimal.ZERO) < 0) {
            string = string.substring(1);
            minus = "-";
        }
        String[] parts = {string.substring(0, 16), string.substring(16)};
        long shownNumber = Long.parseLong(parts[0]);
        int lastDigit = Integer.parseInt(parts[1].charAt(0) + "");
        if (lastDigit > 4) {
            parts[0] = shownNumber + 1 + "";
        }
        return new BigDecimal(minus + parts[0] + parts[1]);
    }

    private static boolean decimalPartNotZero(String decimalPart) {

        boolean result = false;
        if (decimalPart != null) {
            result = new BigDecimal(decimalPart).compareTo(BigDecimal.ZERO) > 0;
        }
        return result;
    }

    private static boolean hasToBeRounded(String string) {

        int length = string.length();
        return Integer.parseInt(string.charAt(length - 1) + "") > ROUND_MODULE;
    }

    private static BigDecimal roundNumberWithExponent(BigDecimal number) {

        String numberStr = number.toString();
        String[] parts = numberStr.split(E);
        String minus = "";
        if (parts[0].contains("-")) {
            minus = "-";
            parts[0] = parts[0].substring(1);
        }
        if (parts[0].length() > 17) {
            if (hasToBeRounded(parts[0])) {
                parts[0] = Double.parseDouble(parts[0]) + 0.0000000000000001 + "";
            }
        }
        return new BigDecimal(minus + parts[0] + E + parts[1]);
    }

    public static void checkOnOverflow(BigDecimal number) throws OperationException {

        if (isOverflow(number)) {
            throw new OperationException(OVERFLOW);
        }
    }

    private static boolean isOverflow(BigDecimal number) {

        BigDecimal numberForCheck = number;
        boolean result = false;
        if (numberForCheck!=null){
            numberForCheck = numberForCheck.abs();
            if (numberForCheck.compareTo(MAX_ABS_VALUE) > 0 || (numberForCheck.compareTo(MIN_ABS_VALUE) < 0 && numberForCheck.compareTo(BigDecimal.ZERO)>0)) {
                result = true;
            }
        }
        return result;
    }

}
