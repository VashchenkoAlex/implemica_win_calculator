package win_calculator.controller.enums;

/**
 * Enum class with possibles digit types and values for them
 */
public enum DigitType {

    COMA("."),
    ZERO("0"),
    ONE("1"),
    TWO("2"),
    THREE("3"),
    FOUR("4"),
    FIVE("5"),
    SIX("6"),
    SEVEN("7"),
    EIGHT("8"),
    NINE("9");

    private String value;

    DigitType(String value){
        this.value = value;
    }

    public String getValue(){

        return value;
    }
}
