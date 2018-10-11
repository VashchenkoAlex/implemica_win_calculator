package win_calculator.model.exceptions;

/**
 * Enumerate possible exception types at {@link win_calculator.model.CalcModel}
 */
public enum ExceptionType {

   /**
    * Exception type for division by zero
    */
   DIVIDE_BY_ZERO,
   /**
    * Exception type for square root operation with negative number
    */
   NEGATIVE_VALUE_FOR_SQRT,
   /**
    * Exception type for number overflow
    */
   OVERFLOW,
   /**
    * Exception type for division zero by zero
    */
   ZERO_DIVIDE_BY_ZERO
}
