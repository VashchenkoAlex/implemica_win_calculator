package win_calculator.model;

import java.math.BigDecimal;
/**
 * Memory container class
 * Stores BigDecimal number
 * Provides operation methods for memory operations:
 * store, add, subtract and recall
 */
class Memory {
   /**
    * Saved BigDecimal number at memory
    */
   private BigDecimal storedNumber = BigDecimal.ZERO;

   /**
    * Getter for the stored number
    *
    * @return BigDecimal stored number
    */
   BigDecimal getStoredNumber() {
      return storedNumber;
   }

   /**
    * Adds given BigDecimal number to the stored number at the memory
    *
    * @param number - given BigDecimal number
    */
   void addToStoredNumber(BigDecimal number) {
      storedNumber = storedNumber.add(number);
   }

   /**
    * Subtract given BigDecimal number from the stored number at the memory
    *
    * @param number - given BigDecimal number
    */
   void subtractFromStoredNumber(BigDecimal number) {
      storedNumber = storedNumber.subtract(number);
   }

   /**
    * Stores given BigDecimal number to the memory
    *
    * @param number - given BigDecimal number
    */
   void storeNumber(BigDecimal number) {
      storedNumber = number;
   }

}























