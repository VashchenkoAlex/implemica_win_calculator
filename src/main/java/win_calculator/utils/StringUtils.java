package win_calculator.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class StringUtils {

    private static final String COMA = ",";
    private static final String DOT = ".";
    private static final String MINUS = "-";
    private static final String BRACKET = " )";
    private static final String SPACE = "  ";
    private static final int DIGITS = 3;
    private static final String ADD_EXTRA_OPERATION_REGEX = "^.+[\\s{2}][^\\s{2}]+$";
    private static final String CAPACITY_REGEX = "(?<=\\G.{3})";
    private static final String FIND_ZERO_E_REGEX = "^0E-[0-9]+";
    private static final String ARE_ZEROS_FIRST_REGEX = "^0.0[0-9]+";
    private static final String IS_ZERO_FIRST_REGEX = "^0.+";
    private static final String BASIC_FORMAT_PATTERN = "#############,###.###############";
    private static final String E_PART_OF_FORMAT = "#E0";
    private static final String SIXTEEN_DECIMAL_PART = "#.################";
    private static final String FOURTEEN_DECIMAL_PART = "#.##############";

    private static final String E = "E";

    private static String replaceDotToComa(String string) {
        return string.replace(".", ",");
    }

    private static String replaceComaToDot(String string) {
        return string.replace(",", ".");
    }

    public static String addCapacity(String currentStr) {

        String result = "";
        if (!currentStr.contains("e") && !currentStr.matches("^0,[0-9]+")) {
            ArrayList<String> stringParts = cutMinus(currentStr);
            stringParts.addAll(splitByComa(stringParts.get(1)));
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
            result = stringParts.get(0) + result;
        } else {
            result = currentStr;
        }
        return result;
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

    private static String removeCapacity(String currentString) {

        return currentString.replaceAll("\\s", "");
    }

    public static boolean isComaAbsent(String number) {

        return !number.contains(COMA);
    }

    private static String cutLastZeros(String current) {

        String result = current;
        if (current.contains(COMA)) {
            result = current.replaceAll("[0]+$", "");
        }
        return result;
    } //TO DO TESTS

    private static String cutLastComa(String currentStr) {
        return currentStr.replaceAll(COMA + "$", "");
    }

    public static String prepareCapacity(String current) {

        return addCapacity(removeCapacity(replaceDotToComa(transformSimpleNumberToE(current))));
    } //TO DO TESTS

    public static String optimizeStringForHistory(String current) {

        String result;

        if (!current.contains(E) && current.length() > 16 && !current.contains(DOT)) {
            result = prepareEPlus(current);
        } else {
            result = cutLastComa(cutLastZeros(replaceDotToComa(transformSimpleNumberToE(current))));
        }
        return result;
    }

    private static String transformSimpleNumberToE(String current) {

        String result = current;
        if (current.matches(FIND_ZERO_E_REGEX)) {
            result = "0";
        } else if (current.contains("E")) {
            String[] parts = current.split("E");
            parts[0] = cutLastZeros(replaceDotToComa(parts[0]));
            result = parts[0] + "e" + parts[1];
        }
        return result;
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

        StringBuilder resultStr = new StringBuilder();
        if (currentStr.contains(SPACE)) {
            if (currentStr.matches(ADD_EXTRA_OPERATION_REGEX)) {
                String[] parts = currentStr.split("\\s\\s");
                for (int i = 0; i < parts.length - 1; i++) {
                    resultStr.append(parts[i]).append("  ");
                }
                resultStr.append(symbol).append(parts[parts.length - 1]);
            } else {
                resultStr = new StringBuilder(currentStr + symbol);
            }
        } else if (!"".equals(currentStr)) {
            resultStr = new StringBuilder(symbol + currentStr);
        }
        return resultStr + BRACKET;
    }

    public static String addNegateToString(String currentStr, String symbol, String extraSymbol) {

        String resultStr = "";

        return resultStr;
    }

    public static boolean isOverflow(String current, int max) {

        boolean result = false;
        if (current.contains("E")) {
            String[] parts = current.split("E");
            int rate = Integer.parseInt(parts[1]);
            if (rate > max || rate < (0 - max) && !parts[0].equals("0")) {
                result = true;
            }
        }
        return result;
    }

    public static String convertToString(BigDecimal response) {

        String pattern = BASIC_FORMAT_PATTERN;
        int scale = 15;
        String exponentSeparator = "e";
        response = response.setScale(9999, RoundingMode.HALF_UP).stripTrailingZeros();
        String result = response.abs().toString();
        int unscaledLength = response.unscaledValue().toString().length();
        if (result.matches(IS_ZERO_FIRST_REGEX)) {
            if (result.matches(ARE_ZEROS_FIRST_REGEX) && isCorrectExponent(response)) {
                pattern = "0." + addDigits(unscaledLength) + E_PART_OF_FORMAT;
                scale += unscaledLength;
            } else {
                ++scale;
                pattern += "#";
            }
        } else if (result.contains(E)) {
            String[] parts = result.split(E);
            int expCount = Math.abs(Integer.parseInt(parts[1]));
            response = response.setScale(15+expCount,RoundingMode.HALF_UP).stripTrailingZeros();
            unscaledLength = response.unscaledValue().toString().length();
            if (parts[1].contains("-")){
                if (expCount > 17 - unscaledLength) {
                    pattern = "0." + addDigits(unscaledLength) + E_PART_OF_FORMAT;
                    scale = scale + expCount;
                    if (unscaledLength < 2) {
                        exponentSeparator = ",e";
                    }
                } else {
                    pattern = BASIC_FORMAT_PATTERN+"#";
                    ++scale;
                }
            }else if (parts[1].contains("+")){
                int roundLength = response.abs().toBigInteger().toString().length();
                if (roundLength>16){
                    pattern = "0." + addDigits(unscaledLength) + E_PART_OF_FORMAT;
                    scale = scale + expCount;
                    if (unscaledLength<2){
                        exponentSeparator = ",e+";
                    }
                }else {
                    pattern = BASIC_FORMAT_PATTERN+"#";
                    ++scale;
                }
            }

        }
        response = response.setScale(scale, RoundingMode.HALF_UP).stripTrailingZeros();
        unscaledLength = response.unscaledValue().toString().length();
        int count = response.abs().toBigInteger().toString().length();
        if (unscaledLength > 16) {
            if (result.matches(IS_ZERO_FIRST_REGEX)) {
                pattern = SIXTEEN_DECIMAL_PART+E_PART_OF_FORMAT;
            } else if (result.contains(DOT)) {
                pattern = preparePattern(count);
            } else {
                pattern = FOURTEEN_DECIMAL_PART+E_PART_OF_FORMAT;
                exponentSeparator = "e+";
            }
        }
        DecimalFormat format = new DecimalFormat(pattern);
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setExponentSeparator(exponentSeparator);
        format.setDecimalFormatSymbols(symbols);
        result = format.format(response);
        return result;
    }

    private static String prepareEPlus(String number) {

        int count = number.length() - 1;
        BigDecimal resultNumber = new BigDecimal(number).movePointLeft(count);
        return cutLastZeros(replaceDotToComa(resultNumber.setScale(15, RoundingMode.HALF_UP).toString())) + "e+" + count;
    }

    private static String addDigits(int count) {

        String result = "";
        for (int i = 1; i < count; i++) {
            result += "0";
        }
        return result;
    }

    private static String preparePattern(int count){

        String result = "#.###############";
        for (int i = 1; i < count; i++) {
            if (i==3){
                result = "," + result;
            }
            result = "#" + result.substring(0,result.length()-1);
        }
        return result;
    }

    private static boolean isCorrectExponent(BigDecimal number){

        DecimalFormat format = new DecimalFormat("#.#################E0");
        String[] parts = format.format(number).split("E");
        boolean result = false;
        if (parts.length>1){
            result = Integer.parseInt(parts[1]) < -3 && parts[0].length()>16;
        }
        return result;
    }
}
