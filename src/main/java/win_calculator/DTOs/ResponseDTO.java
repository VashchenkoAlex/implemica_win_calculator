package win_calculator.DTOs;

public class ResponseDTO {

    private String displayNumber;
    private String history;

    public String getDisplayNumber() {

        return displayNumber;
    }

    public String getHistory() {
        return history;
    }

    public ResponseDTO(String displayNumber, String history) {
        this.displayNumber = displayNumber;
        this.history = history;
    }
}
