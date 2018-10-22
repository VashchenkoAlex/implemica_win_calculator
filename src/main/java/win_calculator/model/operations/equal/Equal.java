package win_calculator.model.operations.equal;

import win_calculator.model.operations.Operation;
import win_calculator.model.operations.OperationType;

/**
 * Entity class for equal operation at {@link win_calculator.model.CalcModel}
 */
public class Equal implements Operation {

   /**
    * Getter for equal operation type
    *
    * @return operation type of equal
    */
   @Override
   public OperationType getType() {
      return OperationType.EQUAL;
   }
}
