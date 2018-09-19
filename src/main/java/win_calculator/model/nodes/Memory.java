package win_calculator.model.nodes;

import java.math.BigDecimal;
import java.util.LinkedList;

public class Memory {
    private LinkedList<BigDecimal> storedNumbers;

    public Memory(){
        storedNumbers = new LinkedList<>();
    }

    public BigDecimal getStoredNumber(){

        return storedNumbers.getLast();
    }

    public void addToStoredNumber(BigDecimal number){

        if (storedNumbers.isEmpty()){
            storedNumbers.add(BigDecimal.ZERO);
        }
        BigDecimal result = storedNumbers.getLast().add(number);
        storedNumbers.removeLast();
        storedNumbers.add(result);
    }

    public void subtractFromStoredNumber(BigDecimal number){
        if (storedNumbers.isEmpty()){
            storedNumbers.add(BigDecimal.ZERO);
        }
        BigDecimal result = storedNumbers.getLast().subtract(number);
        storedNumbers.removeLast();
        storedNumbers.add(result);
    }

    public void storeNumber(BigDecimal number){

        storedNumbers.add(number);
    }

    public void clear(){

        storedNumbers = new LinkedList<>();
    }
}
