package win_calculator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainApp extends Application {


    public static void main(String[] args) throws Exception {
        launch(args);
    }

    public void start(final Stage primaryStage) throws Exception {

        String fxmlFile = "/fxml/calculator.fxml";
        FXMLLoader loader = new FXMLLoader();
        Parent rootNode = loader.load(getClass().getResourceAsStream(fxmlFile));

        Scene scene = new Scene(rootNode, 320, 470);
        scene.getStylesheets().add("/styles/styles.css");

        primaryStage.setTitle("Calculator");
        primaryStage.setScene(scene);
        //primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setMinHeight(470);
        primaryStage.setMinWidth(320);
        primaryStage.setOpacity(0.99F);
        primaryStage.getIcons().add(new Image("/images/icon.jpg"));

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

        primaryStage.show();

    }
}
