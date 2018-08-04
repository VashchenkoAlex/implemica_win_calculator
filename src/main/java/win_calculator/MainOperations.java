package win_calculator;

import java.math.BigDecimal;

import static win_calculator.StringUtils.addSpaces;
import static win_calculator.StringUtils.replaceComaToDot;
import static win_calculator.StringUtils.replaceDotToComa;

abstract class MainOperations {

    private static BigDecimal[] variables = new BigDecimal[3];

    static void addNumber(String numberStr){

        if (variables[0] == null){
            variables[0] = new BigDecimal(numberStr);
        }
        if (variables[1] == null){
            variables[1] = new BigDecimal(numberStr);
        }
    }

    static void doPlus(){

        variables[2] = variables[0].add(variables[1]);
    }

    static void doMinus(){

        variables[2] = variables[0].subtract(variables[1]);
    }

    static void doMultiply(){
        variables[2] = variables[0].multiply(variables[1]);
    }

    static void doDivide(){
        variables[2] = variables[0].divide(variables[1]);
    }

    static boolean isResult(){
        return variables[2]!=null;
    }

    static String getResultString(){
        String resultStr = replaceDotToComa(variables[2].toString());
        if (resultStr.length()>3){
            resultStr = addSpaces(resultStr,0);
        }
        return resultStr;
    }

    static void setEmptyVariables(){
        variables = new BigDecimal[3];
    }

    static void finalizeCalc(MainOperation operation){

    }
}
