package win_calculator.controller.entities;

import win_calculator.controller.enums.DigitType;

/**
 * Entity class of digit event
 */
public class Digit{

    /**
     * Stores type of current digit
     */
    private DigitType type;

    /**
     * Constructs digit with {@link DigitType}
     * @param type - type of current digit
     */
    public Digit(DigitType type){
        this.type = type;
    }

    /**
     * Getter for value of current digit type
     * @return String value of digit's type
     */
    public String getValue(){
        return type.getValue();
    }
}
