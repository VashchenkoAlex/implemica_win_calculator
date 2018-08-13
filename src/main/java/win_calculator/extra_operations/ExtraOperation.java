package win_calculator.extra_operations;

import java.math.BigDecimal;

public interface ExtraOperation {

    BigDecimal calculate(BigDecimal number);

    BigDecimal calculate(BigDecimal firstNumber,BigDecimal secondNumber);

    String getValue();
}
