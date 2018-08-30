package win_calculator.controller.nodes.digits;

import win_calculator.model.nodes.actions.Action;
import win_calculator.utils.ActionType;
import java.math.BigDecimal;

public class Number implements Action {

    private BigDecimal bigDecimalValue;
    private static final ActionType TYPE = ActionType.NUMBER;

    public Number(BigDecimal bigDecimalValue) {
        this.bigDecimalValue = bigDecimalValue;
    }

    public Number() {
    }


    public String getValue() {

        return bigDecimalValue.toString();
    }

    @Override
    public ActionType getType() {
        return TYPE;
    }

    public BigDecimal getBigDecimalValue(){

        return bigDecimalValue;
    }

    public void setBigDecimalValue(BigDecimal bigDecimalValue) {
        this.bigDecimalValue = bigDecimalValue;
    }
}
