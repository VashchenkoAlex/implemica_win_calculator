package win_calculator.model.DTOs;

import win_calculator.model.nodes.actions.Action;

import java.math.BigDecimal;
import java.util.LinkedList;

public class ResponseDTO {

    private BigDecimal display;
    private LinkedList<Action> history;
    private String message = null;

    public ResponseDTO(BigDecimal display, LinkedList<Action> history) {

        this.display = display;
        this.history = history;
    }

    public ResponseDTO(String message,LinkedList<Action> history){

        this.message = message;
        this.history = history;
    }

    public BigDecimal getDisplay() {

        return display;
    }

    public LinkedList<Action> getHistory() {

        return history;
    }

    public String getMessage() {
        return message;
    }
}
