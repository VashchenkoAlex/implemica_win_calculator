package win_calculator.controller;

import win_calculator.model.nodes.actions.Action;
import win_calculator.model.nodes.actions.digits.Digit;
import win_calculator.model.nodes.actions.digits.Number;
import win_calculator.model.nodes.actions.digits.ZeroDigit;
import win_calculator.utils.ActionType;

import java.math.BigDecimal;
import java.util.LinkedList;

import static win_calculator.utils.ActionType.*;

public class NumberBuilder {

    private boolean wasNotOperation = true;
    private Number number;
    private LinkedList<Digit> digitsChain = new LinkedList<>();

    public String toDo(Action action){

        ActionType actionType = action.getType();
        if (DIGIT.equals(actionType)){
            add((Digit) action);
        }else if (BACKSPACE.equals(actionType)){
            backSpace();
        }else if (CLEAR_ENTERED.equals(actionType)){
            clear();
        }
        return getValue();
    }

    public void add(Digit digit){
        if (number == null) {
            number = new Number();
        }
        wasNotOperation = false;
        if (".".equals(digit.getValue())){
            addComa(digit);
        }else if("0".equals(digit.getValue())){
            addZero(digit);
        }else {
            addDigit(digit);
        }
        number.setBigDecimalValue(new BigDecimal(getValue()));
    }

    public Number finish(){

        Number finishedNumber = null;
        if (isChainNotEmpty()){
            finishedNumber = new Number(number.getBigDecimalValue());
            number = new Number();
        }else if (wasNotOperation){
            finishedNumber = new Number(BigDecimal.ZERO);
        }
        digitsChain = new LinkedList<>();
        return finishedNumber;
    }

    public void clear(){

        number = new Number();
        digitsChain = new LinkedList<>();
    }

    private void backSpace(){

        cutLastDigit();
        number.setBigDecimalValue(new BigDecimal(getValue()));
    }

    private void addDigit(Digit digit){

        if ("0".equals(getValue())){
            digitsChain.removeLast();
            digitsChain.add(digit);
        }else {
            digitsChain.add(digit);
        }
    }

    private void addComa(Digit coma){
        if (digitsChain.isEmpty()){
            digitsChain.add(new ZeroDigit());
            digitsChain.add(coma);
        }else {
            if (numberWithoutComa()){
                digitsChain.add(coma);
            }
        }
    }

    private void addZero(Digit zero){

        if (!"0".equals(getValue())){
            digitsChain.add(zero);
        }
    }

    private boolean numberWithoutComa(){

        boolean result = true;
        for (Digit digit : digitsChain) {
            if (".".equals(digit.getValue())){
                result = false;
            }
        }
        return result;
    }

    private void cutLastDigit(){

        if (digitsChain.size()>1){
            digitsChain.removeLast();
        }else {
            digitsChain.removeLast();
            digitsChain.add(new ZeroDigit());
        }
    }

    private String getValue(){

        StringBuilder result = new StringBuilder();
        for (Digit digit : digitsChain) {
            result.append(digit.getValue());
        }
        return result.toString();
    }

    private boolean isChainNotEmpty(){

        return !digitsChain.isEmpty();
    }
}
