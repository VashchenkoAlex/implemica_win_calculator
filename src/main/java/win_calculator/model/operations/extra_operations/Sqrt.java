package win_calculator.model.operations.extra_operations;

import win_calculator.model.operations.OperationKind;
import win_calculator.model.operations.OperationType;
import win_calculator.model.exceptions.OperationException;

import java.math.BigDecimal;
import java.math.BigInteger;

import static win_calculator.model.exceptions.ExceptionType.NEGATIVE_VALUE_FOR_SQRT;

public class Sqrt implements ExtraOperation {

    private static final String SYMBOL = "√( ";
    private static final OperationType TYPE = OperationType.EXTRA_OPERATION;
    private static final OperationKind kind = OperationKind.SQRT;
    private static final int SCALE = 10001;

    //was taken from https://www.java-forums.org/advanced-java/44345-square-rooting-bigdecimal.html
    @Override
    public BigDecimal calculate(BigDecimal number) throws OperationException {
        BigDecimal result;
        if (number.compareTo(BigDecimal.ZERO) == 0 || number.compareTo(BigDecimal.ONE) == 0) {
            result = number;
        } else if (number.compareTo(BigDecimal.ZERO)<0){
            throw new OperationException(NEGATIVE_VALUE_FOR_SQRT);
        } else {
            BigInteger integerValue = number.movePointRight(SCALE << 1).toBigInteger();
            int bits = (integerValue.bitLength() + 1) >> 1;
            BigInteger firstVar = integerValue.shiftRight(bits);
            BigInteger secondVar;
            do {
                secondVar = firstVar;
                firstVar = firstVar.add(integerValue.divide(firstVar)).shiftRight(1);
            } while (firstVar.compareTo(secondVar) != 0);
            result = new BigDecimal(firstVar, SCALE);
        }
        return result;
    }

    @Override
    public String getValue() {

        return SYMBOL;
    }

    @Override
    public OperationType getType() {

        return TYPE;
    }

    @Override
    public OperationKind getKind() {

        return kind;
    }
}
