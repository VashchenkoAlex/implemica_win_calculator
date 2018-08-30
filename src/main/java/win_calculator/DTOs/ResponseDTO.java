package win_calculator.DTOs;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

import static win_calculator.utils.StringUtils.*;

public class ResponseDTO {

    private BigDecimal displayNumber;
    private String history;

    public String getDisplayNumber() {

        String result = null;
        if (displayNumber!=null){
            setCorrectScale();
            displayNumber = displayNumber.setScale(17,RoundingMode.HALF_UP);
            result = optimizeString(prepareScaling(displayNumber.toString()));
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

    private void setCorrectScale(){


    }
}
