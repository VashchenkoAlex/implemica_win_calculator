package win_calculator.controller.utils;

public abstract class ControllerUtils {

    private static final String COMA = ",";
    private static final String DOT = ".";
    private static final String IS_TEXT_REGEX = "^[^0-9]+";
    private static final String MINUS_REGEX = "^-.[0-9,]*";
    private static final String MINUS = "-";

    public static boolean isComaAbsent(String number) {

        return !number.contains(COMA);
    }


    public static String replaceDotToComa(String string){

        return string.replace(DOT,COMA);
    }

    public static String replaceComaToDot(String string){

        return string.replace(COMA,DOT);
    }


    public static boolean isNotNumber(String string){

        return string.matches(IS_TEXT_REGEX);
    }

    public static String addCapacity(String string) {

        String[] parts = {string, ""};
        String minus = "";
        String coma = "";
        if (string.matches(MINUS_REGEX)) {
            minus = MINUS;
            parts[0] = string.substring(1);
        }
        if (parts[0].contains(COMA)) {
            coma = COMA;
            parts = parts[0].split(COMA);
        }
        parts[0] = String.format("%,d", Long.parseLong(parts[0].replaceAll(" ", "")));
        String result;
        if (parts.length>1){
            result = minus + parts[0] + coma + parts[1];
        }else {
            result = minus + parts[0] + coma;
        }
        return result;
    }

    public static String replaceCapacity(String string){

        return string.replaceAll(" ","");
    }

}
