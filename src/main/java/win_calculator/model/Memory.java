package win_calculator.model;

import java.math.BigDecimal;
/**
 * Memory container class
 * Stores list of saved BigDecimal number
 * Provides operation methods for memory operations:
 * store, add, subtract, recall and clear
 */
public class Memory {
   /**
    * Saved BigDecimal number at memory
    */
   private BigDecimal storedNumber;

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
      if (storedNumber == null) {
         storedNumber = BigDecimal.ZERO;
      }
      storedNumber = storedNumber.add(number);
   }

   /**
    * Subtract given BigDecimal number from the stored number at the memory
    *
    * @param number - given BigDecimal number
    */
   void subtractFromStoredNumber(BigDecimal number) {
      if (storedNumber == null) {
         storedNumber = BigDecimal.ZERO;
      }
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

   /**
    * Clear stored number
    */
   public void clear() {
      storedNumber = null;
   }
}























