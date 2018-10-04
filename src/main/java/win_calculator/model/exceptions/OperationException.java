package win_calculator.model.exceptions;

public class OperationException extends Exception {

    private ExceptionType type;
    public OperationException(ExceptionType type){
        this.type = type;
    }

    public ExceptionType getType() {
        return type;
    }
}
