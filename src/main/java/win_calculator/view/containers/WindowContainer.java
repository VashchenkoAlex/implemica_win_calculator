package win_calculator.view.containers;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Stores window of current FXApp and provides methods for changing it
 */
public class WindowContainer {

   /**
    * Constant: maximize screen symbol at full screen button
    */
   private static final String FULL_SCREEN_SYMBOL = "\uE922";
   /**
    * Constant: minimize screen symbol at full screen button
    */
   private static final String MINIMIZE_SCREEN_SYMBOL = "\uE923";

   /**
    * Constant: window x coordinate by default
    */
   private static final double defaultX = 200;
   /**
    * Constant: window y coordinate by default
    */
   private static final double defaultY = 200;
   /**
    * Instance of current class, using for exit() method test
    */
   public static WindowContainer instance = new WindowContainer();
   /**
    * Instance of full screen button
    */
   private Button fullScreenBtn;
   /**
    * Instance of current window of FXApp
    */
   private Stage stage;

   /**
    * Window setter
    *
    * @param rootPane - given root node of FXApp
    */
   public void setStage(AnchorPane rootPane) {
      stage = (Stage) rootPane.getScene().getWindow();
   }

   /**
    * Full screen button setter
    *
    * @param fullScreen - given Full screen button
    */
   public void setFullScreenBtn(Button fullScreen) {
      this.fullScreenBtn = fullScreen;
   }

   /**
    * Closes the app
    */
   public void close() {
      Platform.exit();
   }

   /**
    * Hides current window
    */
   public void hide() {
      stage.setIconified(true);
   }

   /**
    * Changes maximized status of current window
    */
   public void fullScreen() {
      if (stage.isMaximized()) {
         stage.setY(defaultY);
         stage.setX(defaultX);
         stage.setMaximized(false);
         fullScreenBtn.setText(FULL_SCREEN_SYMBOL);
      } else {
         stage.setX(0);
         stage.setY(0);
         stage.setMaximized(true);
         fullScreenBtn.setText(MINIMIZE_SCREEN_SYMBOL);
      }
   }

   public static WindowContainer getInstance() {
      return instance;
   }
}











