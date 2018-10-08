package win_calculator.model;

import win_calculator.model.operations.Operation;
import win_calculator.model.operations.Number;
import win_calculator.model.operations.OperationType;

import java.util.LinkedList;

import static win_calculator.model.operations.OperationType.MAIN_OPERATION;
import static win_calculator.model.operations.OperationType.NUMBER;

public class History {

    private LinkedList<Operation> operations;

    public History() {

        operations = new LinkedList<>();
    }

    public void addEvent(Operation operation) {

        operations.add(operation);
    }

    public void setOperations(LinkedList<Operation> operations) {

        this.operations = operations;
    }

    public LinkedList<Operation> getOperations() {

        return operations;
    }

    private Operation getLastEvent() {

            return operations.getLast();
    }

    public void changeLastEvent(Operation operation) {

        if (!operations.isEmpty() && isChangingMOperationPossible()) {
            for (int i = operations.size() - 1; i > 0; i--) {
                if (MAIN_OPERATION.equals(operations.get(i).getType())) {
                    operations.set(i, operation);
                    break;
                }
            }
        } else {
            operations.add(operation);
        }
    }

    public void changeLastNumber(Number number) {

        if (isChangingNumberPossible()) {
            for (int i = operations.size() - 1; i >= 0; i--) {
                if (NUMBER.equals(operations.get(i).getType())) {
                    operations.set(i, number);
                    break;
                }
            }
        } else {
            operations.add(number);
        }

    }

    private boolean isChangingNumberPossible() {

        boolean result = false;
        for (Operation operation : operations) {
            if (NUMBER.equals(operation.getType())) {
                result = true;
                break;
            }
        }
        result = result && !MAIN_OPERATION.equals(getLastEvent().getType());
        return result;
    }

    private boolean isChangingMOperationPossible() {

        boolean result = false;
        for (Operation operation : operations) {
            if (MAIN_OPERATION.equals(operation.getType())) {
                result = true;
                break;
            }
        }
        return result;
    }

    public void changeNumberAtFirstPosition(Number number) {

        if (NUMBER.equals(operations.getFirst().getType())) {
            operations.set(0, number);
        }
    }

    public boolean isContain(OperationType expectedType){

        boolean result = false;
        OperationType type;
        if (!operations.isEmpty()) {
            for (int i = operations.size() - 1; i > 0; i--) {
                type = operations.get(i).getType();
                if (expectedType.equals(type)){
                    result = true;
                }
                if (MAIN_OPERATION.equals(type)){
                    break;
                }
            }
        }
        return result;
    }
}
