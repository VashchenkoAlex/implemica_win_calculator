package win_calculator.controller.entities;

/**
 * Entity class of entered symbol for number
 */
public class NumberSymbol {

    /**
     * Stores string symbol of current digit
     */
    private Symbol symbol;

    /**
     * Constructs digit with given string
     * @param symbol - symbol of current digit
     */
    public NumberSymbol(Symbol symbol){
        this.symbol = symbol;
    }

    /**
     * Getter for string of current symbol
     * @return {@link Symbol}
     */
    public String getSymbol(){
        return symbol.getString();
    }
}
