package win_calculator.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class StringUtils {

    private static final String COMA = ",";
    private static final String DOT = ".";
    private static final String BRACKET = " )";
    private static final String SPACE = "  ";
    private static final int DIGITS = 3;
    private static final String REGEXP = "^.+[\\s{2}][^\\s{2}]+$";

    public static String replaceComaToDot(String string){
        return string.replace(",",".");
    }

    public static String replaceDotToComa(String string){
        return string.replace(".",",");
    }

    public static String addCapacity(String currentStr){

        ArrayList<String> stringParts = cutMinus(currentStr);
        stringParts.addAll(splitByComa(stringParts.get(1)));
        String result = "";
        if (stringParts.get(3).length()>= DIGITS+1){
            int firstPartIndex = (stringParts.get(3).length()% DIGITS);
            String firstWholePart = stringParts.get(3).substring(0,firstPartIndex);
            String secondPart = stringParts.get(3).substring(firstWholePart.length());
            String[] subStr = secondPart.split("(?<=\\G.{"+ DIGITS +"})");
            for (String str : subStr) {
                result +=str+" ";
            }
            result = result.substring(0,result.length()-1) + stringParts.get(2);
            if (stringParts.size()>4){
                result += stringParts.get(4);
            }
            if (!"".equals(firstWholePart)){
                firstWholePart+=" ";
            }
            result = firstWholePart+result;
        }else {
            result = stringParts.get(1);
        }
        return stringParts.get(0)+result;
    }

    private static ArrayList<String> splitByComa(String string){
        ArrayList<String> result = new ArrayList<>();
        if (string.contains(",")){
            result.add(",");
            result.addAll(Arrays.asList(string.split("[,]")));
        } else {
            result.add("");
            result.add(string);
        }
        return result;
    }

    public static String removeCapacity(String currentString){

        return currentString.replaceAll("\\s","");
    }

    public static boolean isComaAbsent(String number){

        return !number.contains(COMA);
    }

    /*public static boolean isDotAbsent(BigDecimal number){

        boolean re = true;
        if (number!=null){
            re = !number.toString().contains(DOT);
        }
        return re;
    }

    public static boolean isZeroNotFirst(BigDecimal number){

        return !number.toString().matches("^0");
    }
    public static String deleteOneSymbolFromTheEndOf(String current){

        String result = current.substring(0,current.length()-1);
        if (result.length()>3){
            result = addCapacity(result);
        }
        return result;
    } //TO DO TESTS*/

    public static String cutLastZeros(String current){

        String result = current;
        if (current.contains(",")){
            result = current.replaceAll("[0]+$","");
        }
        return result;
    } //TO DO TESTS

    public static String cutLastComa(String currentStr){
        return currentStr.replaceAll(",$","");
    }

    public static String optimizeString(String current){

        String result;
        if (current.matches("[a-zA-Z]+")||"0E-16".equals(current)){
            if ("0E-16".equals(current)){
                result = "0";
            }else {
                result = current;
            }
        }else {
            result = removeCapacity(replaceDotToComa(current));
            result = addCapacity(result);
        }
        return result;
    } //TO DO TESTS

    public static String optimizeStringWithComaAndZero(String string){

        return addCapacity(removeCapacity(string));
    }

    private static ArrayList<String> cutMinus(String currentStr){

        ArrayList<String> result = new ArrayList<String>(){{add("");add(currentStr);}};
        if (currentStr.contains("-")) {
            result.set(0,"-");
            result.set(1,currentStr.substring(1));
        }
        return result;
    }

    public static String addExtraOperationToString(String currentStr,String symbol){

        String resultStr = "";
        if (currentStr.contains(SPACE)){
            if (currentStr.matches(REGEXP)){
                String[] parts = currentStr.split("\\s\\s");
                for (int i = 0;i < parts.length-1;i++){
                    resultStr += parts[i]+"  ";
                }
                resultStr = resultStr+symbol+parts[parts.length-1];
            }else {
                resultStr = currentStr+symbol;
            }
        }else if (!"".equals(currentStr)){
            resultStr = symbol+currentStr;
        }
        return resultStr+BRACKET;
    }
}
