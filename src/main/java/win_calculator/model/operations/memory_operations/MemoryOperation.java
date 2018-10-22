package win_calculator.model.operations.memory_operations;

import win_calculator.model.operations.Operation;
import win_calculator.model.operations.OperationType;

/**
 * Entity class for memory operation at {@link win_calculator.model.CalcModel}
 */
public class MemoryOperation implements Operation {

   /**
    * Stores {@link MemoryOperationType} of current memory operation
    */
   private MemoryOperationType memoryOperationType;

   /**
    * Constructs {@link MemoryOperation} with given {@link MemoryOperationType}
    *
    * @param memoryOperationType - given {@link MemoryOperationType}
    */
   public MemoryOperation(MemoryOperationType memoryOperationType) {
      this.memoryOperationType = memoryOperationType;
   }

   /**
    * Getter for current memory operation type
    *
    * @return operation type of current memory operation
    */
   public MemoryOperationType getMemoryOperationType() {
      return memoryOperationType;
   }

   /**
    * Getter for Memory operation type
    *
    * @return operation type of memory
    */
   public OperationType getType() {
      return OperationType.MEMORY;
   }
}
