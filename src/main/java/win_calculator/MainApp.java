package win_calculator;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class MainApp extends Application {

    public static void main(String[] args){
        launch(args);
    }

    public void start(final Stage primaryStage) throws IOException {

        String fxmlFile = "/fxml/calculator.fxml";
        Stage stage = FXMLLoader.load(getClass().getResource(fxmlFile));

        stage.setResizable(true);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.toBack();
        stage.setOpacity(0.95F);
        stage.getIcons().add(new Image("/images/calc_logo.png"));
        stage.setOnCloseRequest(e -> Platform.exit());
        ComponentResizer.addResizeListener(stage);

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

        stage.show();
    }
}
