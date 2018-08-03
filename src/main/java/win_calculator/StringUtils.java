package win_calculator;

abstract class StringUtils {

    private static final String COMA = ",";

    static String replaceComaToDot(String string){
        return string.replace(",",".");
    }

    static String replaceDotToComa(String string){
        return string.replace(".",",");
    }

    static String addSpaces(String currentStr,int count){

        String[] stringParts = splitByComa(currentStr);
        int firstPartIndex = (stringParts[0].length()%3)+count;
        String firstWholePart = stringParts[0].substring(0,firstPartIndex);
        String secondPart = stringParts[0].substring(firstWholePart.length());
        String[] subStr = secondPart.split("(?<=\\G.{3})");
        String result = "";
        for (String str : subStr) {
            result +=" "+str;
        }
        if (stringParts.length>1){
            result += ","+stringParts[1];
        }
        return firstWholePart+result;
    }

    static String[] splitByComa(String string){
        String[] result;
        if (string.contains(",")){
            result = string.split("[,]");
        } else {
            result = new String[]{string};
        }
        return result;
    }

    static String removeSpaces(String currentString){
        return currentString.replaceAll("\\s","");
    }

    static boolean isComaAbsent(String string){
        return !string.contains(COMA);
    }

    static String deleteOneSymbolFromTheEnd(String currentStr){
        String result = removeSpaces(currentStr);
        result = result.substring(0,result.length()-1);
        if (result.length()>3){
            result = addSpaces(result,0);
        }
        return result;
    }
}
