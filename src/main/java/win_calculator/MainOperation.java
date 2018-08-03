package win_calculator;

public enum MainOperation {
    PLUS(" + "),
    MINUS(" - "),
    MULTIPLY(" x "),
    DIVIDE(" / ");

    private String value;
    private MainOperation(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
