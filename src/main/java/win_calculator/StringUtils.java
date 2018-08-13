package win_calculator;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class StringUtils {

    private static final String COMA = ",";
    private static final int DIGITS = 3;

    public static String replaceComaToDot(String string){
        return string.replace(",",".");
    }

    static String replaceDotToComa(String string){
        return string.replace(".",",");
    }

    static String addCapacity(String currentStr, int count){

        ArrayList<String> stringParts = cutMinus(currentStr);
        stringParts.addAll(splitByComa(stringParts.get(1)));
        String result = "";
        if (stringParts.size()>3){
            count = 1;
        }
        if (stringParts.get(2).length()>= DIGITS+count){
            int firstPartIndex = (stringParts.get(2).length()% DIGITS)-count+1;
            String firstWholePart = stringParts.get(2).substring(0,firstPartIndex);
            String secondPart = stringParts.get(2).substring(firstWholePart.length());
            String[] subStr = secondPart.split("(?<=\\G.{"+ DIGITS +"})");
            for (String str : subStr) {
                result +=str+" ";
            }
            result = result.substring(0,result.length()-1);
            if (stringParts.size()>3){
                result += ","+stringParts.get(3);
            }
            if (!"".equals(firstWholePart)){
                firstWholePart+=" ";
            }
            result = firstWholePart+result;
        }else {
            result = stringParts.get(1);
        }
        return stringParts.get(0)+result;
    } //TO DO TESTS

    private static ArrayList<String> splitByComa(String string){
        ArrayList<String> result;
        if (string.contains(",")){
            result = new ArrayList<>(Arrays.asList(string.split("[,]")));
        } else {
            result = new ArrayList<>();
            result.add(string);
        }
        return result;
    }

    static String removeCapacity(String currentString){

        return currentString.replaceAll("\\s","");
    }

    static boolean isComaAbsent(String string){
        return !string.contains(COMA);
    }

    static String deleteOneSymbolFromTheEndOf(String current){

        String result = current.substring(0,current.length()-1);
        if (result.length()>3){
            result = addCapacity(result,1);
        }
        return result;
    } //TO DO TESTS

    private static String cutLastZeros(String current){

        String result = current;
        if (current.contains(",")){
            result = current.replaceAll("[0]+$","");
        }
        return result;
    } //TO DO TESTS

    private static String cutLastComa(String currentStr){
        return currentStr.replaceAll(",$","");
    }

    public static String optimizeString(String current){

        String result = cutLastComa(cutLastZeros(removeCapacity(replaceDotToComa(current))));
        result = addCapacity(result,1);
        return result;
    } //TO DO TESTS

    private static ArrayList<String> cutMinus(String currentStr){

        ArrayList<String> result = new ArrayList<String>(){{add("");add(currentStr);}};
        if (currentStr.contains("-")) {
            result.set(0,"-");
            result.set(1,currentStr.substring(1));
        }
        return result;
    }
}
