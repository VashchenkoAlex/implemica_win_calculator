package win_calculator.controller;

import win_calculator.controller.entities.NumberSymbol;

import java.math.BigDecimal;
import java.util.LinkedList;

import static win_calculator.controller.utils.ControllerUtils.addCapacity;
import static win_calculator.controller.utils.ControllerUtils.convertNumberToString;
import static win_calculator.controller.utils.ControllerUtils.replaceDotToComa;

/**
 * Class builds number from digits and return BigDecimal or String value
 */
public class NumberBuilder {

   /**
    * Constant of max entered digits
    */
   private static final int MAX_DIGITS = 16;
   /**
    * Constant of coma representation at String
    */
   private static final String COMA = ".";
   /**
    * Constant of zero representation at String
    */
   private static final String ZERO_STR = "0";
   /**
    * Constant of minus representation at String
    */
   private static final String MINUS_STR = "-";
   /**
    * Constant of String pattern for converting BigDecimal number to String for
    * display label
    */
   private static final String DISPLAY_PATTERN = "#############,###.################";


   /**
    * LinkedList of current entered digits chain
    */
   private LinkedList<NumberSymbol> digitsChain = new LinkedList<>();

   /**
    * LinkedList of previous digits chain
    */
   private LinkedList<NumberSymbol> previousChain;

   /**
    * Flag of number sign
    */
   private boolean positive = true;
   /**
    * BigDecimal representation of current number
    */
   private BigDecimal number;


   /**
    * Method resets number, current digit's chain,
    * previous digit's chain and sign
    */
   void clean() {

      number = null;
      digitsChain = new LinkedList<>();
      previousChain = null;
      resetPositive();
   }

   /**
    * Method sets up BigDecimal representation of number
    *
    * @param number - BigDecimal value for number
    */
   public void setNumber(BigDecimal number) {
      this.number = number;
   }

   /**
    * Getter for BigDecimal representation of number
    *
    * @return BigDecimal number
    */
   public BigDecimal getNumber() {

      if (number == null) {
         prepareNumber();
      }
      return number;
   }

   /**
    * Method add {@link NumberSymbol} to the current numberSymbol's chain
    *
    * @param numberSymbol - current {@link NumberSymbol}
    * @return String of current numberSymbol's chain
    */
   String addDigit(NumberSymbol numberSymbol) {

      if (isNotMaxDigits()) {
         add(numberSymbol);
      }
      return convertChainToString();
   }

   /**
    * Method finalizes current number
    * Cleans digit's chain and BigDecimal representation of number
    *
    * @return finalized BigDecimal number
    */
   BigDecimal finish() {

      prepareNumber();
      previousChain = digitsChain;
      digitsChain = new LinkedList<>();
      BigDecimal result;
      if (number != null) {
         result = number;
      } else {
         result = null;
      }
      number = null;
      return result;
   }

   /**
    * Method process backspace operation with current number
    *
    * @return String value of current digit's chain
    */
   String doBackSpace() {

      if (digitsChain.isEmpty() && isPreviousChainNotEmpty()) {
         digitsChain = previousChain;
         cutLastDigit();
         BigDecimal value = new BigDecimal(getValue());
         number = setSign(value);
      } else if (!digitsChain.isEmpty()) {
         cutLastDigit();
         BigDecimal value = new BigDecimal(getValue());
         number = setSign(value);
      }
      return convertChainToString();
   }

   /**
    * Method change sign of current number
    *
    * @param wasMemoryOperation - flag was it memory_operations operation,
    *                           helps select where get number
    * @return String value of current number
    */
   String negate(boolean wasMemoryOperation) {

      changeIsPositive();
      String result;
      if (!wasMemoryOperation) {
         prepareNumber();
         result = convertChainToString();
      } else {
         result = convertNumberToString(number.negate(), DISPLAY_PATTERN);
      }
      return result;
   }

   /**
    * Method check and return are chains or number not empty
    *
    * @return true if {@link NumberBuilder} contains number
    */
   boolean containsNumber() {

      return isChainNotEmpty() || isPreviousChainNotEmpty() || number != null;
   }

   /**
    * Method adds {@link NumberSymbol} from parameter to the number
    *
    * @param numberSymbol - current {@link NumberSymbol}
    */
   private void add(NumberSymbol numberSymbol) {

      if (COMA.equals(numberSymbol.getValue())) {
         addComa(numberSymbol);
      } else if (ZERO_STR.equals(numberSymbol.getValue())) {
         addZero(numberSymbol);
      } else {
         addDigitToChain(numberSymbol);
      }
      number = new BigDecimal(getValue());
   }

   /**
    * Method sets up sign depends on flag positive
    *
    * @param value - BigDecimal value for setting sign
    * @return changed BigDecimal value
    */
   private BigDecimal setSign(BigDecimal value) {

      BigDecimal result = value;
      if (!positive && result.compareTo(BigDecimal.ZERO) > 0) {
         result = result.negate();
      }
      return result;
   }

   /**
    * Method adds {@link NumberSymbol} to the current numberSymbol's chain
    *
    * @param numberSymbol - current {@link NumberSymbol}
    */
   private void addDigitToChain(NumberSymbol numberSymbol) {

      if (ZERO_STR.equals(getValue())) {
         digitsChain.removeLast();
         digitsChain.add(numberSymbol);
      } else {
         digitsChain.add(numberSymbol);
      }
   }

   /**
    * Method adds coma to the current digit's chain with zero if it's necessary
    *
    * @param coma - {@link NumberSymbol} coma
    */
   private void addComa(NumberSymbol coma) {

      if (digitsChain.isEmpty()) {
         digitsChain.add(new NumberSymbol(ZERO_STR));
         digitsChain.add(coma);
      } else if (numberWithoutComa()) {
         digitsChain.add(coma);
      }
   }

   /**
    * Method adds zero to the digit's chain if it's possible
    *
    * @param zero - {@link NumberSymbol} zero
    */
   private void addZero(NumberSymbol zero) {

      if (!ZERO_STR.equals(getValue())) {
         digitsChain.add(zero);
      }
   }

   /**
    * Verifies is digit's chain contains coma
    *
    * @return true if chain without coma
    */
   private boolean numberWithoutComa() {

      boolean result = true;
      for (NumberSymbol numberSymbol : digitsChain) {
         if (COMA.equals(numberSymbol.getValue())) {
            result = false;
         }
      }
      return result;
   }

   /**
    * Method reject last digit from the digit's chain
    * and adds zero if it's necessary
    */
   private void cutLastDigit() {

      if (digitsChain.size() > 1) {
         digitsChain.removeLast();
      } else {
         digitsChain.removeLast();
         digitsChain.add(new NumberSymbol(ZERO_STR));
      }
   }

   /**
    * Method converts current digit's chain to String
    *
    * @return String represents of current digit's chain
    */
   private String getValue() {

      StringBuilder result = new StringBuilder();
      for (NumberSymbol numberSymbol : digitsChain) {
         result.append(numberSymbol.getValue());
      }
      return result.toString();
   }

   /**
    * Verifies is current digit's chain empty
    *
    * @return true if current digit's chain not empty
    */
   private boolean isChainNotEmpty() {

      return !digitsChain.isEmpty();
   }

   /**
    * Verifies is previous digit's chain empty
    *
    * @return true if previous digit's chain not empty
    */
   private boolean isPreviousChainNotEmpty() {

      return previousChain != null && !previousChain.isEmpty();
   }

   /**
    * Method verifies length of current digit's chain
    *
    * @return true if current digit's chain is not full
    */
   private boolean isNotMaxDigits() {

      int digitsCount = digitsChain.size();
      if (!digitsChain.isEmpty() && ZERO_STR.equals(digitsChain.get(0).getValue())) {
         --digitsCount;
      }
      for (NumberSymbol d : digitsChain) {
         if (COMA.equals(d.getValue())) {
            --digitsCount;
         }
      }
      return digitsCount < MAX_DIGITS;
   }

   /**
    * Method change sign flag
    */
   private void changeIsPositive() {
      positive = !positive;
   }

   /**
    * Method reset sign flag
    */
   private void resetPositive() {

      positive = true;
   }

   /**
    * Method verifies flags and sets up BigDecimal number
    */
   private void prepareNumber() {

      if (digitsChain.isEmpty() && isPreviousChainNotEmpty()) {
         digitsChain = previousChain;
         number = setSign(new BigDecimal(getValue()));
      } else if (isChainNotEmpty()) {
         BigDecimal value = setSign(number);
         if (positive && value.compareTo(BigDecimal.ZERO) < 0) {
            value = value.abs();
         }
         number = value;
      } else if (number != null) {
         number = setSign(number);
      }
   }

   /**
    * Method converts current digit's chain to String
    *
    * @return String value of current digit's chain
    */
   private String convertChainToString() {

      StringBuilder resultBuilder = new StringBuilder();
      if (!positive) {
         resultBuilder = new StringBuilder(MINUS_STR);
      }
      LinkedList<NumberSymbol> chain = digitsChain;
      if (digitsChain.isEmpty()) {
         chain = previousChain;
      }
      if (chain != null && !chain.isEmpty()) {
         for (NumberSymbol numberSymbol : chain) {
            resultBuilder.append(numberSymbol.getValue());
         }
      }
      String result = resultBuilder.toString();
      if (!"".equals(result)) {
         result = addCapacity(replaceDotToComa(result));
      }
      return result;
   }
}
