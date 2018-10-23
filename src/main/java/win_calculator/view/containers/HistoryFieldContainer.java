package win_calculator.view.containers;


import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.Text;

/**
 * Contains display label and provides methods for changing it
 */
public class HistoryFieldContainer {

   /**
    * Constant: class id for history label text at FXApp
    */
   private static final String HISTORY_TEXT_ID = ".text";
   /**
    * Constant: String represents of overrun symbol at history label
    */
   private static final String OVERRUN_SYMBOL = "...";

   /**
    * Instance of history label
    */
   private Label history;
   /**
    * Instance of history scroll
    */
   private ScrollPane scroll;

   /**
    * Sets up history label with scroll
    *
    * @param historyField - given history label
    * @param scroll       - given history scroll
    */
   public void setHistoryField(Label historyField, ScrollPane scroll) {
      this.history = historyField;
      scroll.setFitToHeight(true);
      scroll.setFitToWidth(true);
      this.scroll = scroll;
   }

   /**
    * Sets up given string to the history label and sets up scroll if it's necessary
    *
    * @param text - given string
    */
   public void setHistoryText(String text) {
      history.setText(text);
      showScroll();
   }

   /**
    * Shows scroll of history label if displayed text at history label is overrun
    */
   private void showScroll() {
      history.layout();
      if (isShownTextOverrun()) {
         scroll.setFitToWidth(false);
      }
      scroll.layout();
      history.layout();
   }

   /**
    * Verifies is history label contains overrun symbol
    * @return boolean verification result
    */
   private boolean isShownTextOverrun() {
      String shownText = ((Text) history.lookup(HISTORY_TEXT_ID)).getText();

      return shownText.contains(OVERRUN_SYMBOL);
   }

   /**
    * Cleans history label and hide scroll
    */
   public void clear() {
      scroll.setFitToWidth(true);
      scroll.layout();
      setHistoryText("");
      history.layout();
   }
}






























