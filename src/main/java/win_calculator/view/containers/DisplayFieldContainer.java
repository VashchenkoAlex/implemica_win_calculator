package win_calculator.view.containers;

import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.regex.Pattern;

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
    * Constant: match first zero presents pattern
    */
   private static final Pattern FIRST_ZERO_PATTERN = Pattern.compile("0,");
   /**
    * Constant: match space or coma presents pattern
    */
   private static final Pattern SPACE_OR_COMA_PATTERN = Pattern.compile("[ ,]");

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
   public void addComma() {
      String text = display.getText();
      if (!text.isEmpty() && isComaAbsent(text)) {
         setDisplayedText(text + COMA);
      }
   }

   /**
    * Changes font of display label text depends on correct displaying
    */
   private void fixFontSize() {
      String fontName = display.getFont().getName();
      display.setFont(new Font(fontName, DEFAULT_FONT_SIZE));
      display.layout();

      while (isOverrun()) {
         display.setFont(new Font(fontName, display.getFont().getSize() - 1));
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
   //fixed to Pattern
   private boolean isNotMax() {
      String displayedText = display.getText();
      displayedText = FIRST_ZERO_PATTERN.matcher(displayedText).replaceFirst("");
      displayedText = SPACE_OR_COMA_PATTERN.matcher(displayedText).replaceAll("");

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























