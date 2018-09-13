package win_calculator.DTOs;

import java.math.BigDecimal;

public class ResponseDTO {

    private BigDecimal displayNumber;
    private String history;

    public BigDecimal getDisplayNumber() {

        BigDecimal result = displayNumber;
        if (result==null){
            result = BigDecimal.ZERO;
        }
        return result;
    }

    public String getHistory() {

        if (history == null){
            history = "";
        }
        return history;
    }

    public ResponseDTO(BigDecimal displayNumber, String history) {
        this.displayNumber = displayNumber;
        this.history = history;
    }

    public void setDisplayNumber(BigDecimal displayNumber) {
        this.displayNumber = displayNumber;
    }
}
