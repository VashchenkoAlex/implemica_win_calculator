package win_calculator.model.operations.extra_operations;

import win_calculator.model.operations.OperationType;

import java.math.BigDecimal;

/**
 * Entity class for square operation at {@link win_calculator.model.CalcModel}
 */
public class Sqr implements ExtraOperation {

   /**
    * Constant of current exponent for the method
    */
   private static final int POW = 2;

   /**
    * Overridden method from {@link ExtraOperation}
    * Calculate square operation on given BigDecimal number
    *
    * @param number - given BigDecimal number
    * @return BigDecimal result of calculation
    */
   @Override
   public BigDecimal calculate(BigDecimal number) {
      return number.pow(POW);
   }

   /**
    * Getter for sqr operation type
    *
    * @return operation type of sqr
    */
   @Override
   public OperationType getType() {
      return OperationType.SQR;
   }

}
