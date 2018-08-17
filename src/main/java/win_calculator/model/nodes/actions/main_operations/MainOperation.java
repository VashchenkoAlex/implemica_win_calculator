package win_calculator.model.nodes.actions.main_operations;

import win_calculator.exceptions.MyException;
import win_calculator.model.nodes.actions.Action;

import java.math.BigDecimal;

public interface MainOperation extends Action {

    BigDecimal calculate(BigDecimal firstNumber, BigDecimal secondNumber) throws MyException;

    BigDecimal calculate(BigDecimal number) throws MyException;

    String getValue();
}
