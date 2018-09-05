package win_calculator.DTOs;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

import static win_calculator.utils.StringUtils.*;

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

        return history;
    }

    public ResponseDTO(BigDecimal displayNumber, String history) {
        this.displayNumber = displayNumber;
        this.history = history;
    }

    public void setDisplayNumber(BigDecimal displayNumber) {
        this.displayNumber = displayNumber;
    }

    public void setHistory(String history) {
        this.history = history;
    }
}
