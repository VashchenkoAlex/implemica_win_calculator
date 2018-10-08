package win_calculator.model.operations.main_operations;

import win_calculator.model.operations.OperationKind;
import win_calculator.model.operations.OperationType;
import win_calculator.model.exceptions.OperationException;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static win_calculator.model.exceptions.ExceptionType.DIVIDE_BY_ZERO;
import static win_calculator.model.exceptions.ExceptionType.ZERO_DIVEDE_BY_ZERO;

public class Divide implements MainOperation {

    private static final String VALUE = "  \u00F7  ";
    private static final OperationType TYPE = OperationType.MAIN_OPERATION;
    private static final OperationKind kind = OperationKind.DIVIDE;
    private static final int SCALE = 10030;

    @Override
    public BigDecimal calculate(BigDecimal number) throws OperationException {

        BigDecimal result;
        try{
            result = number.divide(number,SCALE,RoundingMode.HALF_DOWN);
        }catch (ArithmeticException e){
            throw new OperationException(ZERO_DIVEDE_BY_ZERO);
        }
        return result;
    }

    @Override
    public BigDecimal calculate(BigDecimal firstNumber, BigDecimal secondNumber) throws OperationException {

        BigDecimal result;
        if (firstNumber.equals(BigDecimal.ZERO) && secondNumber.equals(BigDecimal.ZERO)){
            throw new OperationException(ZERO_DIVEDE_BY_ZERO);
        }
        try{
            result = firstNumber.divide(secondNumber,SCALE,RoundingMode.HALF_UP);
        }catch (ArithmeticException e){
            throw new OperationException(DIVIDE_BY_ZERO);
        }
        return result;
    }

    @Override
    public String getValue() {
        return VALUE;
    }

    @Override
    public OperationKind getKind() {

        return kind;
    }

    @Override
    public OperationType getType() {
        return TYPE;
    }

}
