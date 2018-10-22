package win_calculator.model;

import win_calculator.model.operations.Operation;
import win_calculator.model.operations.Number;
import win_calculator.model.operations.OperationType;

import java.util.LinkedList;

import static win_calculator.model.operations.OperationType.*;
import static win_calculator.model.utils.ModelUtils.isBinaryOperation;

/**
 * History container class
 * Store calculator operations at the list
 * Provides methods for creating, modifying and cleaning of operations list
 */
public class History {

   /**
    * Stores calculator operations
    */
   private LinkedList<Operation> operations;

   /**
    * Constructs {@link History} with new LinkedList
    */
   History() {
      operations = new LinkedList<>();
   }

   /**
    * Adds given operation to the operation's list
    *
    * @param operation - given operation
    */
   void addOperation(Operation operation) {

      operations.add(operation);
   }

   /**
    * Setter for operations
    *
    * @param operations - given LinkedList<Operation> for set
    */
   public void setOperations(LinkedList<Operation> operations) {

      this.operations = operations;
   }

   /**
    * Getter for operations
    *
    * @return LinkedList<Operation> of current history
    */
   public LinkedList<Operation> getOperations() {
      return operations;
   }

   /**
    * Getter for last operation at history
    *
    * @return last operation at history
    */
   private Operation getLastEvent() {
      return operations.getLast();
   }

   /**
    * Adds given operation to the end if it's possible, else
    * changes last main operation at operations list to the given
    *
    * @param operation - given main operation
    */
   void addOrChangeBinaryOperation(Operation operation) {

      if (!operations.isEmpty() && isChangingMOperationPossible()) {

         for (int i = operations.size() - 1; i > 0; i--) {
            if (isBinaryOperation(operations.get(i).getType())) {
               operations.set(i, operation);
               break;
            }
         }

      } else {
         operations.add(operation);
      }
   }

   /**
    * Changes last number at operations to the given if it's possible,
    * else adds given umber to the end of operations
    *
    * @param number - given number
    */
   void changeLastNumber(Number number) {

      if (isChangingNumberPossible()) {

         for (int i = operations.size() - 1; i >= 0; i--) {
            if (NUMBER.equals(operations.get(i).getType())) {
               operations.set(i, number);
               break;
            }
         }

      } else {
         operations.add(number);
      }

   }

   /**
    * Verifies is changing number at operations possible
    *
    * @return true if changing number at operations possible
    */
   private boolean isChangingNumberPossible() {

      boolean result = false;

      for (Operation operation : operations) {
         if (NUMBER.equals(operation.getType())) {
            result = true;
            break;
         }
      }

      result = result && !isBinaryOperation(getLastEvent().getType());

      return result;
   }

   /**
    * Verifies is changing main operation at operations possible
    *
    * @return true if changing main operation at operations possible
    */
   private boolean isChangingMOperationPossible() {

      boolean result = false;
      for (Operation operation : operations) {

         if (isBinaryOperation(operation.getType())) {
            result = true;
            break;
         }

      }

      return result;
   }

   /**
    * Changes number at beginning of the operations to the given
    * if it's possible
    *
    * @param number - given number
    */
   void changeNumberAtFirstPosition(Number number) {

      if (NUMBER.equals(operations.getFirst().getType())) {
         operations.set(0, number);
      }
   }

   /**
    * Verifies is operations contains given operation type
    *
    * @param expectedType - given operation type
    * @return true if contains
    */
   boolean isContainingGivenOperationType(OperationType expectedType) {

      OperationType type;
      boolean isContaining = false;
      if (!operations.isEmpty()) {

         for (int i = operations.size() - 1; i > 0; i--) {
            type = operations.get(i).getType();

            if (expectedType.equals(type)) {
               isContaining = true;
               break;
            }

            if (isBinaryOperation(type)) {
               break;
            }

         }
      }

      return isContaining;
   }

   /**
    * Verifies is history containing extra operations
    * (sqr, sqrt, fraction)
    *
    * @return true if it's containing
    */
   boolean isContainingExtraOperation() {

      return isContainingGivenOperationType(SQR) ||
              isContainingGivenOperationType(SQRT) ||
              isContainingGivenOperationType(FRACTION);
   }

}





































