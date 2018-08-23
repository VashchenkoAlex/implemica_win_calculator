package win_calculator.model.nodes.actions.extra_operations;

import win_calculator.exceptions.MyException;
import win_calculator.model.nodes.actions.Action;
import win_calculator.utils.ActionType;

import java.math.BigDecimal;

public interface ExtraOperation extends Action {

    BigDecimal calculate(BigDecimal number) throws MyException;

    String getValue();
    ActionType getType();
}
