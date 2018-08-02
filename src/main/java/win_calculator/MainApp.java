package win_calculator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainApp extends Application {


    public static void main(String[] args) throws Exception {
        launch(args);
    }

    public void start(Stage stage) throws Exception {

        String fxmlFile = "/fxml/calculator.fxml";
        FXMLLoader loader = new FXMLLoader();
        Parent rootNode = loader.load(getClass().getResourceAsStream(fxmlFile));

        Scene scene = new Scene(rootNode, 320, 470);
        scene.getStylesheets().add("/styles/styles.css");

        stage.setTitle("Calculator");
        stage.setScene(scene);
        stage.setMinHeight(470);
        stage.setMinWidth(320);
        //stage.initStyle(StageStyle.TRANSPARENT);
        stage.setOpacity(0.99F);
        stage.getIcons().add(new Image("/images/icon.jpg"));

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

        stage.show();
    }
}
