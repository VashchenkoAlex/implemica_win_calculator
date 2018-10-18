package win_calculator.model;

import win_calculator.model.exceptions.*;
import win_calculator.model.operations.memory_operations.MemoryOperation;
import win_calculator.model.operations.Operation;
import win_calculator.model.operations.OperationType;

import java.math.BigDecimal;
import java.util.LinkedList;

import static win_calculator.model.operations.OperationType.*;
import static win_calculator.model.utils.ModelUtils.*;

/**
 * Model class of WinCalculator application
 * Initializes the {@link OperationProcessor} instance
 * Receives data from {@link win_calculator.controller.CalcController}
 * Verifies which operation was sent and decides what methods
 * of the {@link OperationProcessor} have to be called
 * verifies and returns result of operations
 */
public class CalcModel {

   /**
    * The instance of {@link OperationProcessor}
    */
   private OperationProcessor operationProcessor = new OperationProcessor();

   /**
    * Stores BigDecimal result number for return
    */
   private BigDecimal responseNumber;
   /**
    * Stores BigDecimal value of inputted number
    */
   private BigDecimal inputtedNumber;

   /**
    * Receives BigDecimal number and store it
    *
    * @param number - given BigDecimal number
    */
   public void calculate(BigDecimal number) {

      inputtedNumber = number;
   }

   /**
    * Receives current operation
    * Decides which method has to be called for calculations by
    * current operation's type
    * Verifies result on overflow
    * Rounds result if it's necessary
    *
    * @param operation - current operation
    * @return BigDecimal value of calculation's result
    * @throws OperationException if number is overflow or
    *                            {@link OperationProcessor} throws {@link OperationException}
    */
   public BigDecimal calculate(Operation operation) throws OperationException {

      OperationType type = operation.getType();
      if (isBinaryOperation(type)){
         responseNumber = operationProcessor.processBinaryOperation(operation, inputtedNumber, responseNumber);
      } else if (EQUAL.equals(type)) {
         responseNumber = operationProcessor.processEnter(inputtedNumber, responseNumber);
      } else if (isExtraOperation(type)) {
         responseNumber = operationProcessor.processExtraOperation(operation, inputtedNumber, responseNumber);
      } else if (NEGATE.equals(type)) {
         responseNumber = operationProcessor.processNegate(operation, inputtedNumber, responseNumber);
      } else if (PERCENT.equals(type)) {
         responseNumber = operationProcessor.processPercent(operation, inputtedNumber);
      } else if (CLEAR.equals(type)) {
         responseNumber = operationProcessor.processClear();
      } else if (CLEAR_ENTERED.equals(type)) {
         operationProcessor.processClearEntered();
         responseNumber = null;
      } else if (MEMORY.equals(type)) {
         BigDecimal result = operationProcessor.processMemory((MemoryOperation) operation, inputtedNumber);

         if (result != null) {
            responseNumber = result;
         }

      }

      checkOnOverflow(responseNumber);
      return roundNumber(responseNumber);
   }

   /**
    * Getter for history list
    *
    * @return LinkedList<Operation> of current history
    */
   public LinkedList<Operation> getHistory() {

      return operationProcessor.getHistory();
   }

   /**
    * Sends request to the {@link OperationProcessor} to clean
    * last number and extra operations on it at history
    */
   public void clearLastExtra() {

      operationProcessor.rejectLastNumberWithExtraOperations();
   }


}
