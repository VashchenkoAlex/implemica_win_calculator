package win_calculator.model.operations.extra_operations;

import win_calculator.model.exceptions.OperationException;
import win_calculator.model.operations.OperationType;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static win_calculator.model.exceptions.ExceptionType.DIVIDE_BY_ZERO;

/**
 * Entity class for fraction operation at {@link win_calculator.model.CalcModel}
 */
public class Fraction implements ExtraOperation {

   /**
    * Constant of max scaling for the calculator app
    */
   private static final int SCALE = 10000;

   /**
    * Overridden method from {@link ExtraOperation}
    * Calculate BigDecimal.ONE divide on given BigDecimal number
    *
    * @param number - given BigDecimal number
    * @return BigDecimal result of calculation
    * throws {@link OperationException} when given BigDecimal number equals zero
    */
   @Override
   public BigDecimal calculate(BigDecimal number) throws OperationException {
      try {
         return BigDecimal.ONE.divide(number, SCALE, RoundingMode.HALF_UP);
      } catch (ArithmeticException e) {
         throw new OperationException(DIVIDE_BY_ZERO);
      }
   }

   /**
    * Getter for fraction operation type
    *
    * @return operation type of fraction
    */
   @Override
   public OperationType getType() {
      return OperationType.FRACTION;
   }

}
