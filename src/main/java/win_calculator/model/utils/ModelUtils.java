package win_calculator.model.utils;

import win_calculator.model.exceptions.OperationException;
import win_calculator.model.operations.OperationType;

import java.math.BigDecimal;

import static win_calculator.model.exceptions.ExceptionType.OVERFLOW;
import static win_calculator.model.operations.OperationType.*;

/**
 * Abstract class with utils for checking and rounding BigDecimal numbers
 */
public abstract class ModelUtils {

   /**
    * Constant: module of max BigDecimal value for the {@link win_calculator.model.CalcModel}
    */
   private static final BigDecimal MAX_ABS_VALUE = (new BigDecimal("9.9999999999999995e+9999")).subtract(BigDecimal.valueOf(0.000000000000001));
   /**
    * Constant: module of min BigDecimal value for the {@link win_calculator.model.CalcModel}
    */
   private static final BigDecimal MIN_ABS_VALUE = new BigDecimal("1.e-9999");

   /**
    * Verifies given BigDecimal number on overflow
    *
    * @param number - given BigDecimal number
    * @throws OperationException if given number is overflow
    */
   public static void checkOnOverflow(BigDecimal number) throws OperationException {
      if (isOverflow(number)) {
         throw new OperationException(OVERFLOW);
      }
   }

   /**
    * Compares given BigDecimal number with max and min values for the model
    *
    * @param number - given BigDecimal number
    * @return true if number is out of valid range
    */
   private static boolean isOverflow(BigDecimal number) {
      BigDecimal numberForCheck = number;
      boolean result = false;
      if (numberForCheck != null) {
         numberForCheck = numberForCheck.abs();

         if (isOverflowNumber(numberForCheck)) {
            result = true;
         }
         
      }

      return result;
   }

   /**
    * Verifies is given BigDecimal number overflow
    * @param number - given BigDecimal number
    * @return boolean verification result
    */
   private static boolean isOverflowNumber(BigDecimal number) {
      boolean isBiggerMax = number.compareTo(MAX_ABS_VALUE) > 0;
      boolean isSmallerMin = number.compareTo(MIN_ABS_VALUE) < 0 && number.compareTo(BigDecimal.ZERO) > 0;

      return isBiggerMax || isSmallerMin;
   }

   /**
    * Verifies is given {@link OperationType} binary operation
    * (add, divide, subtract, multiply)
    *
    * @param type - given operation type
    * @return boolean result
    */
   public static boolean isBinaryOperation(OperationType type) {
      return type == ADD || type == SUBTRACT || type == DIVIDE || type == MULTIPLY;
   }

   /**
    * Verifies is given {@link OperationType} extra operation
    * (sqrt, sqr, fraction)
    *
    * @param type - given operation type
    * @return boolean result
    */
   public static boolean isExtraOperation(OperationType type) {
      return type == SQR || type == SQRT || type == FRACTION;
   }
}
