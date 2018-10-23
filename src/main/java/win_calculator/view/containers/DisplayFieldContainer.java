package win_calculator.view.containers;

import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import static win_calculator.controller.utils.ControllerUtils.isComaAbsent;

/**
 * Contains display label and provides methods for changing it
 */
public class DisplayFieldContainer {

   /**
    * Constant: String represent of symbol coma
    */
   private static final String COMA = ",";
   /**
    * Constant of String pattern for converting BigDecimal number to String for
    * display label
    */
   private static final String DISPLAY_PATTERN = "#############,###.################";
   /**
    * Constant: String represent of digit zero
    */
   private static final String ZERO = "0";
   /**
    * Constant: int value of max possible digits at the display label
    */
   private static final int MAX_DIGITS = 16;
   /**
    * Constant: int value of default font size for the display label
    */
   private static final double DEFAULT_FONT_SIZE = 47.0;
   /**
    * Constant: class id for display label text at FXApp
    */
   private static final String DISPLAY_TEXT_ID = ".text";
   /**
    * Constant: match first zero presents regular expression
    */
   private static final String FIRST_ZERO_STR = "0,";
   /**
    * Constant: match space or coma presents regular expression
    */
   private static final String SPACE_OR_COMA_REGEX = "[ ,]";

   /**
    * Instance of display label
    */
   private Label display;

   public void setDisplay(Label display) {
      this.display = display;
   }

   /**
    * Display label text setter
    * Fixes font size if it's necessary
    *
    * @param string - given string for display
    */
   public void setDisplayedText(String string) {

      display.setText(string);
      fixFontSize();
   }

   /**
    * Adds coma to the current string at display label
    */
   public void addComa() {

      String text = display.getText();

      if (!text.isEmpty() && isComaAbsent(text)) {
         setDisplayedText(text + COMA);
      }
   }

   /**
    * Changes font of display label text depends on correct displaying
    */
   private void fixFontSize() {

      display.setFont(new Font(display.getFont().getName(), DEFAULT_FONT_SIZE));
      display.layout();

      while (isOverrun()) {
         display.setFont(new Font(display.getFont().getName(), display.getFont().getSize() - 1));
         display.layout();
      }
   }

   /**
    * Compares displayed text at window with text that label contains
    *
    * @return true if displayed text equals contained text
    */
   private boolean isOverrun() {

      String shownText = ((Text) display.lookup(DISPLAY_TEXT_ID)).getText();

      return !display.getText().equals(shownText);
   }

   /**
    * Verifies is current number length not max
    *
    * @return true if it is not
    */
   private boolean isNotMax() {

      String displayedText = display.getText();
      displayedText = displayedText.replace(FIRST_ZERO_STR, "");
      displayedText = displayedText.replaceAll(SPACE_OR_COMA_REGEX, "");

      return displayedText.length() < MAX_DIGITS;
   }

   /**
    * Adds given number to display label text in depends on previous operation
    *
    * @param number         - given string value of number
    * @param wasDigitBefore - was digit entered before
    */
   public void sendDigitToDisplay(String number, boolean wasDigitBefore) {
      if (isNotMax() || !wasDigitBefore) {
         setDisplayedText(number);
      }
   }
}























