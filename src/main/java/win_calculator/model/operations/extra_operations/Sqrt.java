package win_calculator.model.operations.extra_operations;

import win_calculator.model.operations.OperationType;
import win_calculator.model.exceptions.OperationException;

import java.math.BigDecimal;
import java.math.BigInteger;

import static win_calculator.model.exceptions.ExceptionType.NEGATIVE_VALUE_FOR_SQRT;

/**
 * Entity class for square root operation at {@link win_calculator.model.CalcModel}
 */
public class Sqrt implements ExtraOperation {

   /**
    * Constant of max scaling for the method
    */
   private static final int SCALE = 10001;

   /**
    * Overridden method from {@link ExtraOperation}
    * Calculate square root operation on given BigDecimal number
    *
    * @param number - given BigDecimal number
    * @return BigDecimal result of calculation
    * throws {@link OperationException} if given number is less than zero
    * <p>
    * was taken from https://www.java-forums.org/advanced-java/44345-square-rooting-bigdecimal.html
    */
   @Override
   public BigDecimal calculate(BigDecimal number) throws OperationException {

      BigDecimal calculationResult;
      if (number.compareTo(BigDecimal.ZERO) == 0 || number.compareTo(BigDecimal.ONE) == 0) {
         calculationResult = number;

      } else if (number.compareTo(BigDecimal.ZERO) < 0) {
         throw new OperationException(NEGATIVE_VALUE_FOR_SQRT);

      } else {
         BigInteger integerValue = number.movePointRight(SCALE << 1).toBigInteger();
         int bits = (integerValue.bitLength() + 1) >> 1;
         BigInteger firstVar = integerValue.shiftRight(bits);
         BigInteger secondVar;

         do {
            secondVar = firstVar;
            firstVar = firstVar.add(integerValue.divide(firstVar)).shiftRight(1);
         } while (firstVar.compareTo(secondVar) != 0);

         calculationResult = new BigDecimal(firstVar, SCALE);
      }

      return calculationResult;
   }

   /**
    * Getter for sqrt operation type
    *
    * @return operation type of sqrt
    */
   @Override
   public OperationType getType() {

      return OperationType.SQRT;
   }

}
