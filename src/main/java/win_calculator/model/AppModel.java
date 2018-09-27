package win_calculator.model;

import win_calculator.model.exceptions.MyException;
import win_calculator.model.nodes.events.Event;
import win_calculator.model.nodes.events.Number;

import java.math.BigDecimal;
import java.util.LinkedList;

public class AppModel {

    private OperationProcessor operationProcessor = new OperationProcessor();
    private BigDecimal responseNumber;
    private String errorMessage;

    public BigDecimal toDo(Number number, Event event) {

        try {
            switch (event.getType()) {
                case MAIN_OPERATION: {
                    responseNumber = operationProcessor.processMainOperation(event,number,responseNumber);
                    break;
                }
                case ENTER: {
                    responseNumber = operationProcessor.processEnter(number,responseNumber);
                    break;
                }
                case EXTRA_OPERATION: {
                    responseNumber = operationProcessor.processExtraOperation(event,number,responseNumber);
                    break;
                }
                case NEGATE: {
                    responseNumber = operationProcessor.processNegate(event,number,responseNumber);
                    break;
                }
                case PERCENT: {
                    responseNumber = operationProcessor.processPercent(event,number);
                    break;
                }
                case CLEAR: {
                    responseNumber = operationProcessor.processClear();
                    break;
                }
                case CLEAR_ENTERED: {
                    operationProcessor.processClearEntered();
                    responseNumber = null;
                    break;
                }
                case CLEAR_EXTRA: {
                    operationProcessor.rejectLastNumberWithExtraOperations();
                    break;
                }
            }
        } catch (MyException e) {
            errorMessage = e.getMessage();
            responseNumber = null;
        }
        return responseNumber;
    }

    public LinkedList<Event> getHistory() {

        return operationProcessor.getHistory();
    }

    public String getErrorMessage(){

        return errorMessage;
    }

}
