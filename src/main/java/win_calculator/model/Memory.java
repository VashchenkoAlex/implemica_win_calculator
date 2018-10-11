package win_calculator.model;

import java.math.BigDecimal;
import java.util.LinkedList;

/**
 * Memory container class
 * Stores list of saved BigDecimal number
 * Provides operation methods for memory operations:
 * store, add, subtract, recall and clear
 */
public class Memory {
    /**
     * List of saved BigDecimal numbers at memory
     */
    private LinkedList<BigDecimal> storedNumbers;

    /**
     * Constructs instance of {@link Memory} with empty list
     */
    Memory(){
        storedNumbers = new LinkedList<>();
    }

    /**
     * Getter for the last stored number
     * @return last BigDecimal stored number
     */
    BigDecimal getStoredNumber(){
        return storedNumbers.getLast();
    }

    /**
     * Adds given BigDecimal number to the last stored number at the list
     * and stores result in the list
     * @param number - given BigDecimal number
     */
    void addToStoredNumber(BigDecimal number){

        if (storedNumbers.isEmpty()){
            storedNumbers.add(BigDecimal.ZERO);
        }
        BigDecimal result = storedNumbers.getLast().add(number);
        storedNumbers.removeLast();
        storedNumbers.add(result);
    }

    /**
     * Subtract given BigDecimal number from the last stored number at the list
     * and stores result in the list
     * @param number - given BigDecimal number
     */
    void subtractFromStoredNumber(BigDecimal number){
        if (storedNumbers.isEmpty()){
            storedNumbers.add(BigDecimal.ZERO);
        }
        BigDecimal result = storedNumbers.getLast().subtract(number);
        storedNumbers.removeLast();
        storedNumbers.add(result);
    }

    /**
     * Stores given BigDecimal number to the memory
     * @param number - given BigDecimal number
     */
    void storeNumber(BigDecimal number){
        storedNumbers.add(number);
    }

    /**
     * Clear list of stored numbers
     */
    public void clear(){
        storedNumbers = new LinkedList<>();
    }
}
