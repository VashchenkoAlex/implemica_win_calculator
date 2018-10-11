package win_calculator.controller.entities;

/**
 * Entity class of entered symbol for number
 */
public class NumberSymbol {

    /**
     * Stores string value of current digit
     */
    private String value;

    /**
     * Constructs digit with given string
     * @param value - value of current digit
     */
    public NumberSymbol(String value){
        this.value = value;
    }

    /**
     * Getter for value of current symbol
     * @return String value of symbol
     */
    public String getValue(){
        return value;
    }
}
