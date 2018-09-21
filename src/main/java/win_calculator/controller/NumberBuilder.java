package win_calculator.controller;

import win_calculator.model.nodes.actions.Action;
import win_calculator.controller.nodes.digits.Digit;
import win_calculator.controller.nodes.digits.Number;
import win_calculator.controller.nodes.digits.ZeroDigit;
import win_calculator.utils.ActionType;

import java.math.BigDecimal;
import java.util.LinkedList;

import static win_calculator.utils.ActionType.*;
import static win_calculator.utils.AppUtils.addCapacity;
import static win_calculator.utils.AppUtils.convertToString;
import static win_calculator.utils.AppUtils.replaceDotToComa;

public class NumberBuilder {

    private static final int MAX_DIGITS = 16;
    private static final String COMA = ".";
    private static final String ZERO = "0";
    private static final String DISPLAY_PATTERN = "#############,###.################";
    private boolean positive = true;
    private Number number;
    private LinkedList<Digit> digitsChain = new LinkedList<>();
    private LinkedList<Digit> previousChain;

    String toDo(Action action) {

        ActionType actionType = action.getType();
        if (DIGIT.equals(actionType) && isNotMaxDigits()) {
            add((Digit) action);
        } else if (BACKSPACE.equals(actionType)) {
            backSpace();
        }
        return convertChainToString();
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

        prepareNumber();
        previousChain = digitsChain;
        digitsChain = new LinkedList<>();
        Number result;
        if (number != null && number.getBigDecimalValue() != null) {
            result = number;
        } else {
            result = null;
        }
        number = new Number();
        return result;
    }

    public void clear() {

        number = new Number();
        digitsChain = new LinkedList<>();
        previousChain = null;
        resetPositive();
    }

    private void backSpace() {

        if (digitsChain.isEmpty() && isPreviousChainNotEmpty()) {
            digitsChain = previousChain;
            cutLastDigit();
            BigDecimal value = new BigDecimal(getValue());
            if (!positive && value.compareTo(BigDecimal.ZERO) > 0) {
                value = value.negate();
            }
            number.setBigDecimalValue(value);
        } else if (!digitsChain.isEmpty()) {
            cutLastDigit();
            BigDecimal value = new BigDecimal(getValue());
            if (!positive && value.compareTo(BigDecimal.ZERO) > 0) {
                value = value.negate();
            }
            number.setBigDecimalValue(value);
        }

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

    private boolean isPreviousChainNotEmpty() {

        return previousChain != null && !previousChain.isEmpty();
    }

    private boolean isNotMaxDigits() {

        int digitsCount = digitsChain.size();
        if (!digitsChain.isEmpty() && ZERO.equals(digitsChain.get(0).getValue())) {
            --digitsCount;
        }
        for (Digit d : digitsChain) {
            if (COMA.equals(d.getValue())) {
                --digitsCount;
            }
        }
        return digitsCount < MAX_DIGITS;
    }

    private void changeIsPositive() {
        positive = !positive;
    }

    String negate(boolean wasMemoryAction) {

        changeIsPositive();
        String result;
        if (!wasMemoryAction){
            prepareNumber();
            result = convertChainToString();
        }else {
            result = convertToString(number.getBigDecimalValue().negate(),DISPLAY_PATTERN);
        }
        return result;
    }

    private void resetPositive() {

        positive = true;
    }

    boolean containsNumber() {

        return isChainNotEmpty() || isPreviousChainNotEmpty() || (number != null && number.getBigDecimalValue() != null);
    }

    private void prepareNumber() {

        if (digitsChain.isEmpty() && isPreviousChainNotEmpty()) {
            digitsChain = previousChain;
            BigDecimal value = new BigDecimal(getValue());
            if (!positive && value.compareTo(BigDecimal.ZERO) > 0) {
                value = value.negate();
            }
            number = new Number(value);
        } else if (isChainNotEmpty()) {
            BigDecimal value = number.getBigDecimalValue();
            boolean positiveValue = value.compareTo(BigDecimal.ZERO) > 0;
            if (!positive && positiveValue) {
                value = value.negate();
            }else if (positive && !positiveValue){
                value = value.abs();
            }
            number = new Number(value);
        }else if (number != null && number.getBigDecimalValue() != null){
            BigDecimal value = number.getBigDecimalValue();
            if (!positive && value.compareTo(BigDecimal.ZERO) > 0){
                number.setBigDecimalValue(value.negate());
            }
        }
    }

    public void setNumber(Number number){

        this.number = number;
    }

    public Number getNumber(){

        if (number != null && number.getBigDecimalValue() == null){
            prepareNumber();
        }
        return number;
    }

    private boolean isNumberNegative(){

        return !positive;
    }

    String convertChainToString(){

        StringBuilder resultBuilder = new StringBuilder();
        if (isNumberNegative()){
            resultBuilder = new StringBuilder("-");
        }
        LinkedList<Digit> chain = digitsChain;
        if (digitsChain.isEmpty()){
            chain = previousChain;
        }
        if (chain != null && !chain.isEmpty()){
            for (Digit digit: chain) {
                resultBuilder.append(digit.getValue());
            }
        }
        String result = resultBuilder.toString();
        if (!"".equals(result)){
            result = addCapacity(replaceDotToComa(result));
        }
        return result;
    }
}
