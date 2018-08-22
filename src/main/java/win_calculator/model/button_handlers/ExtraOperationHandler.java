package win_calculator.model.button_handlers;

import win_calculator.exceptions.MyException;
import win_calculator.model.nodes.actions.extra_operations.ExtraOperation;
import java.math.BigDecimal;

public class ExtraOperationHandler {

    public BigDecimal doOperation(BigDecimal number, ExtraOperation operation) throws MyException {

        return operation.calculate(number);
    }
}
