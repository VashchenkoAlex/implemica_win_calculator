package win_calculator.controller.utils;

import win_calculator.model.operations.Operation;
import win_calculator.model.operations.OperationType;
import win_calculator.model.operations.Number;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.regex.Pattern;

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
    * Constant String representation of zero
    */
   private static final String ZERO_STR = "0";
   /**
    * Constant String representation of plus
    */
   private static final String PLUS = "+";

   /**
    * Constant: match contains extra operation pattern
    */
   private static final Pattern IS_TEXT_PATTERN = Pattern.compile("^[^0-9]+");
   /**
    * Constant: match contains extra operation pattern
    */
   private static final Pattern CONTAINS_EXTRA_OPERATION_PATTERN = Pattern.compile("^.+[\\s{2}][^\\s{2}]+$");
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
    * Constant: map with symbols for operation used in history label
    */
   private static final HashMap<OperationType, String> operationSymbols = initSymbolsMap();
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
    * Constant: minimal exponent
    */
   private static final int MIN_EXPONENT = 3;
   /**
    * Constant: max number length
    */
   private static final int MAX_VISIBLE_NUMBER_LENGTH = 16;
   /**
    * Constant: max fractional part length
    */
   private static final int MAX_FRACTIONAL_PART = 17;
   /**
    * Constant: maximum possible BigDecimal number without exponent representation
    */
   private static final BigDecimal MAX_NUMBER_WITHOUT_EXPONENT = BigDecimal.valueOf(9999999999999999L);
   /**
    * Constant: minimum possible BigDecimal number without exponent representation
    */
   private static final BigDecimal MIN_NUMBER_WITHOUT_EXPONENT = BigDecimal.valueOf(0.0000000000000001);

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
   //fixed
   public static boolean isNotNumber(String string) {
      return IS_TEXT_PATTERN.matcher(string).matches();
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
   //fixed
   public static String convertNumberToString(BigDecimal givenNumber, String pattern) {
      String stringRepresentation;
      if (givenNumber != null) {
         BigDecimal number = optimizeScale(givenNumber).stripTrailingZeros();
         String currentPattern = preparePattern(number, pattern);
         DecimalFormat formatter = initFormatter(currentPattern, selectSeparator(number));
         stringRepresentation = formatter.format(number);
      } else {
         stringRepresentation = ZERO_STR;
      }

      return stringRepresentation;
   }

   /**
    * Initializes mapping for operation symbols used in history label text
    *
    * @return initialized map
    */
   private static HashMap<OperationType, String> initSymbolsMap() {
      HashMap<OperationType, String> symbols = new HashMap<>();
      symbols.put(FRACTION, FRACTION_SYMBOL);
      symbols.put(DIVIDE, DIVIDE_SYMBOL);
      symbols.put(NEGATE, NEGATE_SYMBOL);
      symbols.put(SQR, SQR_SYMBOL);
      symbols.put(SQRT, SQRT_SYMBOL);
      symbols.put(MULTIPLY, MULTIPLY_SYMBOL);
      symbols.put(ADD, ADD_SYMBOL);
      symbols.put(SUBTRACT, SUBTRACT_SYMBOL);

      return symbols;
   }

   /**
    * Selects symbol by given {@link OperationType}
    *
    * @param type - given {@link OperationType}
    * @return symbol by given {@link OperationType}
    */
   private static String selectOperationSymbol(OperationType type) {
      return operationSymbols.get(type);
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
   //fixed
   private static String selectSeparator(BigDecimal number) {
      String separator;
      if (number.precision() == 1) {
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
   // fixed
   private static String preparePattern(BigDecimal number, String pattern) {
      String currentPattern;
      if (isPatternHasToBeChanged(number)) {
         currentPattern = truncatePattern(number) + E_PART_OF_FORMAT;
      } else {
         currentPattern = pattern;
      }

      return currentPattern;
   }

   /**
    * Verifies is default pattern has to be changed that used in DecimalFormat
    * for given BigDecimal number
    *
    * @param number - given BigDecimal number
    * @return boolean verification result
    */
   // fixed
   private static boolean isPatternHasToBeChanged(BigDecimal number) {
      boolean isNumberBigger = number.abs().compareTo(MAX_NUMBER_WITHOUT_EXPONENT) > 0;
      BigDecimal borderValue = MIN_NUMBER_WITHOUT_EXPONENT.movePointRight(number.precision() - 1);
      boolean isNumberSmaller = isNumberFractional(number) && (number.abs().compareTo(borderValue) < 0);

      return isNumberBigger || isNumberSmaller;
   }

   /**
    * Verifies is given BigDecimal number fractional
    *
    * @param number - given BigDecimal number
    * @return boolean verification result
    */
   //fixed
   private static boolean isNumberFractional(BigDecimal number) {
      BigDecimal numberAbs = number.abs();

      return (numberAbs.compareTo(BigDecimal.ONE) < 1) &&
              (numberAbs.compareTo(BigDecimal.ZERO) > 0);
   }

   /**
    * Selects correct scale by given number
    *
    * @param number - given BigDecimal number
    * @return correct Integer scale for given number
    */
   // fixed
   private static int selectScale(BigDecimal number) {
      int scale;
      if (isNumberFractional(number)) {

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
    * Truncates basic pattern for {@link DecimalFormat} depends on
    * given BigDecimal number
    *
    * @param number - given BigDecimal number
    * @return truncated String pattern
    */
   // fixed
   private static String truncatePattern(BigDecimal number) {
      int precision = number.precision();
      int index = INTEGER_AND_DECIMAL_PART.length() - MAX_FRACTIONAL_PART + precision;

      return INTEGER_AND_DECIMAL_PART.substring(0, index);
   }

   /**
    * Verifies is given BigDecimal number has exponent in range between
    * 4 and 9999
    *
    * @param number - given BigDecimal number
    * @return true if given BigDecimal number has correct exponent
    */
   // fixed
   private static boolean correctExponent(BigDecimal number) {
      int exponent = getExponent(number);
      boolean isBiggerMinExponent = exponent > MIN_EXPONENT;
      boolean isNumberLarge = number.precision() + exponent > MAX_VISIBLE_NUMBER_LENGTH;

      return isBiggerMinExponent && isNumberLarge;
   }

   /**
    * Gets an exponent for given BigDecimal number by {@link DecimalFormat}
    *
    * @param number - given BigDecimal number
    * @return int exponent value for given BigDecimal number
    */
   //fixed
   private static int getExponent(BigDecimal number) {
      return Math.abs(number.precision() - number.scale() - 1);
   }

   /**
    * Gets whole length for given BigDecimal number
    *
    * @param givenNumber - given BigDecimal number
    * @return int value of whole length for given BigDecimal number
    * <p>
    * was taken from https://stackoverflow.com/a/23773083
    */
   //fixed
   private static int getWholeLength(BigDecimal givenNumber) {
      BigInteger number = givenNumber.abs().toBigInteger();
      double factor = Math.log(2) / Math.log(10);
      int digitCount = (int) (factor * number.bitLength() + 1);

      if (BigInteger.TEN.pow(digitCount - 1).compareTo(number) > 0) {
         digitCount--;
      }

      return digitCount;
   }

   /**
    * Optimizes scaling for given BigDecimal number
    *
    * @param number - given BigDecimal number
    * @return BigDecimal value of scaled given number
    */
   private static BigDecimal optimizeScale(BigDecimal number) {
      return number.setScale(selectScale(number), RoundingMode.HALF_UP);
   }

   /**
    * Adds given symbol of extra operation to the given String of history
    *
    * @param currentStr - given String of history
    * @param symbol     - given symbol of extra operation
    * @return given String with symbol
    */
   // fixed
   private static String addExtraOperationToString(String currentStr, String symbol) {
      String resultStr;
      if (currentStr.contains(OPERATION_SEPARATOR)) {

         if (CONTAINS_EXTRA_OPERATION_PATTERN.matcher(currentStr).matches()) {
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
   //fixed
   private static String redistributeOperations(String string, String symbol) {
      int lastSeparatorIndex = string.lastIndexOf("  ");
      String beginPart = string.substring(0, lastSeparatorIndex);
      String endPart = string.substring(lastSeparatorIndex + OPERATION_SEPARATOR.length());

      return beginPart + OPERATION_SEPARATOR + symbol + endPart;
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
