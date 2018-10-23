package win_calculator.model.operations.percent;

import win_calculator.model.operations.Operation;
import win_calculator.model.operations.OperationType;

import java.math.BigDecimal;

/**
 * Entity class for percent operation at {@link win_calculator.model.CalcModel}
 */
public class Percent implements Operation {

   /**
    * Constant of percent calculation value
    */
   private static final BigDecimal PERCENT = BigDecimal.valueOf(0.01);

   /**
    * Calculates percent operation on given first number and second number
    *
    * @param firstNumber  - given BigDecimal first number
    * @param secondNumber - given BigDecimal second number
    * @return BigDecimal result of operation
    */
   public BigDecimal calculate(BigDecimal firstNumber, BigDecimal secondNumber) {
      return firstNumber.multiply(secondNumber.multiply(PERCENT));
   }

   /**
    * Getter for percent operation type
    *
    * @return operation type of percent
    */
   @Override
   public OperationType getType() {
      return OperationType.PERCENT;
   }

}
