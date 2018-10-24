package win_calculator.controller;

import win_calculator.controller.entities.NumberSymbol;
import win_calculator.controller.entities.Symbol;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.LinkedList;

/**
 * Class builds number from digits and return BigDecimal or String value
 */
public class NumberBuilder {

   /**
    * Constant of max possible digits for enter
    */
   private static final int MAX_DIGITS = 16;
   /**
    * Constant of coma representation at String
    */
   private static final String COMMA = ".";
   /**
    * Constant of zero representation at String
    */
   private static final String ZERO = "0";
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
    * Method add {@link NumberSymbol} to the current numberSymbol's chain
    *
    * @param numberSymbol - current {@link NumberSymbol}
    * @return String of current numberSymbol's chain
    */
   String addDigit(NumberSymbol numberSymbol) {
      if (isNotMaxDigits()) {
         add(numberSymbol);
      }
      return convertNumberToString();
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
      BigDecimal result = number;
      number = null;

      return result;
   }

   /**
    * Method process backspace operation with current number
    *
    * @return String value of current digit's chain
    */
   String doBackSpace() {
      if (!isChainEmpty()) {
         cutLastDigit();
         number = setSign(getBigDecimalNumberFromChain());
      }

      return convertNumberToString();
   }

   /**
    * Converts current digit's chain to the BigDecimal
    *
    * @return BigDecimal converting result
    */
   private BigDecimal getBigDecimalNumberFromChain() {
      return new BigDecimal(buildStringFromChain(digitsChain));
   }

   /**
    * Verifies is current digit's chain empty
    *
    * @return true if current digit's chain empty
    */
   private boolean isChainEmpty() {
      return digitsChain.isEmpty();
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
      if (!wasMemoryOperation) {
         prepareNumber();
      }

      return convertNumberToString();
   }

   /**
    * Method check and return are chains or number not empty
    *
    * @return true if {@link NumberBuilder} contains number
    */
   boolean containsNumber() {
      return !isChainEmpty() || isPreviousChainNotEmpty() || number != null;
   }

   /**
    * Method adds {@link NumberSymbol} from parameter to the number
    *
    * @param numberSymbol - current {@link NumberSymbol}
    */
   private void add(NumberSymbol numberSymbol) {
      String symbol = numberSymbol.getSymbol();
      if (COMMA.equals(symbol)) {
         addComma(numberSymbol);
      } else if (ZERO.equals(symbol)) {
         addZero(numberSymbol);
      } else {
         addDigitToChain(numberSymbol);
      }
      number = getBigDecimalNumberFromChain();
   }

   /**
    * Method sets up sign depends on flag positive
    *
    * @param value - BigDecimal value for setting sign
    * @return changed BigDecimal value
    */
   private BigDecimal setSign(BigDecimal value) {
      BigDecimal changedValue;
      if (!positive && value.compareTo(BigDecimal.ZERO) > 0) {
         changedValue = value.negate();
      } else {
         changedValue = value;
      }

      return changedValue;
   }

   /**
    * Method adds {@link NumberSymbol} to the current digit's chain
    *
    * @param numberSymbol - current {@link NumberSymbol}
    */
   private void addDigitToChain(NumberSymbol numberSymbol) {
      digitsChain.add(numberSymbol);
   }

   /**
    * Method adds comma to the current digit's chain with zero if it's necessary
    *
    * @param comma - {@link NumberSymbol} coma
    */
   private void addComma(NumberSymbol comma) {
      if (isChainEmpty()) {
         digitsChain.add(new NumberSymbol(Symbol.ZERO));
      }

      if (!chainContainsComma(digitsChain)) {
         digitsChain.add(comma);
      }
   }

   /**
    * Method adds zero to the digit's chain if it's possible
    *
    * @param zero - {@link NumberSymbol} zero
    */
   private void addZero(NumberSymbol zero) {
      if (!isChainJustZero()) {
         digitsChain.add(zero);
      }
   }

   /**
    * Verifies is current digit's chain contains just zero
    *
    * @return boolean verification result
    */
   private boolean isChainJustZero() {
      return ZERO.equals(buildStringFromChain(digitsChain));
   }

   /**
    * Method reject last digit from the digit's chain
    * and adds zero if it's necessary
    */
   private void cutLastDigit() {
      digitsChain.removeLast();
      if (digitsChain.isEmpty()) {
         digitsChain.add(new NumberSymbol(Symbol.ZERO));
      }
   }

   /**
    * Method builds String from given digit's chain
    *
    * @param digitsChain - given chain of digits
    * @return String represents of given digit's chain
    */
   private String buildStringFromChain(LinkedList<NumberSymbol> digitsChain) {
      StringBuilder builder = new StringBuilder();
      digitsChain.forEach(numberSymbol -> builder.append(numberSymbol.getSymbol()));

      return builder.toString();
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
      if (!isChainEmpty() && ZERO.equals(digitsChain.get(0).getSymbol())) {
         digitsCount--;
      }

      if (chainContainsComma(digitsChain)) {
         digitsCount--;
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
      if (isChainEmpty() && isPreviousChainNotEmpty()) {
         digitsChain = previousChain;
         number = setSign(getBigDecimalNumberFromChain());
      } else if (!isChainEmpty()) {
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
    * Method converts stored number to String
    * and formats it for display label
    *
    * @return String value of stored number chain
    */
   private String convertNumberToString() {
      LinkedList<NumberSymbol> chain = selectChainForConverting();
      DecimalFormat format = new DecimalFormat(DISPLAY_PATTERN);
      BigDecimal number;
      if (chain != null) {

         if (chainContainsComma(chain)) {
            format.setDecimalSeparatorAlwaysShown(true);
            int minFractionDigits = getMinFractionDigits(chain);
            format.setMinimumFractionDigits(minFractionDigits);
         }

         number = new BigDecimal(buildStringFromChain(chain));
      } else {
         number = this.number;
      }

      String convertedNumber = format.format(number);

      if (!positive) {
         convertedNumber = MINUS_STR + convertedNumber;
      }

      return convertedNumber;
   }

   /**
    * Verifies is given number symbol chain contains separator
    *
    * @param chain - given LinkedList<NumberSymbol> chain
    * @return true if contains
    */
   private boolean chainContainsComma(LinkedList<NumberSymbol> chain) {
      return chain.stream().anyMatch(numberSymbol -> COMMA.equals(numberSymbol.getSymbol()));
   }

   /**
    * Counts digits at fractional part of number at given digit's chain
    *
    * @param chain - given digit's chain
    * @return int count of digits at fractional part
    */
   private int getMinFractionDigits(LinkedList<NumberSymbol> chain) {
      int wholeCounter = 0;
      for (NumberSymbol symbol : chain) {
         wholeCounter++;
         if (COMMA.equals(symbol.getSymbol())) {
            break;
         }
      }
      return chain.size() - wholeCounter;
   }

   /**
    * Verifies digit chains are they empty and return not empty
    *
    * @return selected chain
    */
   private LinkedList<NumberSymbol> selectChainForConverting() {
      LinkedList<NumberSymbol> chain = digitsChain;
      if (isChainEmpty()) {
         chain = previousChain;
      }

      return chain;
   }
}
