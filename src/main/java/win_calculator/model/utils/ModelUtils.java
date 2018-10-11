package win_calculator.model.utils;

import win_calculator.model.exceptions.OperationException;

import java.math.BigDecimal;

import static win_calculator.model.exceptions.ExceptionType.OVERFLOW;

/**
 * Abstract class with utils for checking and rounding BigDecimal numbers
 */
public abstract class ModelUtils {

   /**
    * Constant: module of max BigDecimal value for the {@link win_calculator.model.CalcModel}
    */
   private static final BigDecimal MAX_ABS_VALUE = (new BigDecimal("9.9999999999999995e+9999")).subtract(BigDecimal.valueOf(0.000000000000001));
   /**
    * Constant: module of min BigDecimal value for the {@link win_calculator.model.CalcModel}
    */
   private static final BigDecimal MIN_ABS_VALUE = new BigDecimal("1.e-9999");
   /**
    * Constant: rounding BigDecimal value for the number with incorrect fraction
    */
   private static final BigDecimal ROUND_VALUE = new BigDecimal("1.e-10030");
   /**
    * Constant: double value for the rounding fractional number
    */
   private static final double DOUBLE_ROUND_VALUE = 0.0000000000000001;
   /**
    * Constant: int module for rounding
    */
   private static final int ROUND_MODULE = 4;
   /**
    * Constant: max possible length of visible number
    */
   private static final int MAX_NUMBER_LENGTH = 16;
   /**
    * Constant: max possible exponent for the {@link win_calculator.model.CalcModel}
    */
   private static final int MAX_EXPONENT = 9999;
   /**
    * Constant: String represents of exponent at number
    */
   private static final String EXPONENT = "E";
   /**
    * Constant: String represents of symbol minus
    */
   private static final String MINUS = "-";
   /**
    * Constant: String represents of symbol dot
    */
   private static final String DOT = ".";
   /**
    * Constant: find dot regular expression
    */
   private static final String SPLIT_BY_DOT_REGEX = "[.]";
   /**
    * Constant: find last zero regular expression
    */
   private static final String LAST_ZERO_REGEX = "0+$";

   /**
    * Verifies given BigDecimal number and round it if it's necessary
    *
    * @param number - given BigDecimal number
    * @return BigDecimal result of rounding
    */
   public static BigDecimal roundNumber(BigDecimal number) {

      BigDecimal result = number;
      if (result != null) {
         String string = number.toString();
         String[] parts = {""};
         if (string.contains(EXPONENT)) {
            result = roundNumberWithExponent(number);
         } else if (string.contains(DOT)) {
            parts = string.split(SPLIT_BY_DOT_REGEX);
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

   /**
    * Verifies given BigDecimal number on overflow
    *
    * @param number - given BigDecimal number
    * @throws OperationException if given number is overflow
    */
   public static void checkOnOverflow(BigDecimal number) throws OperationException {

      if (isOverflow(number)) {
         throw new OperationException(OVERFLOW);
      }
   }

   /**
    * Compares given BigDecimal number with max and min values for the model
    *
    * @param number - given BigDecimal number
    * @return true if number is out of valid range
    */
   private static boolean isOverflow(BigDecimal number) {

      BigDecimal numberForCheck = number;
      boolean result = false;
      if (numberForCheck != null) {
         numberForCheck = numberForCheck.abs();
         if (numberForCheck.compareTo(MAX_ABS_VALUE) > 0 || (numberForCheck.compareTo(MIN_ABS_VALUE) < 0 && numberForCheck.compareTo(BigDecimal.ZERO) > 0)) {
            result = true;
         }
      }
      return result;
   }

   /**
    * Verifies given string value of number on large length
    *
    * @param string - given string value of number
    * @return true if length is over the max number length
    */
   private static boolean numberIsLarge(String string) {

      int length = string.replaceAll(LAST_ZERO_REGEX, "").length();
      if (string.contains(MINUS)) {
         --length;
      }
      return length > MAX_NUMBER_LENGTH;
   }

   /**
    * Rounds given whole BigDecimal number
    *
    * @param number - given whole BigDecimal number
    * @return BigDecimal result of rounding
    */
   private static BigDecimal roundWholeNumber(BigDecimal number) {

      String string = number.toBigInteger().toString();
      String minus = "";
      if (number.compareTo(BigDecimal.ZERO) < 0) {
         string = string.substring(1);
         minus = MINUS;
      }
      String[] parts = {string.substring(0, MAX_NUMBER_LENGTH), string.substring(MAX_NUMBER_LENGTH)};
      long shownNumber = Long.parseLong(parts[0]);
      int lastDigit = Integer.parseInt(parts[1].charAt(0) + "");
      if (lastDigit > ROUND_MODULE) {
         parts[0] = shownNumber + 1 + "";
      }
      return new BigDecimal(minus + parts[0] + parts[1]);
   }

   /**
    * Equals given decimal part of number at String with zero
    *
    * @param decimalPart - given decimal part of number at String
    * @return true if decimal part not equals zero
    */
   private static boolean decimalPartNotZero(String decimalPart) {

      boolean result = false;
      if (decimalPart != null) {
         result = new BigDecimal(decimalPart).compareTo(BigDecimal.ZERO) > 0;
      }
      return result;
   }

   /**
    * Verifies is given number at string has to be rounded
    *
    * @param string - given number at string
    * @return boolean result
    */
   private static boolean hasToBeRounded(String string) {

      int length = string.length();
      return Integer.parseInt(string.charAt(length - 1) + "") > ROUND_MODULE;
   }

   /**
    * Rounds given BigDecimal number with exponent
    *
    * @param number - given BigDecimal number
    * @return BigDecimal result of rounding
    */
   private static BigDecimal roundNumberWithExponent(BigDecimal number) {

      String numberStr = number.toString();
      String[] parts = numberStr.split(EXPONENT);
      String minus = "";
      if (parts[0].contains(MINUS)) {
         minus = MINUS;
         parts[0] = parts[0].substring(1);
      }
      if (parts[0].length() > MAX_NUMBER_LENGTH + 1) {
         if (hasToBeRounded(parts[0])) {
            parts[0] = Double.parseDouble(parts[0]) + DOUBLE_ROUND_VALUE + "";
         }
      }
      return new BigDecimal(minus + parts[0] + EXPONENT + parts[1]);
   }
}
