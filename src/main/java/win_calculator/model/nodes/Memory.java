package win_calculator.model.nodes;

import win_calculator.controller.nodes.digits.Number;

import java.math.BigDecimal;
import java.util.LinkedList;

public class Memory {
    private LinkedList<Number> storedNumbers;

    public Memory(){
        storedNumbers = new LinkedList<>();
    }

    public Number getStoredNumber(){

        return storedNumbers.getLast();
    }

    public void addToStoredNumber(Number number){

        BigDecimal result = storedNumbers.getLast().getBigDecimalValue().add(number.getBigDecimalValue());
        storedNumbers.removeLast();
        storedNumbers.add(new Number(result));
    }

    public void subtractFromStoredNumber(Number number){
        BigDecimal result = storedNumbers.getLast().getBigDecimalValue().subtract(number.getBigDecimalValue());
        storedNumbers.removeLast();
        storedNumbers.add(new Number(result));
    }

    public void storeNumber(Number number){

        storedNumbers.add(number);
    }
}
