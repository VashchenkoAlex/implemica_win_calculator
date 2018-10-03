package win_calculator.model;

import win_calculator.model.exceptions.OverflowException;
import win_calculator.model.exceptions.DivideByZeroException;
import win_calculator.model.exceptions.NegativeValueForSQRTException;
import win_calculator.model.exceptions.UndefinedResultException;
import win_calculator.model.memory.MemoryEvent;
import win_calculator.model.nodes.events.Event;

import java.math.BigDecimal;
import java.util.LinkedList;

import static win_calculator.model.utils.ModelUtils.checkOnOverflow;
import static win_calculator.model.utils.ModelUtils.roundNumber;

public class CalcModel {

    private OperationProcessor operationProcessor = new OperationProcessor();
    private BigDecimal responseNumber;

    public BigDecimal toDo(BigDecimal number, Event event) throws UndefinedResultException, DivideByZeroException, NegativeValueForSQRTException, OverflowException {

        switch (event.getType()) {
            case MAIN_OPERATION: {
                responseNumber = operationProcessor.processMainOperation(event, number, responseNumber);
                break;
            }
            case ENTER: {
                responseNumber = operationProcessor.processEnter(number, responseNumber);
                break;
            }
            case EXTRA_OPERATION: {
                responseNumber = operationProcessor.processExtraOperation(event, number, responseNumber);
                break;
            }
            case NEGATE: {
                responseNumber = operationProcessor.processNegate(event, number, responseNumber);
                break;
            }
            case PERCENT: {
                responseNumber = operationProcessor.processPercent(event, number);
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
            case MEMORY: {
                BigDecimal result = operationProcessor.processMemory((MemoryEvent) event, number);
                if (result!=null){
                    responseNumber = result;
                }
                break;
            }
        }
        checkOnOverflow(responseNumber);
        return roundNumber(responseNumber);
    }

    public LinkedList<Event> getHistory() {

        return operationProcessor.getHistory();
    }
}
