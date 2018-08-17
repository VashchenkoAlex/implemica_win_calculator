package win_calculator.DTOs;

import java.math.BigDecimal;

public class ResponseDTO {

    private BigDecimal displayNumber;
    private String history;

    public BigDecimal getDisplayNumber() {

        return displayNumber;
    }

    public String getHistory() {
        return history;
    }

    public ResponseDTO(BigDecimal displayNumber, String history) {
        this.displayNumber = displayNumber;
        this.history = history;
    }
}
