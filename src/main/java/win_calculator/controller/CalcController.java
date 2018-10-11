package win_calculator.controller;

import win_calculator.controller.entities.NumberSymbol;
import win_calculator.model.CalcModel;
import win_calculator.model.exceptions.ExceptionType;
import win_calculator.model.exceptions.OperationException;
import win_calculator.model.operations.memory_operations.MemoryOperation;
import win_calculator.model.operations.memory_operations.MemoryOperationType;
import win_calculator.model.operations.Operation;
import win_calculator.model.operations.OperationType;

import java.math.BigDecimal;

import static win_calculator.controller.utils.ControllerUtils.convertNumberToString;
import static win_calculator.controller.utils.ControllerUtils.convertHistoryToString;
import static win_calculator.controller.utils.ControllerUtils.isNotNumber;
import static win_calculator.model.exceptions.ExceptionType.*;
import static win_calculator.model.operations.memory_operations.MemoryOperationType.STORE;
import static win_calculator.model.operations.OperationType.*;
import static win_calculator.model.operations.OperationType.EQUAL;

/**
 * Controller class of WinCalculator application
 * Initializes the {@link NumberBuilder} and the {@link CalcModel}
 * Receives data from {@link win_calculator.view.FXMLView}
 * Verifies which operation was sent and decides what has to be then
 * Decides where data have to be transfer
 * Receive response from {@link NumberBuilder} or {@link CalcModel}
 * and convert it to String
 * Handle exceptions from {@link CalcModel}
 * Return String[] response to the {@link win_calculator.view.FXMLView}
 */
public class CalcController {

   /**
    * Constant: message for {@link OperationException} if was given negative value
    * for square root operation
    */
   private static final String NEGATIVE_VALUE_FOR_SQRT_MSG = "Invalid input";
   /**
    * Constant: message for {@link OperationException} if number was overflow
    */
   private static final String OVERFLOW_MSG = "Overflow";
   /**
    * Constant: message for {@link OperationException} if was given zero for divisor
    */
   private static final String DIVIDE_BY_ZERO_MSG = "Cannot divide by zero";
   /**
    * Constant: message for {@link OperationException} if were given  two zeros for
    * divide operation
    */
   private static final String ZERO_DIVIDE_BY_ZERO_MSG = "Result is undefined";
   /**
    * Constant String representation of zero
    */
   private static final String ZERO_STR = "0";
   /**
    * Constant: default DecimalFormat string pattern for display label
    */
   private static final String DISPLAY_PATTERN = "#############,###.################";

   /**
    * The instance of {@link CalcModel}
    */
   private CalcModel model = new CalcModel();
   /**
    * The instance of {@link NumberBuilder}
    */
   private NumberBuilder numberBuilder = new NumberBuilder();

   /**
    * Stores String text for display label
    */
   private String displayText;
   /**
    * Stores String text for history label
    */
   private String historyText;
   /**
    * Stores {@link OperationType} of last operation
    */
   private OperationType lastOperationType;

   /**
    * Flag was it exception after last operation
    */
   private boolean wasException = false;

   /**
    * Method receives operation and handle exceptions from the {@link CalcModel}
    * Saves exception message to the display text
    *
    * @param operation - operation for calculations
    * @return String[] response with text for display and history labels
    */
   public String[] handleOperation(Operation operation) {

      try {
         selectAndProcessOperationByType(operation);
      } catch (OperationException e) {
         displayText = selectMessageForException(e.getType());
         wasException = true;
      }
      setHistoryText(isOperationTypeResettingOverflow(operation.getType()));
      setLastOperationType(operation.getType());

      return new String[]{displayText, historyText};
   }

   /**
    * Method check operation type and select method for handling
    *
    * @param operation - operation for calculations
    * @throws OperationException from the {@link CalcModel}
    */
   private void selectAndProcessOperationByType(Operation operation) throws OperationException {

      OperationType type = operation.getType();
      if (CLEAR_ENTERED == type) {
         handleClearEntered(operation);
      } else if (BACKSPACE == type) {
         handleBackSpace();
      } else if (NEGATE == type && numberBuilder.containsNumber()) {
         handleNegate();
      } else {
         displayText = convertNumberToString(doOperationWithModel(operation), DISPLAY_PATTERN);
      }
   }

   /**
    * Method receives {@link NumberSymbol )
    * Sets up history label text depends on previous operation
    * Save response from {@link NumberBuilder} to the display label text
    *
    * @param numberSymbol - {@link NumberSymbol ) event from view
    * @return String[] response with text for display and history labels
    */
   public String[] handleDigit(NumberSymbol numberSymbol) {

      if (displayText != null && isNotNumber(displayText)) {
         historyText = "";
         numberBuilder.clean();
      }

      if (isExtraOperation(lastOperationType)) {
         model.clearLastExtra();
         historyText = convertHistoryToString(model.getHistory());
      }

      displayText = numberBuilder.addDigit(numberSymbol);
      setHistoryText(true);
      lastOperationType = null;
      return new String[]{displayText, historyText};
   }

   /**
    * Getter for lastOperationType
    *
    * @return {@link OperationType} of last operation
    */
   public OperationType getLastOperationType() {
      return lastOperationType;
   }

   /**
    * Method clears display label text,
    * calls clean() at {@link NumberBuilder}
    * transfers operation to the {@link CalcModel}
    *
    * @param operation - operation for calculations
    * @throws OperationException from the {@link CalcModel}
    */
   private void handleClearEntered(Operation operation) throws OperationException {

      displayText = ZERO_STR;
      numberBuilder.clean();
      model.calculate(operation);
   }

   /**
    * Verifies possibility for backspace
    * and sets up display label text depends on result
    */
   private void handleBackSpace() {

      if (isBackSpacePossible()) {
         displayText = numberBuilder.doBackSpace();
      } else {
         if (displayText == null || isNotNumber(displayText)) {
            displayText = ZERO_STR;
         }
      }
   }

   /**
    * @return true if backspace possible to process depends on
    * previous operation and if {@link NumberBuilder} contains number
    */
   private boolean isBackSpacePossible() {

      return !isBinaryOperation(lastOperationType)
              && !isExtraOperation(lastOperationType)
              && EQUAL != lastOperationType
              && numberBuilder.containsNumber();
   }

   /**
    * Calls method negate() at {@link NumberBuilder} and saves result to the display label text
    */
   private void handleNegate() {

      displayText = numberBuilder.negate(MEMORY.equals(lastOperationType));
   }

   /**
    * Method gets number from {@link NumberBuilder}
    * Sends received number to the {@link CalcModel}
    * Sends operation to the {@link CalcModel}
    * Selects result number depends on operation type
    *
    * @param operation - operation for calculations
    * @return BigDecimal result of calculations
    * @throws OperationException from {@link CalcModel}
    */
   private BigDecimal doOperationWithModel(Operation operation) throws OperationException {

      BigDecimal currentNum = numberBuilder.finish();
      model.calculate(currentNum);
      BigDecimal result = model.calculate(operation);
      if (MEMORY.equals(operation.getType())) {
         MemoryOperationType memoryOperationType = ((MemoryOperation) operation).getMemoryOperationType();

         if (!STORE.equals(memoryOperationType)) {
            numberBuilder.clean();
            numberBuilder.setNumber(result);
         } else if (currentNum != null) {
            numberBuilder.setNumber(currentNum);
            result = currentNum;
         }

      } else if (!NEGATE.equals(operation.getType()) && !MEMORY.equals(operation.getType())) {
         numberBuilder.clean();
      }

      if (EQUAL.equals(operation.getType())) {
         historyText = "";
      }
      return result;
   }

   /**
    * Method select message for exception from {@link CalcModel}
    *
    * @param type - type of thrown exception
    * @return selected String message
    */
   private String selectMessageForException(ExceptionType type) {

      String message;
      if (OVERFLOW.equals(type)) {
         message = OVERFLOW_MSG;
      } else if (DIVIDE_BY_ZERO.equals(type)) {
         message = DIVIDE_BY_ZERO_MSG;
      } else if (ZERO_DIVIDE_BY_ZERO == type) {
         message = ZERO_DIVIDE_BY_ZERO_MSG;
      } else if (NEGATIVE_VALUE_FOR_SQRT == type) {
         message = NEGATIVE_VALUE_FOR_SQRT_MSG;
      } else {
         message = "";
      }
      return message;
   }

   /**
    * Method get history from {@link CalcModel} and converts it to String
    * Sets up history label text
    *
    * @param isResettingOverflow - given flag for resetting history after overflow exception
    */
   private void setHistoryText(boolean isResettingOverflow) {

      if (wasException && isResettingOverflow) {
         historyText = "";
         wasException = false;
      } else {
         historyText = convertHistoryToString(model.getHistory());
      }
   }

   /**
    * Setter of last operation type
    *
    * @param type - type of {@link Operation}
    */
   private void setLastOperationType(OperationType type) {

      lastOperationType = type;
   }

   /**
    * Verifies is reset overflow effects possible
    *
    * @param type - current operatio type
    * @return true if resetting is possible
    */
   private boolean isOperationTypeResettingOverflow(OperationType type) {

      return BACKSPACE == type || CLEAR == type
              || CLEAR_ENTERED == type
              || (EQUAL == lastOperationType && EQUAL == type);
   }

   /**
    * Verifies is given {@link OperationType} binary operation
    * (add, divide, subtract, multiply)
    * @param type - given operation type
    * @return boolean result
    */
   private boolean isBinaryOperation(OperationType type){

      return type == ADD || type == SUBTRACT || type == DIVIDE || type == MULTIPLY;
   }

   /**
    * Verifies is given {@link OperationType} extra operation
    * (sqrt, sqr, fraction)
    * @param type - given operation type
    * @return boolean result
    */
   private boolean isExtraOperation(OperationType type){

      return type == SQR || type == SQRT || type == FRACTION;
   }
}
