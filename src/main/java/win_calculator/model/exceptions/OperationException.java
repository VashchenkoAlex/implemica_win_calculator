package win_calculator.model.exceptions;

/**
 * Operation exception for {@link win_calculator.model.CalcModel}
 * Could be thrown after arithmetic exception
 * Is an a part of logic
 */
public class OperationException extends Exception {

    /**
     * Stores type of exception
     */
    private ExceptionType type;

    /**
     * Constructs exception with {@link ExceptionType}
     * @param type - given exception type
     */
    public OperationException(ExceptionType type){
        this.type = type;
    }

    /**
     * Getter for type of current exception
     * @return {@link ExceptionType} of current exception
     */
    public ExceptionType getType() {
        return type;
    }
}
