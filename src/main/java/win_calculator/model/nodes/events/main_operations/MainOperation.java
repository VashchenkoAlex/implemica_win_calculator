package win_calculator.model.nodes.events.main_operations;

import win_calculator.model.exceptions.OperationException;
import win_calculator.model.nodes.events.Event;

import java.math.BigDecimal;

public interface MainOperation extends Event {

    BigDecimal calculate(BigDecimal firstNumber, BigDecimal secondNumber) throws OperationException;

    BigDecimal calculate(BigDecimal number) throws OperationException;

    String getValue();
}
