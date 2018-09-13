package win_calculator.controller.view_handlers;


import javafx.scene.control.Label;

public class HistoryFieldHandler {

    private Label historyField;
    private String lastValue;

    public void setHistoryField(Label historyField) {
        this.historyField = historyField;
    }

    public void setHistoryText(String text){

        lastValue = text;
        historyField.setText(text);
    }

    public String getLastValue() {
        return lastValue;
    }
}
