package win_calculator.DTOs;

import java.math.BigDecimal;

import static win_calculator.utils.AppUtils.convertToString;

public class ResponseDTO {

    private BigDecimal displayNumber;
    private String history;
    private static final String DISPLAY_PATTERN = "#############,###.################";

    public String getDisplayNumber() {

        String result;
        if (displayNumber == null){
            result = "0";
        }else {
            result = convertToString(displayNumber,DISPLAY_PATTERN);
        }
        return result;
    }

    public BigDecimal getBigdecimalNumber(){

        return displayNumber;
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
}
