package win_calculator.controller.utils;

import win_calculator.model.operations.Operation;
import win_calculator.model.operations.OperationType;
import win_calculator.model.operations.Number;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.LinkedList;

import static win_calculator.model.operations.OperationType.*;

/**
 * Abstract class with utils for converting data to String
 * Changing strings for different needs
 */
public abstract class ControllerUtils {

   /**
    * Constant String representation of coma
    */
   private static final String COMA = ",";
   /**
    * Constant String representation of dot
    */
   private static final String DOT = ".";
   /**
    * Constant String representation of zero
    */
   private static final String ZERO_STR = "0";
   /**
    * Constant String representation of space
    */
   private static final String SPACE = " ";
   /**
    * Constant String representation of plus
    */
   private static final String PLUS = "+";
   /**
    * Constant String representation of minus
    */
   private static final String MINUS = "-";
   /**
    * Constant Exponent symbol
    */
   private static final String E = "E";

   /**
    * Constant: match two spaces regular expression
    */
   private static final String SPACES_REGEX = "\\s\\s";
   /**
    * Constant: match is text regular expression
    */
   private static final String IS_TEXT_REGEX = "^[^0-9]+";
   /**
    * Constant: match first symbol is minus regular expression
    */
   private static final String MINUS_REGEX = "^-.[0-9,]*";
   /**
    * Constant: match first digit is zero regular expression
    */
   private static final String IS_ZERO_FIRST_REGEX = "^0.+";
   /**
    * Constant: match contains extra operation regular expression
    */
   private static final String CONTAINS_EXTRA_OPERATION_REGEX = "^.+[\\s{2}][^\\s{2}]+$";
   /**
    * Constant: number to string format regular expression
    */
   private static final String NUMBER_REGEX_FOR_STR_FORMAT = "%,d";

   /**
    * Constant: exponent symbol for DecimalFormat
    */
   private static final String SIMPLE_E_SEPARATOR = "e";
   /**
    * Constant: exponent part of string pattern for DecimalFormat
    */
   private static final String E_PART_OF_FORMAT = "#E0";
   /**
    * Constant: default DecimalFormat string pattern for history label
    */
   private static final String HISTORY_PATTERN = "################.################";
   /**
    * Constant: DecimalFormat string pattern with 14 possible digits after dot
    */
   private static final String FOURTEEN_DECIMAL_PART = "#.##############";
   /**
    * Constant: DecimalFormat string pattern with 16 required digits after dot
    */
   private static final String INTEGER_AND_DECIMAL_PART = "0.0000000000000000";

   /**
    * Constant: separator between operations at history string
    */
   private static final String OPERATION_SEPARATOR = "  ";
   /**
    * Constant: closing bracket symbol of extra operation at history string
    */
   private static final String BRACKET = " )";
   /**
    * Constant: fraction operation symbol at history string
    */
   private static final String FRACTION_SYMBOL = "1/( ";
   /**
    * Constant: negate operation symbol at history string
    */
   private static final String NEGATE_SYMBOL = "negate( ";
   /**
    * Constant: square operation symbol at history string
    */
   private static final String SQR_SYMBOL = "sqr( ";
   /**
    * Constant: square root operation symbol at history string
    */
   private static final String SQRT_SYMBOL = "√( ";
   /**
    * Constant: add operation symbol at history string
    */
   private static final String ADD_SYMBOL = "  +  ";
   /**
    * Constant: divide operation symbol at history string
    */
   private static final String DIVIDE_SYMBOL = "  \u00F7  ";
   /**
    * Constant: multiply operation symbol at history string
    */
   private static final String MULTIPLY_SYMBOL = "  \u00D7  ";
   /**
    * Constant: subtract operation symbol at history string
    */
   private static final String SUBTRACT_SYMBOL = "  -  ";

   /**
    * Constant: minimal number length
    */
   private static final int MIN_EXPONENT = 3;
   /**
    * Constant: binary base for unscaled length getting
    */
   private static final int BINARY_BASE = 2;
   /**
    * Constant: decimal base for unscaled length getting
    */
   private static final int DECIMAL_BASE = 10;
   /**
    * Constant: max number length
    */
   private static final int MAX_VISIBLE_NUMBER_LENGTH = 16;
   /**
    * Constant: max decimal part length
    */
   private static final int MAX_DECIMAL_PART = 17;

   private static final BigDecimal MAX_NUMBER_WITHOUT_EXPONENT = new BigDecimal("9999999999999999");

   /**
    * Verifies is String value of number contains coma
    *
    * @param string - String value of number for check
    * @return true if given number doesn't contain coma
    */
   public static boolean isComaAbsent(String string) {
      return !string.contains(COMA);
   }

   /**
    * Verifies is current string not number
    *
    * @param string - given String
    * @return true if given String is not number
    */
   public static boolean isNotNumber(String string) {
      return string.matches(IS_TEXT_REGEX); // TODO: 22.10.2018 Replace with Pattern
   }

   /**
    * Adds capacity to the given String value of number
    *
    * @param string - the given String value of number
    * @return String value of number with capacity
    */
   public static String addCapacity(String string) {
      // TODO: 22.10.2018  переписать на NumberFormat

      String[] parts = {string, ""};
      String minus;

      if (string.matches(MINUS_REGEX)) { // TODO: 22.10.2018 Replace with Pattern
         minus = MINUS;
         parts[0] = string.substring(1);
      } else {
         minus = "";
      }

      String coma;

      if (parts[0].contains(COMA)) {
         coma = COMA;
         parts = parts[0].split(COMA);
      } else {
         coma = "";
      }

      long number = Long.parseLong(parts[0].replaceAll(SPACE, ""));
      parts[0] = String.format(NUMBER_REGEX_FOR_STR_FORMAT, number);
      String stringWithCapacity;

      if (parts.length > 1) {
         stringWithCapacity = minus + parts[0] + coma + parts[1];
      } else {
         stringWithCapacity = minus + parts[0] + coma;
      }

      return stringWithCapacity;
   }

   /**
    * Converts given history of operations at LinkedList to the String
    *
    * @param history - given history of operations at LinkedList
    * @return String of converted history
    */
   public static String convertHistoryToString(LinkedList<Operation> history) {

      StringBuilder builder = new StringBuilder();
      history.forEach(operation -> {
         OperationType type = operation.getType();
         String symbol = selectOperationSymbol(type);
         if (isExtraOperation(type)) {
            String result = addExtraOperationToString(builder.toString(), symbol);
            builder.delete(0, builder.length());
            builder.append(result);
         } else if (NUMBER == type) {
            builder.append(convertNumberToString(((Number) operation).getValue(), HISTORY_PATTERN));
         } else if (PERCENT != type) {
            builder.append(symbol);
         }
      });

      return builder.toString();
   }

   /**
    * Converts given BigDecimal number to String by {@link DecimalFormat}
    *
    * @param givenNumber - given BigDecimal number for converting
    * @param pattern     - String pattern for {@link DecimalFormat}
    * @return String of given number
    */
   public static String convertNumberToString(BigDecimal givenNumber, String pattern) { // переписать

      String stringRepresentation;

      if (givenNumber != null) {
         BigDecimal number = givenNumber;
         int scale = selectScale(number);

         number = setNewScale(number, scale);
         number = number.stripTrailingZeros();

         String thePattern = preparePattern(number, pattern);
         DecimalFormat formatter = initFormatter(thePattern, selectSeparator(number));
         stringRepresentation = formatter.format(number);
      } else {
         stringRepresentation = ZERO_STR;
      }

      return stringRepresentation;
   }

   /**
    * Selects symbol by given {@link OperationType}
    *
    * @param type - given {@link OperationType}
    * @return symbol by given {@link OperationType}
    */
   private static String selectOperationSymbol(OperationType type) {

      String symbol; // записать в map

      if (FRACTION == type) {
         symbol = FRACTION_SYMBOL;

      } else if (DIVIDE == type) {
         symbol = DIVIDE_SYMBOL;

      } else if (NEGATE == type) {
         symbol = NEGATE_SYMBOL;

      } else if (SQR == type) {
         symbol = SQR_SYMBOL;

      } else if (MULTIPLY == type) {
         symbol = MULTIPLY_SYMBOL;

      } else if (SQRT == type) {
         symbol = SQRT_SYMBOL;

      } else if (ADD == type) {
         symbol = ADD_SYMBOL;

      } else if (SUBTRACT == type) {
         symbol = SUBTRACT_SYMBOL;

      } else {
         symbol = "";

      }

      return symbol;
   }

   /**
    * Initializes {@link DecimalFormat}
    *
    * @param pattern   - given pattern for {@link DecimalFormat}
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
    *
    * @param number - given number
    * @return separator for {@link DecimalFormat}
    */
   private static String selectSeparator(BigDecimal number) {

      String separator;

      if (getUnscaledLength(number) == 1) {
         separator = COMA + SIMPLE_E_SEPARATOR;
      } else {
         separator = SIMPLE_E_SEPARATOR;
      }

      if (number.abs().compareTo(MAX_NUMBER_WITHOUT_EXPONENT) > 0) {
         separator += PLUS;
      }

      return separator;
   }

   /**
    * Prepares String pattern for {@link DecimalFormat} by given number
    * and given String pattern
    *
    * @param number  - given BigDecimal number
    * @param pattern - given pattern
    * @return prepared String pattern
    */
   private static String preparePattern(BigDecimal number, String pattern) { //...

      String currentPattern = pattern;
      number = optimizeDecimalNumber(number);

      if (exponentChangesPattern(number)) {
         currentPattern = truncatePattern(getUnscaledLength(number)) + E_PART_OF_FORMAT;
      }

      return currentPattern;
   }

   /**
    * Verifies is exponent of given BigDecimal number changes format pattern
    *
    * @param number - given BigDecimal number
    * @return boolean verification result
    */
   private static boolean exponentChangesPattern(BigDecimal number) { //...

      String numberString = number.toString();
      boolean isChanging;

      if (isZeroFirst(numberString)) {
         isChanging = correctExponent(number);
      } else {
         isChanging = validExponent(number);
      }

      return isChanging;
   }

   private static boolean isZeroFirst(String numberString) { // принимать номер
      return numberString.matches(IS_ZERO_FIRST_REGEX);
   }

   /**
    * Sets new scaling and strips trailing zeros for given BigDecimal number
    *
    * @param number - given BigDecimal number
    * @return BigDecimal result of optimization
    */
   private static BigDecimal optimizeDecimalNumber(BigDecimal number) {

      BigDecimal optimizedNumber = number;

      if (isDecimal(number)) {
         int newScale = MAX_VISIBLE_NUMBER_LENGTH + getExponent(optimizedNumber);
         optimizedNumber = setNewScale(number, newScale);
         optimizedNumber = optimizedNumber.stripTrailingZeros();
      }

      return optimizedNumber;
   }

   /**
    * Verifies is given number decimal
    *
    * @param number given BigDecimal number
    * @return boolean verification result
    */
   private static boolean isDecimal(BigDecimal number) { // ...
      return number.toPlainString().contains(DOT);
   }

   /**
    * Selects correct scale by given number
    *
    * @param number - given BigDecimal number
    * @return correct Integer scale for given number
    */
   private static int selectScale(BigDecimal number) { //...

      int scale;
      String selectedScale = number.abs().toString();

      if (containsE(number) && !isZeroFirst(selectedScale)) {
         scale = MAX_VISIBLE_NUMBER_LENGTH - 1;
         String[] parts = selectedScale.split(E);
         scale += getAbsInt(parts[1]);
      } else if (isZeroFirst(selectedScale)) {

         if (correctExponent(number)) {
            scale = MAX_VISIBLE_NUMBER_LENGTH + getExponent(number) - 1;
         } else {
            scale = MAX_VISIBLE_NUMBER_LENGTH;
         }

      } else {
         scale = MAX_VISIBLE_NUMBER_LENGTH - getWholeLength(number);
      }

      return scale;
   }

   /**
    * Converts given string to the absolute integer value
    *
    * @param str - given string
    * @return converted integer result
    */
   private static int getAbsInt(String str) { //...
      return Math.abs(Integer.parseInt(str));
   }

   /**
    * Truncates basic pattern for {@link DecimalFormat} depends on
    * given int count
    *
    * @param count - given int count for truncate
    * @return truncated String pattern
    */
   private static String truncatePattern(int count) { //...

      int index = INTEGER_AND_DECIMAL_PART.length() - MAX_DECIMAL_PART + count;

      return INTEGER_AND_DECIMAL_PART.substring(0, index);
   }

   /**
    * Verifies is given BigDecimal number has exponent in range between
    * 4 and 9999
    *
    * @param number - given BigDecimal number
    * @return true if given BigDecimal number has correct exponent
    */
   private static boolean correctExponent(BigDecimal number) { // оптимизировать

      DecimalFormat formatter = new DecimalFormat(FOURTEEN_DECIMAL_PART + E_PART_OF_FORMAT);
      String[] parts = formatter.format(number).split(E);
      boolean exponentInRange;

      if (parts.length > 1) {
         int exponent = getAbsInt(parts[1]);
         boolean isLessMinExponent = exponent > MIN_EXPONENT;
         boolean isNumberLengthLargerMaxVisibleLength = parts[0].length() + exponent > MAX_VISIBLE_NUMBER_LENGTH;
         exponentInRange = isLessMinExponent && isNumberLengthLargerMaxVisibleLength;
      } else {
         exponentInRange = false;
      }

      return exponentInRange;
   }

   /**
    * Verifies is given BigDecimal number has valid exponent
    *
    * @param number - given BigDecimal number
    * @return boolean verification result
    */
   private static boolean validExponent(BigDecimal number) { //...

      String[] parts = number.toString().split(E);
      boolean isValid;

      if (parts.length > 1) {
         int expCount = Integer.parseInt(parts[1]);
         int unscaledLength = getUnscaledLength(number);
         boolean validNegativeExponent = expCount < 0 && (Math.abs(expCount) > MAX_DECIMAL_PART - unscaledLength);
         boolean validPositiveExponent = expCount > 0 && getWholeLength(number) > MAX_VISIBLE_NUMBER_LENGTH;
         isValid = validNegativeExponent || validPositiveExponent;
      } else {
         isValid = false;
      }

      return isValid;
   }

   /**
    * Gets an exponent for given BigDecimal number by {@link DecimalFormat}
    *
    * @param number - given BigDecimal number
    * @return int exponent value for given BigDecimal number
    */
   private static int getExponent(BigDecimal number) {

      DecimalFormat formatter = new DecimalFormat(FOURTEEN_DECIMAL_PART + E_PART_OF_FORMAT);
      String[] parts = formatter.format(number).split(E);

      return getAbsInt(parts[1]);
   }

   /**
    * Gets unscaled length for given BigDecimal number
    *
    * @param number - given BigDecimal number
    * @return int value of unscaled length for given BigDecimal number
    * was taken from https://stackoverflow.com/a/23773083
    */
   private static int getUnscaledLength(BigDecimal number) {

      BigInteger unscaledValue = number.abs().unscaledValue();
      double factor = Math.log(BINARY_BASE) / Math.log(DECIMAL_BASE);
      int length = (int) (factor * unscaledValue.bitLength() + 1);

      if (BigInteger.TEN.pow(length - 1).compareTo(unscaledValue) > 0) {
         length = length - 1;
      }

      return length;
   }

   /**
    * Gets whole length for given BigDecimal number
    *
    * @param number - given BigDecimal number
    * @return int value of whole length for given BigDecimal number
    */
   private static int getWholeLength(BigDecimal number) { //...

      return number.abs().toBigInteger().toString().length();
   }

   /**
    * Sets scaling for given BigDecimal number
    *
    * @param number - given BigDecimal number
    * @param scale  - given scale
    * @return BigDecimal value of scaled given number
    */
   private static BigDecimal setNewScale(BigDecimal number, int scale) {

      return number.setScale(scale, RoundingMode.HALF_UP);
   }

   /**
    * Verifies is given BigDecimal number contains exponent
    *
    * @param number - given BigDecimal number
    * @return true if given BigDecimal number contains exponent
    */
   private static boolean containsE(BigDecimal number) { //...
      return number.toString().contains(E);
   }

   /**
    * Adds given symbol of extra operation to the given String of history
    *
    * @param currentStr - given String of history
    * @param symbol     - given symbol of extra operation
    * @return given String with symbol
    */
   private static String addExtraOperationToString(String currentStr, String symbol) {
      String resultStr;
      if (currentStr.contains(OPERATION_SEPARATOR)) {

         if (currentStr.matches(CONTAINS_EXTRA_OPERATION_REGEX)) { // TODO: 22.10.2018 replace with Pattern
            resultStr = redistributeOperations(currentStr, symbol);
         } else {
            resultStr = currentStr + symbol;
         }

      } else if (!currentStr.isEmpty()) {
         resultStr = symbol + currentStr;
      } else {
         resultStr = "";
      }

      return resultStr + BRACKET;
   }

   /**
    * Redistributes operations with separators at given string with given symbol
    *
    * @param string - given string
    * @param symbol - given symbol
    * @return - redistributed string
    */
   private static String redistributeOperations(String string, String symbol) {

      String[] parts = string.split(SPACES_REGEX);
      StringBuilder redistributedString = new StringBuilder();

      for (int i = 0; i < parts.length - 1; i++) {
         redistributedString.append(parts[i]).append(OPERATION_SEPARATOR); // отдельно просмотреть
      }

      return redistributedString + symbol + parts[parts.length - 1];
   }

   /**
    * Verifies is given {@link OperationType} extra operation
    * (negate, sqrt, sqr, fraction)
    *
    * @param type - given operation type
    * @return boolean result
    */
   private static boolean isExtraOperation(OperationType type) {

      return type == SQR || type == SQRT || type == FRACTION || NEGATE == type;
   }
}
