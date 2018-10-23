package win_calculator.model.operations.extra_operations;

import win_calculator.model.operations.OperationType;

import java.math.BigDecimal;

/**
 * Entity class for negate operation at {@link win_calculator.model.CalcModel}
 */
public class Negate implements ExtraOperation {

   /**
    * Overridden method from {@link ExtraOperation}
    * Calculate negate operation on given BigDecimal number
    *
    * @param number - given BigDecimal number
    * @return BigDecimal result of calculation
    */
   @Override
   public BigDecimal calculate(BigDecimal number) {
      return number.negate();
   }

   /**
    * Getter for negate operation type
    *
    * @return operation type of negate
    */
   @Override
   public OperationType getType() {
      return OperationType.NEGATE;
   }

}
