package win_calculator.controller.entities;

/**
 * Enumerate possible symbol with String representation for {@link NumberSymbol}
 */
public enum Symbol {

   /**
    * One with String representation, marker for {@link NumberSymbol}
    */
   ONE("1"),
   /**
    * Two with String representation, marker for {@link NumberSymbol}
    */
   TWO("2"),
   /**
    * Three with String representation, marker for {@link NumberSymbol}
    */
   THREE("3"),
   /**
    * Four with String representation, marker for {@link NumberSymbol}
    */
   FOUR("4"),
   /**
    * Five with String representation, marker for {@link NumberSymbol}
    */
   FIVE("5"),
   /**
    * Six with String representation, marker for {@link NumberSymbol}
    */
   SIX("6"),
   /**
    * Seven with String representation, marker for {@link NumberSymbol}
    */
   SEVEN("7"),
   /**
    * Eight with String representation, marker for {@link NumberSymbol}
    */
   EIGHT("8"),
   /**
    * Nine with String representation, marker for {@link NumberSymbol}
    */
   NINE("9"),
   /**
    * Zero with String representation, marker for {@link NumberSymbol}
    */
   ZERO("0"),
   /**
    * Separator with String representation, marker for {@link NumberSymbol}
    */
   SEPARATOR(".");

   private String string;

   Symbol(String string) {
      this.string = string;
   }

   public String getString() {
      return string;
   }
}
