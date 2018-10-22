package win_calculator.model.utils;

import win_calculator.model.exceptions.OperationException;
import win_calculator.model.operations.OperationType;

import java.math.BigDecimal;

import static win_calculator.model.exceptions.ExceptionType.OVERFLOW;
import static win_calculator.model.operations.OperationType.*;

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
   public static BigDecimal roundNumber(BigDecimal number) { //...

      BigDecimal roundedNumber = number;
      if (roundedNumber != null) {

         String string = number.toString();
         if (string.contains(EXPONENT)) {
            roundedNumber = roundNumberWithExponent(number);
         } else if (string.contains(DOT)) {
            roundedNumber = roundDecimalNumber(roundedNumber);
         } else if (numberIsLarge(string)) {
            roundedNumber = roundWholeNumber(roundedNumber);

         }

      }

      return roundedNumber;
   }

   private static BigDecimal roundDecimalNumber(BigDecimal number) { //...

      BigDecimal roundedNumber = number;
      String string = roundedNumber.toString();
      String[] parts = string.split(SPLIT_BY_DOT_REGEX);
      int decimalLength;

      if (parts.length > 1) {
         decimalLength = parts[1].length();
      }else {
         decimalLength = 0;
      }

      if (decimalLength > MAX_EXPONENT && decimalPartNotZero(parts[1]) && hasToBeRounded(string)) {
         roundedNumber = roundedNumber.add(ROUND_VALUE);
      }

      return roundedNumber;
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

         if (isOverflowNumber(numberForCheck)) {
            result = true;
         }
         
      }

      return result;
   }

   /**
    * Verifies is given BigDecimal number overflow
    * @param number - given BigDecimal number
    * @return boolean verification result
    */
   private static boolean isOverflowNumber(BigDecimal number) {
      
      boolean isBiggerMax = number.compareTo(MAX_ABS_VALUE) > 0;

      boolean isSmallerMin = number.compareTo(MIN_ABS_VALUE) < 0 && number.compareTo(BigDecimal.ZERO) > 0;

      return isBiggerMax || isSmallerMin;
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
         length--;
      }
      
      return length > MAX_NUMBER_LENGTH && string.length() > MAX_EXPONENT;
   }

   /**
    * Rounds given whole BigDecimal number
    *
    * @param number - given whole BigDecimal number
    * @return BigDecimal result of rounding
    */
   private static BigDecimal roundWholeNumber(BigDecimal number) { //...

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
   private static boolean decimalPartNotZero(String decimalPart) { //...

      boolean isNotZero;
      
      if (decimalPart != null) {
         isNotZero = new BigDecimal(decimalPart).compareTo(BigDecimal.ZERO) > 0;
      } else {
         isNotZero = false;
      }
      
      return isNotZero;
   }

   /**
    * Verifies is given number at string has to be rounded
    *
    * @param string - given number at string
    * @return boolean result
    */
   private static boolean hasToBeRounded(String string) { //...

      int length = string.length() - 1;
      int exponent = Integer.parseInt(string.charAt(length) + "");
      
      return exponent > ROUND_MODULE;
   }

   /**
    * Rounds given BigDecimal number with exponent
    *
    * @param number - given BigDecimal number
    * @return BigDecimal result of rounding
    */
   private static BigDecimal roundNumberWithExponent(BigDecimal number) { //...

      String numberStr = number.toString();
      String[] parts = numberStr.split(EXPONENT);
      String realPart = parts[0];
      String minus = "";
      
      if (realPart.contains(MINUS)) {
         minus = MINUS;
         realPart = realPart.substring(1);
      }
      
      if (realPart.length() > MAX_NUMBER_LENGTH + 1 && hasToBeRounded(realPart)) {
            realPart = Double.parseDouble(realPart) + DOUBLE_ROUND_VALUE + "";
      }
      
      return new BigDecimal(minus + realPart + EXPONENT + parts[1]);
   }

   /**
    * Verifies is given {@link OperationType} binary operation
    * (add, divide, subtract, multiply)
    *
    * @param type - given operation type
    * @return boolean result
    */
   public static boolean isBinaryOperation(OperationType type) {

      return type == ADD || type == SUBTRACT || type == DIVIDE || type == MULTIPLY;
   }

   /**
    * Verifies is given {@link OperationType} extra operation
    * (sqrt, sqr, fraction)
    *
    * @param type - given operation type
    * @return boolean result
    */
   public static boolean isExtraOperation(OperationType type) {

      return type == SQR || type == SQRT || type == FRACTION;
   }
}
