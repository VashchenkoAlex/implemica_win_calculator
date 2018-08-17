package win_calculator;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import win_calculator.model.AppModel;
import win_calculator.controller.view_handlers.*;
import win_calculator.controller.view_handlers.DisplayHandler;

import java.io.IOException;

public class MainApp extends Application {

    public static void main(String[] args){
        launch(args);
    }

    public void start(final Stage primaryStage) throws IOException {

        setUpApp();
    }

    public void setUpApp() throws IOException {

        String fxmlFile = "/fxml/calculator.fxml";
        Stage stage = FXMLLoader.load(getClass().getResource(fxmlFile));

        stage.setResizable(true);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.toBack();
        stage.setOpacity(0.98F);
        stage.getIcons().add(new Image("/images/calc_logo.png"));
        stage.setOnCloseRequest(e -> Platform.exit());
        ResizeHandler.addResizeListener(stage);
        stage.show();
        stage.toFront();
    }

    //System.out.println("\uE700" + " Dropdown menu");
    //System.out.println("\uE947" + " Calculator multiply");
    //System.out.println("\uE948" + " Calculator addition");
    //System.out.println("\uE949" + " Calculator subtract");
    //System.out.println("\uE94A" + " Calculator Divide");
    //System.out.println("\uE94B" + " Calculator Squareroot");
    //System.out.println("\uE94C" + " Calculator percent");
    //System.out.println("\uE94D" + " Calculator negate");
    //System.out.println("\uE94E" + " Calculator equalTo");
    //System.out.println("\uE94F" + " Calculator Backspace");
    //System.out.println("\uE81C" + " Calculator History");

    //System.out.println(" - minimize");
    //System.out.println("9,999999999999376e+9999 is MAX for CALC");
    //System.out.println("9,999999999999376e+9999 ++ show -> Overflow -> 0");
}
