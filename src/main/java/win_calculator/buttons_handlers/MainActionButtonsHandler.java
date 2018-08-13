package win_calculator.buttons_handlers;

import win_calculator.exceptions.MyException;
import win_calculator.main_operations.MainOperation;

import java.math.BigDecimal;

import static win_calculator.Controller.*;
import static win_calculator.StringUtils.optimizeString;

public class MainActionButtonsHandler {

    private BigDecimal firstNumber;
    private BigDecimal secondNumber;
    private BigDecimal resultNumber;
    private MainOperation lastOperation;
    private boolean operationRepeated = false;
    private boolean enterRepeated = false;

    public String doOperation(MainOperation operation, BigDecimal displayedNumber){

        setLastOperation(operation);
        String result = "";
        if (wasOperationBefore() && wasInputtedNumber()){
            secondNumber = displayedNumber;
            try {
                selectWay();
                result = optimizeString(resultNumber.toString());
            } catch (MyException e) {
                result = e.getMessage();
            }
            setOperationRepeated(true);
        }else {
            firstNumber = displayedNumber;
        }
        setEnterRepeated(false);
        setInputtedNumber(false);
        setOperationBefore(true);
        return result;
    }

    public String doEnter(BigDecimal displayedNumber){

        String result;
        if (wasOperationBefore()){
            if (wasInputtedNumber() || isOperationRepeated()){
                secondNumber = displayedNumber;
            }
            try {
                selectWay();
                result = optimizeString(resultNumber.toString());
            } catch (MyException e) {
                result = e.getMessage();
            }
            setEnterRepeated(true);
            setOperationRepeated(false);
            setInputtedNumber(false);
            setOperationBefore(true);
        }else {
            result = optimizeString(displayedNumber.toString());
        }
        return result;
    }

    private void selectWay() throws MyException {

        if (isSecondNumberEmpty()){
            if (isResultEmpty()){
                resultNumber = lastOperation.calculate(firstNumber);
            }else {
                resultNumber = lastOperation.calculate(resultNumber,firstNumber);
            }
        }else {
            if ((operationRepeated||enterRepeated) && !isResultEmpty()){
                resultNumber = lastOperation.calculate(resultNumber,secondNumber);
            }else {
                resultNumber = lastOperation.calculate(firstNumber,secondNumber);
            }
        }
    }

    public void resetValues(){
        firstNumber = null;
        secondNumber = null;
        resultNumber = null;
    }

    private boolean isResultEmpty(){

        return resultNumber==null;
    }

    private boolean isSecondNumberEmpty(){

        return secondNumber==null;
    }

    public void setLastOperation(MainOperation operation){
        lastOperation = operation;
    }

    public boolean isOperationRepeated(){

        return operationRepeated;
    }

    public void setEnterRepeated(boolean val){

        enterRepeated = val;
    }

    public void setOperationRepeated(boolean val){

        operationRepeated = val;
    }

    public BigDecimal getFirstNumber() {
        return firstNumber;
    }

    public void setSecondNumber(BigDecimal secondNumber) {
        this.secondNumber = secondNumber;
    }
}
