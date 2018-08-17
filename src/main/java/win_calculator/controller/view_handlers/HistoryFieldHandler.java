package win_calculator.controller.view_handlers;


import javafx.scene.control.Label;

public class HistoryFieldHandler {

    private Label historyField;

    public void setHistoryField(Label historyField) {
        this.historyField = historyField;
    }

    public void setHistoryText(String text){

        historyField.setText(text);
    }
}
