package win_calculator.controller;

import win_calculator.model.nodes.events.Event;
import win_calculator.controller.digits.Digit;
import win_calculator.controller.digits.ZeroDigit;
import win_calculator.model.nodes.events.EventType;

import java.math.BigDecimal;
import java.util.LinkedList;

import static win_calculator.utils.CalculatorUtils.addCapacity;
import static win_calculator.utils.CalculatorUtils.convertToString;
import static win_calculator.utils.CalculatorUtils.replaceDotToComa;
import static win_calculator.model.nodes.events.EventType.*;

public class NumberBuilder {

    private static final int MAX_DIGITS = 16;
    private static final String COMA = ".";
    private static final String ZERO = "0";
    private static final String DISPLAY_PATTERN = "#############,###.################";
    private boolean positive = true;
    private BigDecimal number;
    private LinkedList<Digit> digitsChain = new LinkedList<>();
    private LinkedList<Digit> previousChain;

    String toDo(Event event) {

        EventType eventType = event.getType();
        if (DIGIT.equals(eventType) && isNotMaxDigits()) {
            add((Digit) event);
        } else if (BACKSPACE.equals(eventType)) {
            backSpace();
        }
        return convertChainToString();
    }

    private void add(Digit digit) {

        if (COMA.equals(digit.getValue())) {
            addComa(digit);
        } else if (ZERO.equals(digit.getValue())) {
            addZero(digit);
        } else {
            addDigit(digit);
        }
        number = new BigDecimal(getValue());
    }

    BigDecimal finish() {

        prepareNumber();
        previousChain = digitsChain;
        digitsChain = new LinkedList<>();
        BigDecimal result;
        if (number != null) {
            result = number;
        } else {
            result = null;
        }
        number = null;
        return result;
    }

    public void clear() {

        number = null;
        digitsChain = new LinkedList<>();
        previousChain = null;
        resetPositive();
    }

    private void backSpace() {

        if (digitsChain.isEmpty() && isPreviousChainNotEmpty()) {
            digitsChain = previousChain;
            cutLastDigit();
            BigDecimal value = new BigDecimal(getValue());
            number = setSign(value);
        } else if (!digitsChain.isEmpty()) {
            cutLastDigit();
            BigDecimal value = new BigDecimal(getValue());
            number = setSign(value);
        }

    }

    private BigDecimal setSign(BigDecimal value){

        BigDecimal result = value;
        if (!positive && result.compareTo(BigDecimal.ZERO) > 0) {
            result = result.negate();
        }
        return result;
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
            result = convertToString(number.negate(),DISPLAY_PATTERN);
        }
        return result;
    }

    private void resetPositive() {

        positive = true;
    }

    boolean containsNumber() {

        return isChainNotEmpty() || isPreviousChainNotEmpty() || (number != null);
    }

    private void prepareNumber() {

        if (digitsChain.isEmpty() && isPreviousChainNotEmpty()) {
            digitsChain = previousChain;
            number = setSign(new BigDecimal(getValue()));
        } else if (isChainNotEmpty()) {
            BigDecimal value = setSign(number);
            if (positive && value.compareTo(BigDecimal.ZERO) < 0){
                value = value.abs();
            }
            number = value;
        }else if (number != null){
            number = setSign(number);
        }
    }

    public void setNumber(BigDecimal number){

        this.number = number;
    }

    public BigDecimal getNumber(){

        if (number == null){
            prepareNumber();
        }
        return number;
    }

    private String convertChainToString(){

        StringBuilder resultBuilder = new StringBuilder();
        if (!positive){
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
