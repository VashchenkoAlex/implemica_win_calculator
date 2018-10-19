package win_calculator.model.exceptions;

/**
 * Enumerate possible exception types at {@link win_calculator.model.CalcModel}
 */
public enum ExceptionType {

   /**
    * Marker for division by zero exception
    */
   DIVIDE_BY_ZERO,
   /**
    * Marker for square root operation with negative number exception
    */
   NEGATIVE_VALUE_FOR_SQRT,
   /**
    * Marker for number overflow exception
    */
   OVERFLOW,
   /**
    * Marker for division zero by zero exception
    */
   ZERO_DIVIDE_BY_ZERO
}
