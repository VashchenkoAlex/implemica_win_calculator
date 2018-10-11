package win_calculator.view.enums;

/**
 * Enum class with possibles options for Menu's list and values for them
 */
public enum MenuListOption {

   /**
    * Not clickable Calculator option for dropdown menu list
    */
   CALCULATOR("Calculator", false),
   /**
    * Clickable Standard option for dropdown menu list
    */
   STANDARD("\uE1D0   Standard", true),
   /**
    * Clickable Scientific option for dropdown menu list
    */
   SCIENTIFIC("      Scientific", true),
   /**
    * Clickable Programmer option for dropdown menu list
    */
   PROGRAMMER("‹/›  Programmer", true),
   /**
    * Clickable Date Calculation option for dropdown menu list
    */
   DATE_CALCULATION("\uED28   Date Calculation", true),
   /**
    * Not clickable Converter option for dropdown menu list
    */
   CONVERTER("Converter", false),
   /**
    * Clickable Currency option for dropdown menu list
    */
   CURRENCY("      Currency", true),
   /**
    * Clickable Volume option for dropdown menu list
    */
   VOLUME("\uF158   Volume", true),
   /**
    * Clickable Length option for dropdown menu list
    */
   LENGTH("\uECC6   Length", true),
   /**
    * Clickable Weight and mass option for dropdown menu list
    */
   WEIGHT_AND_MASS("       Weight and Mass", true),
   /**
    * Clickable Temperature option for dropdown menu list
    */
   TEMPERATURE("\uE9CA   Temperature", true),
   /**
    * Clickable Energy option for dropdown menu list
    */
   ENERGY("\uECAD   Energy", true),
   /**
    * Clickable Area option for dropdown menu list
    */
   AREA("\uE809   Area", true),
   /**
    * Clickable Speed option for dropdown menu list
    */
   SPEED("\uD83C\uDFC3   Speed", true),
   /**
    * Clickable Time option for dropdown menu list
    */
   TIME("\uED5A   Time", true),
   /**
    * Clickable Power option for dropdown menu list
    */
   POWER("\uE945   Power", true),
   /**
    * Clickable Data option for dropdown menu list
    */
   DATA("\uE7F1   Data", true),
   /**
    * Clickable Pressure option for dropdown menu list
    */
   PRESSURE("\uEC4A   Pressure", true),
   /**
    * Clickable Angle option for dropdown menu list
    */
   ANGLE("∠   Angle", true);

   /**
    * Stores text label for current label
    */
   private String label;
   /**
    * Flag: is current option clickable
    */
   private boolean isOption;

   /**
    * Constructs enum instance with given
    *
    * @param label    - String value of text at option
    * @param isOption - flag: is given option clickable
    */
   MenuListOption(String label, boolean isOption) {
      this.label = label;
      this.isOption = isOption;
   }

   public String getLabel() {
      return this.label;
   }

   public boolean isOption() {
      return this.isOption;
   }
}
