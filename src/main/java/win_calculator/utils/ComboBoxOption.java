package win_calculator.utils;

public enum ComboBoxOption {

    CALCULATOR("Calculator","NOT_OPTION"),
    STANDARD("\uE1D0  Standard","OPTION"),
    SCIENTIFIC("Scientific","OPTION"),
    PROGRAMMER("\uF121  Programmer","OPTION"),
    DATE_CALCULATION("\uED28  Date Calculation","OPTION"),
    CONVERTER("Converter","NOT_OPTION"),
    CURRENCY("Currency","OPTION"),
    VOLUME("\uF158  Volume","OPTION"),
    LENGTH("\uECC6  Length","OPTION"),
    WEIGHT_AND_MASS("Weight and Mass","OPTION"),
    TEMPERATURE("\uE9CA  Temperature","OPTION"),
    ENERGY("\uECAD  Energy","OPTION"),
    AREA("\uE809  Area","OPTION"),
    SPEED("\uD83C\uDFC3  Speed","OPTION"),
    TIME("\uED5A  Time","OPTION"),
    POWER("\uE945  Power","OPTION"),
    DATA("\uE7F1  Data","OPTION"),
    PRESSURE("\uEC4A  Pressure","OPTION"),
    ANGLE("∠  Angle","OPTION");

    private String label;
    private String isOption;

    ComboBoxOption(String label, String isOption){
        this.label = label;
        this.isOption = isOption;
    }

    public String getLabel(){
        return this.label;
    }

    public String isOption(){
        return this.isOption;
    }
}
