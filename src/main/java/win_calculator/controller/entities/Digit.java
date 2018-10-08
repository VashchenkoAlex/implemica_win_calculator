package win_calculator.controller.entities;

import win_calculator.controller.enums.DigitType;
import win_calculator.model.operations.Operation;
import win_calculator.model.operations.OperationType;

public class Digit implements Operation {

    private DigitType type;

    public Digit(DigitType type){
        this.type = type;
    }

    public String getValue(){

        return type.getValue();
    }

    @Override
    public OperationType getType() {
        return OperationType.DIGIT;
    }
}
