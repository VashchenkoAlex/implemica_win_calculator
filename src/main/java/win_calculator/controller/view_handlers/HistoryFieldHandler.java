package win_calculator.controller.view_handlers;


import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.Text;

public class HistoryFieldHandler {

    private Label historyField;
    private ScrollPane scroll;

    public void setHistoryField(Label historyField, ScrollPane scroll) {
        this.historyField = historyField;
        scroll.setFitToHeight(true);
        scroll.setFitToWidth(true);
        this.scroll = scroll;
    }

    public void setHistoryText(String text){

        historyField.setText(text);
        showScroll();
    }

    private void showScroll(){

        historyField.layout();
        if (((Text)historyField.lookup(".text")).getText().contains("...")){
            scroll.setFitToWidth(false);
        }
        scroll.layout();
        historyField.layout();
    }

    public void clear(){

        scroll.setFitToWidth(true);
        scroll.layout();
        setHistoryText("");
        historyField.layout();
    }
}
