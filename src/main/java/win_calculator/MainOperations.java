package win_calculator;

import win_calculator.exceptions.MyException;
import win_calculator.main_operations.MainOperation;

import java.math.BigDecimal;

import static win_calculator.StringUtils.*;

abstract class MainOperations {

    private static BigDecimal firstNumber;
    private static BigDecimal secondNumber;
    private static BigDecimal resultNumber;
    private static MainOperation lastOperation;

    static void calculate(boolean isRepeated) throws MyException {

        if (isSecondNumberEmpty()){
            if (isResultEmpty()){
                resultNumber = lastOperation.calculate(firstNumber);
            }else {
                resultNumber = lastOperation.calculate(resultNumber,firstNumber);
            }
        }else {
            if (isRepeated&&!isResultEmpty()){
                resultNumber = lastOperation.calculate(resultNumber,secondNumber);
            }else {
                resultNumber = lastOperation.calculate(firstNumber,secondNumber);
            }
        }
    }

    static BigDecimal getResult(){

        return resultNumber;
    }

    static void resetValues(){
        firstNumber = null;
        secondNumber = null;
        resultNumber = null;
    }

    static void setFirstNumber(String numberStr){

        firstNumber = new BigDecimal(replaceComaToDot(numberStr));
    }

    static void setSecondNumber(String numberStr){

        secondNumber = new BigDecimal(replaceComaToDot(numberStr));
    }

    static void setResult(String numberStr){
        resultNumber = new BigDecimal(replaceComaToDot(numberStr));
    }

    private static boolean isResultEmpty(){

        return resultNumber==null;
    }

    private static boolean isSecondNumberEmpty(){

        return secondNumber==null;
    }

    static void setLastOperation(MainOperation operation){
        lastOperation = operation;
    }
}
