package win_calculator.model.nodes.events.main_operations;

import win_calculator.model.exceptions.DivideByZeroException;
import win_calculator.model.exceptions.UndefinedResultException;
import win_calculator.model.nodes.events.Event;

import java.math.BigDecimal;

public interface MainOperation extends Event {

    BigDecimal calculate(BigDecimal firstNumber, BigDecimal secondNumber) throws UndefinedResultException, DivideByZeroException;

    BigDecimal calculate(BigDecimal number) throws UndefinedResultException;

    String getValue();
}
