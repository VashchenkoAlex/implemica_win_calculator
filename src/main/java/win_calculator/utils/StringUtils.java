package win_calculator.utils;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class StringUtils {

    private static final String COMA = ",";
    private static final String MINUS = "-";
    private static final String BRACKET = " )";
    private static final String SPACE = "  ";
    private static final int DIGITS = 3;
    private static final String REGEXP = "^.+[\\s{2}][^\\s{2}]+$";
    private static final String CAPACITY_REGEX = "(?<=\\G.{3})";

    public static String replaceComaToDot(String string) {
        return string.replace(",", ".");
    }

    public static String replaceDotToComa(String string) {
        return string.replace(".", ",");
    }

    public static String addCapacity(String currentStr) {

        ArrayList<String> stringParts = cutMinus(currentStr);
        stringParts.addAll(splitByComa(stringParts.get(1)));
        String result = "";
        if (stringParts.get(3).length() >= DIGITS + 1) {
            int firstPartIndex = (stringParts.get(3).length() % DIGITS);
            String firstWholePart = stringParts.get(3).substring(0, firstPartIndex);
            String secondPart = stringParts.get(3).substring(firstWholePart.length());
            String[] subStr = secondPart.split(CAPACITY_REGEX);
            for (String str : subStr) {
                result += str + " ";
            }
            result = result.substring(0, result.length() - 1) + stringParts.get(2);
            if (stringParts.size() > 4) {
                result += stringParts.get(4);
            }
            if (!"".equals(firstWholePart)) {
                firstWholePart += " ";
            }
            result = firstWholePart + result;
        } else {
            result = stringParts.get(1);
        }
        return stringParts.get(0) + result;
    }

    private static ArrayList<String> splitByComa(String string) {
        ArrayList<String> result = new ArrayList<>();
        if (string.contains(COMA)) {
            result.add(COMA);
            result.addAll(Arrays.asList(string.split("[,]")));
        } else {
            result.add("");
            result.add(string);
        }
        return result;
    }

    public static String removeCapacity(String currentString) {

        return currentString.replaceAll("\\s", "");
    }

    public static boolean isComaAbsent(String number) {

        return !number.contains(COMA);
    }

    public static String cutLastZeros(String current) {

        String result = current;
        if (current.contains(COMA)) {
            result = current.replaceAll("[0]+$", "");
        }
        return result;
    } //TO DO TESTS

    public static String cutLastComa(String currentStr) {
        return currentStr.replaceAll(COMA + "$", "");
    }

    public static String optimizeString(String current) {

        String result;
        if (current.contains("0E-")) {
            result = "0";
        } else {
            result = removeCapacity(replaceDotToComa(current));
            result = addCapacity(result);
        }
        return result;
    } //TO DO TESTS

    public static String prepareScaling(String current) {

        String result = cutLastComa(cutLastZeros(replaceDotToComa(current)));
        if (result.contains(COMA)&&result.length()>17) {
            String[] resultParts = result.split(COMA);
            int wholeCount = 17 - resultParts[0].length();
            if ("0".equals(resultParts[0])) {
                ++wholeCount;
            }else if ("-0".equals(resultParts[0])){
                wholeCount += 2;
            }else if (resultParts[0].contains(MINUS)){
                ++wholeCount;
            }
            resultParts[1] = resultParts[1].substring(0,wholeCount);
            resultParts[1] = round(resultParts[1]);
            result = resultParts[0] + COMA + resultParts[1];
        }
        return result;
    }

    public static String optimizeStringWithComaAndZero(String string) {

        return addCapacity(removeCapacity(string));
    }

    private static ArrayList<String> cutMinus(String currentStr) {

        ArrayList<String> result = new ArrayList<String>() {{
            add("");
            add(currentStr);
        }};
        if (currentStr.contains("-")) {
            result.set(0, "-");
            result.set(1, currentStr.substring(1));
        }
        return result;
    }

    public static String addExtraOperationToString(String currentStr, String symbol) {

        String resultStr = "";
        if (currentStr.contains(SPACE)) {
            if (currentStr.matches(REGEXP)) {
                String[] parts = currentStr.split("\\s\\s");
                for (int i = 0; i < parts.length - 1; i++) {
                    resultStr += parts[i] + "  ";
                }
                resultStr = resultStr + symbol + parts[parts.length - 1];
            } else {
                resultStr = currentStr + symbol;
            }
        } else if (!"".equals(currentStr)) {
            resultStr = symbol + currentStr;
        }
        return resultStr + BRACKET;
    }

    public static String addNegateToString(String currentStr, String symbol, String extraSymbol){

        String resultStr = "";

        return resultStr;
    }

    private static String round(String current){

        int lastDigit = Integer.parseInt(current.substring(current.length()-1));
        String result = current.substring(0,current.length()-1);
        if (lastDigit>4){
            int roundedDigit = Integer.parseInt(result.substring(result.length()-1));
            ++roundedDigit;
            result = result.substring(0,result.length()-1) + roundedDigit;
        }

        return result;
    }
}
