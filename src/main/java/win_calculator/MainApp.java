package win_calculator;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import win_calculator.controller.ButtonPressHandler;
import win_calculator.controller.view_handlers.*;

import java.io.IOException;

public class MainApp extends Application {

    private static Stage stage;
    public static void main(String[] args){
        launch(args);
    }

    public void start(final Stage primaryStage) throws IOException {

        setUpApp();
    }

    public void setUpApp() throws IOException {

        String fxmlFile = "/fxml/calculator.fxml";
        stage = FXMLLoader.load(getClass().getResource(fxmlFile));

        stage.setResizable(true);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.toBack();
        stage.setOpacity(0.98F);
        stage.setX(200);
        stage.setY(200);
        stage.getIcons().add(new Image("/images/calc_logo.png"));
        stage.setOnCloseRequest(e -> Platform.exit());
        ResizeHandler.addResizeListener(stage);
        ButtonPressHandler.addButtonPressListener(stage);
        stage.show();
        stage.toFront();
    }

    public Stage getStage() {
        return stage;
    }
}
