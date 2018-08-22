package win_calculator;

import org.junit.jupiter.api.Test;
import win_calculator.exceptions.MyException;
import win_calculator.model.button_handlers.ExtraOperationHandler;
import win_calculator.model.button_handlers.MainOperationHandler;
import win_calculator.model.button_handlers.PercentHandler;
import win_calculator.model.nodes.actions.extra_operations.ExtraOperation;
import win_calculator.model.nodes.actions.extra_operations.Percent;
import win_calculator.model.nodes.actions.extra_operations.Sqrt;
import win_calculator.model.nodes.actions.main_operations.*;

import java.math.BigDecimal;

import static org.junit.gen5.api.Assertions.assertEquals;
import static win_calculator.utils.StringUtils.addCapacity;
import static win_calculator.utils.StringUtils.removeCapacity;
import static win_calculator.utils.StringUtils.replaceComaToDot;

class FXMLViewControllerTest {

    private final ExtraOperationHandler extraActBtnHandler = new ExtraOperationHandler();
    private final Sqrt sqrt = new Sqrt();


}
