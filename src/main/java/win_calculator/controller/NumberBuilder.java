package win_calculator.controller;

import win_calculator.model.nodes.actions.Action;
import win_calculator.controller.nodes.digits.Digit;
import win_calculator.controller.nodes.digits.Number;
import win_calculator.controller.nodes.digits.ZeroDigit;
import win_calculator.utils.ActionType;

import java.math.BigDecimal;
import java.util.LinkedList;

import static win_calculator.utils.ActionType.*;

public class NumberBuilder {

    private static final int MAX_DIGITS = 16;
    private static final String COMA = ".";
    private static final String ZERO = "0";
    private Number number;
    private LinkedList<Digit> digitsChain = new LinkedList<>();

    BigDecimal toDo(Action action) {

        ActionType actionType = action.getType();
        if (DIGIT.equals(actionType) && isNotMaxDigits()) {
            add((Digit) action);
        } else if (BACKSPACE.equals(actionType)&& !digitsChain.isEmpty()) {
            backSpace();
        } else if (CLEAR_ENTERED.equals(actionType)) {
            clear();
        }
        return number.getBigDecimalValue();
    }

    private void add(Digit digit) {
        if (number == null) {
            number = new Number();
        }
        if (COMA.equals(digit.getValue())) {
            addComa(digit);
        } else if (ZERO.equals(digit.getValue())) {
            addZero(digit);
        } else {
            addDigit(digit);
        }
        number.setBigDecimalValue(new BigDecimal(getValue()));
    }

    Number finish() {

        Number finishedNumber = null;
        if (isChainNotEmpty()) {
            finishedNumber = new Number(number.getBigDecimalValue());
            number = new Number();
        }
        digitsChain = new LinkedList<>();
        return finishedNumber;
    }

    public void clear() {

        number = new Number();
        digitsChain = new LinkedList<>();
    }

    private void backSpace() {

        cutLastDigit();
        number.setBigDecimalValue(new BigDecimal(getValue()));
    }

    private void addDigit(Digit digit) {

        if (ZERO.equals(getValue())) {
            digitsChain.removeLast();
            digitsChain.add(digit);
        } else {
            digitsChain.add(digit);
        }
    }

    private void addComa(Digit coma) {
        if (digitsChain.isEmpty()) {
            digitsChain.add(new ZeroDigit());
            digitsChain.add(coma);
        } else {
            if (numberWithoutComa()) {
                digitsChain.add(coma);
            }
        }
    }

    private void addZero(Digit zero) {

        if (!ZERO.equals(getValue())) {
            digitsChain.add(zero);
        }
    }

    private boolean numberWithoutComa() {

        boolean result = true;
        for (Digit digit : digitsChain) {
            if (COMA.equals(digit.getValue())) {
                result = false;
            }
        }
        return result;
    }

    private void cutLastDigit() {

        if (digitsChain.size() > 1) {
            digitsChain.removeLast();
        } else {
            digitsChain.removeLast();
            digitsChain.add(new ZeroDigit());
        }
    }

    private String getValue() {

        StringBuilder result = new StringBuilder();
        for (Digit digit : digitsChain) {
            result.append(digit.getValue());
        }
        return result.toString();
    }

    private boolean isChainNotEmpty() {

        return !digitsChain.isEmpty();
    }

    boolean isNotMaxDigits() {

        int digitsCount = digitsChain.size();
        if (!digitsChain.isEmpty() && ZERO.equals(digitsChain.get(0).getValue())){
            --digitsCount;
        }
        for (Digit d : digitsChain) {
            if (COMA.equals(d.getValue())) {
                --digitsCount;
            }
        }
        return digitsCount < MAX_DIGITS;
    }
}
