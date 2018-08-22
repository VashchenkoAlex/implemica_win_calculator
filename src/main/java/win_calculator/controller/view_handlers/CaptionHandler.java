package win_calculator.controller.view_handlers;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class CaptionHandler {

    private static final String FULL = "\uE922";
    private static final String MINIMIZE = "\uE923";
    private Button fullScreenBtn;
    private Stage stage;

    public void setStage(AnchorPane rootPane) {

        stage = (Stage) rootPane.getScene().getWindow();
    }

    public void setFullScreenBtn(Button fullScreen) {

        this.fullScreenBtn = fullScreen;
    }

    public void close(){

        Platform.exit();
    }

    public void hide(){

        stage.toBack();
    }

    public void fullScreen(){

        if (stage.isFullScreen()){
            stage.setFullScreen(false);
            fullScreenBtn.setText(FULL);
        }else {
            stage.setFullScreen(true);
            fullScreenBtn.setText(MINIMIZE);
        }
    }
}
