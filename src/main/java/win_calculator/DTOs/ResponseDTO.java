package win_calculator.DTOs;

import java.math.BigDecimal;

import static win_calculator.utils.StringUtils.cutLastComa;
import static win_calculator.utils.StringUtils.cutLastZeros;
import static win_calculator.utils.StringUtils.optimizeString;

public class ResponseDTO {

    private BigDecimal displayNumber;
    private String history;

    public String getDisplayNumber() {

        String result = null;
        if (displayNumber!=null){
            result = cutLastComa(cutLastZeros(optimizeString(displayNumber.toString())));
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
}
