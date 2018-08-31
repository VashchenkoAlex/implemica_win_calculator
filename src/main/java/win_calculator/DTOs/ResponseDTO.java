package win_calculator.DTOs;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

import static win_calculator.utils.StringUtils.*;

public class ResponseDTO {

    private BigDecimal displayNumber;
    private String history;
    private static final String E = "E";

    public String getDisplayNumber() {

        String result = null;
        if (displayNumber!=null){
            BigDecimal alterableNum = displayNumber.setScale(9999,RoundingMode.HALF_UP);
            result = alterableNum.toString();
            if (result.contains(E)){
                if (isResultPlaceValid(result)){
                    result = transformE(result);
                }else {
                    result = String.format("%.16f",displayNumber);
                    result = prepareCapacity(prepareScaling(result));
                }
            }else {
                result = displayNumber.setScale(17,RoundingMode.HALF_UP).toString();
                result = prepareCapacity(prepareScaling(result));
            }
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
