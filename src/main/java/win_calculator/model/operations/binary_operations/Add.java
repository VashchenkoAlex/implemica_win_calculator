package win_calculator.model.operations.binary_operations;

import win_calculator.model.operations.OperationType;

import java.math.BigDecimal;

/**
 * Entity class for add operation at {@link win_calculator.model.CalcModel}
 */
public class Add implements BinaryOperation {

   /**
    * Overridden method from {@link BinaryOperation}
    * Calculate add operation on given BigDecimal firstNumber with given BigDecimal secondNumber
    *
    * @param firstNumber  - given first BigDecimal number
    * @param secondNumber - given second BigDecimal number
    * @return BigDecimal result of calculation
    */
   @Override
   public BigDecimal calculate(BigDecimal firstNumber, BigDecimal secondNumber) {

      return firstNumber.add(secondNumber);
   }

   /**
    * Getter for Add operation type
    * @return operation type of add
    */
   @Override
   public OperationType getType() {
      return OperationType.ADD;
   }
}
