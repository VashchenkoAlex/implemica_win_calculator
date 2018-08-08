package win_calculator;

abstract class StringUtils {

    private static final String COMA = ",";
    private static final int DIGITS = 3;

    static String replaceComaToDot(String string){
        return string.replace(",",".");
    }

    static String replaceDotToComa(String string){
        return string.replace(".",",");
    }

    static String addSpaces(String currentStr,int count){

        String[] stringParts = splitByComa(currentStr);
        String result = "";
        if (stringParts[0].length()>= DIGITS){
            int firstPartIndex = (stringParts[0].length()% DIGITS)+count;
            String firstWholePart = stringParts[0].substring(0,firstPartIndex);
            String secondPart = stringParts[0].substring(firstWholePart.length());
            String[] subStr = secondPart.split("(?<=\\G.{"+ DIGITS +"})");
            for (String str : subStr) {
                result +=" "+str;
            }
            if (stringParts.length>1){
                result += ","+stringParts[1];
            }
            result = firstWholePart+result;
        }else {
            result = currentStr;
        }
        return result;
    }

    private static String[] splitByComa(String string){
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

    static String deleteOneSymbolFromTheEnd(String current){

        String result = optimizeString(current);
        result = result.substring(0,result.length()-1);
        if (result.length()>3){
            result = addSpaces(result,0);
        }
        return result;
    }

    static String cutLastZeros(String current){

        String result = current;
        if (current.contains(",")){
            result = current.replaceAll("[0]+$","");
        }
        return result;
    }

    static String cutLastComa(String currentStr){
        return currentStr.replaceAll(",$","");
    }

    static String optimizeString(String current){

        String result = cutLastComa(cutLastZeros(removeSpaces(replaceDotToComa(current))));
        if (result.length()>DIGITS){
            result = addSpaces(result,0);
        }
        return result;
    }
}
