package win_calculator.model.DTOs;

import win_calculator.model.nodes.events.Event;

import java.math.BigDecimal;
import java.util.LinkedList;

public class ResponseDTO {

    private BigDecimal display;
    private LinkedList<Event> history;
    private String message = null;

    public ResponseDTO(BigDecimal display, LinkedList<Event> history) {

        this.display = display;
        this.history = history;
    }

    public ResponseDTO(String message,LinkedList<Event> history){

        this.message = message;
        this.history = history;
    }

    public BigDecimal getDisplay() {

        return display;
    }

    public LinkedList<Event> getHistory() {

        return history;
    }

    public String getMessage() {
        return message;
    }
}
