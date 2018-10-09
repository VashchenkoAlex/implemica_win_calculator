package win_calculator;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import win_calculator.view.handlers.ButtonPressHandler;
import win_calculator.view.handlers.ResizeHandler;

import java.io.IOException;

/**
 * Main class for set up and launch calculator application.
 * Extends JavaFx {@link Application} class
 */
public class WinCalculator extends Application {

    /**
     * Constants
     */
    private static final String FXML_PATH = "/fxml/calculator.fxml";
    private static final String LOGO_PATH = "/images/calc_logo.png";
    private static final float OPACITY = 0.98F;
    private static final int START_X_COORDINATE = 200;
    private static final int START_Y_COORDINATE = 200;

    /**
     * Launch FX application
     * @param args supplied command-line arguments
     */
    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        
        Stage stage = FXMLLoader.load(getClass().getResource(FXML_PATH));
        stage.setResizable(true);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.toBack();
        stage.setOpacity(OPACITY);
        stage.setX(START_X_COORDINATE);
        stage.setY(START_Y_COORDINATE);
        stage.getIcons().add(new Image(LOGO_PATH));
        stage.setOnCloseRequest(e -> Platform.exit());
        ResizeHandler.addResizeListener(stage);
        ButtonPressHandler.addButtonPressListener(stage);
        stage.show();
        stage.toFront();
    }
}
