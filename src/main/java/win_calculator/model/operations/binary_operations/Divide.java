package win_calculator.model.operations.binary_operations;

import win_calculator.model.operations.OperationType;
import win_calculator.model.exceptions.OperationException;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static win_calculator.model.exceptions.ExceptionType.DIVIDE_BY_ZERO;
import static win_calculator.model.exceptions.ExceptionType.ZERO_DIVIDE_BY_ZERO;

/**
 * Entity class for divide operation at {@link win_calculator.model.CalcModel}
 */
public class Divide implements BinaryOperation {

   /**
    * Constant of max scaling, is used for rounding before check on overflow
    * is out of working number's range
    */
   private static final int SCALE = 10030;

   /**
    * Overridden method from {@link BinaryOperation}
    * Calculate divide operation on given BigDecimal firstNumber with given BigDecimal secondNumber
    *
    * @param firstNumber  - given first BigDecimal number
    * @param secondNumber - given second BigDecimal number
    * @return BigDecimal result of calculation
    * throws {@link OperationException} if firstNumber and secondNumber equal zero or just secondNumber
    */
   @Override
   public BigDecimal calculate(BigDecimal firstNumber, BigDecimal secondNumber) throws OperationException {
      if (firstNumber.equals(BigDecimal.ZERO) && secondNumber.equals(BigDecimal.ZERO)) {
         throw new OperationException(ZERO_DIVIDE_BY_ZERO);
      }

      try {
         return firstNumber.divide(secondNumber, SCALE, RoundingMode.HALF_UP);
      } catch (ArithmeticException e) {
         throw new OperationException(DIVIDE_BY_ZERO);
      }
   }

   /**
    * Getter for Divide operation type
    *
    * @return operation type of divide
    */
   @Override
   public OperationType getType() {
      return OperationType.DIVIDE;
   }

}
