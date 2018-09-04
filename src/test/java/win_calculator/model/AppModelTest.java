package win_calculator.model;

import org.junit.jupiter.api.Test;
import win_calculator.DTOs.ResponseDTO;
import win_calculator.controller.FXMLViewController;
import win_calculator.controller.nodes.digits.*;
import win_calculator.exceptions.MyException;
import win_calculator.model.nodes.actions.Action;
import win_calculator.model.nodes.actions.clear.BaskSpace;
import win_calculator.model.nodes.actions.clear.Clear;
import win_calculator.model.nodes.actions.clear.ClearDisplay;
import win_calculator.model.nodes.actions.enter.Enter;
import win_calculator.model.nodes.actions.extra_operations.*;
import win_calculator.model.nodes.actions.main_operations.*;

import java.util.HashMap;

import static org.junit.gen5.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import static win_calculator.utils.StringUtils.convertToString;

class AppModelTest {

    private FXMLViewController controller = new FXMLViewController();
    private AppModel appModel = new AppModel();
    private static final HashMap<String,Action> actions = createMap();
    private static final String DISPLAY_PATTERN = "#############,###.###############";
    private static HashMap<String,Action> createMap(){

        HashMap<String,Action> map = new HashMap<>();
        map.put("0",new ZeroDigit());
        map.put("1",new OneDigit());
        map.put("2",new TwoDigit());
        map.put("3",new ThreeDigit());
        map.put("4",new FourDigit());
        map.put("5",new FiveDigit());
        map.put("6",new SixDigit());
        map.put("7",new SevenDigit());
        map.put("8",new EightDigit());
        map.put("9",new NineDigit());
        map.put(",",new Coma());
        map.put("+",new Plus());
        map.put("-",new Minus());
        map.put("*",new Multiply());
        map.put("/",new Divide());
        map.put("%",new Percent());
        map.put("sqrt", new Sqrt());
        map.put("sqr",new Sqr());
        map.put("1/x",new Fraction());
        map.put("CE",new ClearDisplay());
        map.put("C",new Clear());
        map.put("<-",new BaskSpace());
        map.put("=",new Enter());
        map.put("neg",new Negate());
        return map;
    }

    @Test
    void testMainActionOnce() throws MyException {

        test("+ =","0","");
        test("+ 2 =","2","");

        test("2 + 2 =","4","");
        test("2 - 2 =","0","");
        test("2 * 2 =","4","");
        test("2 / 2 =","1","");

        test("2 + 2 = +","4","4  +  ");
        test("2 - 2 = -","0","0  -  ");
        test("2 * 2 = *","4","4  ×  ");
        test("2 / 2 = /","1","1  ÷  ");

        test("0 + = = =","0","");
        test("1 + = = =","4","");
        test("2 + = = =","8","");
        test("13 + = = =","52","");
        test("9999 + = = =","39 996","");

        test("0 - = = =","0","");
        test("1 - = = =","-2","");
        test("2 - = = =","-4","");
        test("13 - = = =","-26","");
        test("9999 - = = =","-19 998","");

        test("0 * = = =","0","");
        test("1 * = = =","1","");
        test("2 * = = =","16","");
        test("13 * = = =","28 561","");
        test("9999 * = = =","9 996 000 599 960 001","");

        test("1 / = = =","1","");
        test("2 / = = =","0,25","");
        test("13 / = = =","0,0059171597633136","");

        test("0,2 + 0,2 =","0,4","");
        test("0,2 neg + 0,2 =","0","");
    }

    @Test
    void testMainActionTwice() throws MyException {

        test("2 + 3 + = =","15","");
        test("2 - 3 - = =","1","");
        test("2 * 3 * = =","216","");
        test("2 / 3 / = =","1,5","");
        test("1 + 2 + 3 = =","9","");
        test("1 - 2 - 3 = =","-7","");
        test("1 * 2 * 3 = =","18","");
        test("1 / 2 / 3 = =","0,0555555555555556","");

    }

    @Test
    void testMainOperationTrice() throws MyException {

        //test positive values for Plus
        test("1 + 2 + 3 + 4 = =","14","");
        test("2 + 3 + 4 + 5 = =","19","");
        test("101 + 102 + 103 + 104 = =","514","");
        test("102 + 103 + 104 + 105 = =","519","");

        //test negative values for Plus
        test("1 neg + 2 neg + 3 neg + 4 neg = =","-14","");
        test("2 neg + 3 neg + 4 neg + 5 neg = =","-19","");
        test("101 neg + 102 neg + 103 neg + 104 neg = =","-514","");
        test("102 neg + 103 neg + 104 neg + 105 neg = =","-519","");

        //test different sign for Plus
        test("1 neg + 2 + 3 neg + 4 = =","6","");
        test("2 + 3 neg + 4 + 5 neg = =","-7","");
        test("101 neg + 102 + 103 neg + 104 = =","106","");
        test("102 + 103 neg + 104 + 105 neg = =","-107","");

        //test positive values for Minus
        test("1 - 2 - 3 - 4 = =","-12","");
        test("2 - 3 - 4 - 5 = =","-15","");
        test("101 - 102 - 103 - 104 = =","-312","");
        test("102 - 103 - 104 - 105 = =","-315","");

        //test negative values for Minus
        test("1 neg - 2 neg - 3 neg - 4 neg = =","12","");
        test("2 neg - 3 neg - 4 neg - 5 neg = =","15","");
        test("101 neg - 102 neg - 103 neg - 104 neg = =","312","");
        test("102 neg - 103 neg - 104 neg - 105 neg = =","315","");

        //test different sign for Minus
        test("1 neg - 2 - 3 neg - 4 = =","-8","");
        test("2 - 3 neg - 4 - 5 neg = =","11","");
        test("101 neg - 102 - 103 neg - 104 = =","-308","");
        test("102 - 103 neg - 104 - 105 neg = =","311","");

        //test positive values for Multiply
        test("1 * 2 * 3 * 4 = =","96","");
        test("2 * 3 * 4 * 5 = =","600","");
        test("101 * 102 * 103 * 104 = =","11 476 922 496","");
        test("102 * 103 * 104 * 105 = =","12 046 179 600","");

        //test negative values for Multiply
        test("1 neg * 2 neg * 3 neg * 4 neg = =","-96","");
        test("2 neg * 3 neg * 4 neg * 5 neg = =","-600","");
        test("101 neg * 102 neg * 103 neg * 104 neg = =","-11 476 922 496","");
        test("102 neg * 103 neg * 104 neg * 105 neg = =","-12 046 179 600","");

        //test different sign for Multiply
        test("1 neg * 2 * 3 neg * 4 = =","96","");
        test("2 * 3 neg * 4 * 5 neg = =","-600","");
        test("101 neg * 102 * 103 neg * 104 = =","11 476 922 496","");
        test("102 * 103 neg * 104 * 105 neg = =","-12 046 179 600","");

        //test positive values for Divide
        test("1 / 2 / 3 / 4 = =","0,0104166666666667","");
        test("2 / 3 / 4 / 5 = =","0,0066666666666667","");
        test("101 / 102 / 103 / 104 = =","8,888271227374158e-7","");
        test("102 / 103 / 104 / 105 = =","8,636763144391438e-7","");

        //test negative values for Divide
        test("1 neg / 2 neg / 3 neg / 4 neg = =","-0,0104166666666667","");
        test("2 neg / 3 neg / 4 neg / 5 neg = =","-0,0066666666666667","");
        //test("101 neg / 102 neg / 103 neg / 104 neg = =","-11 476 922 496","");
        //test("102 neg / 103 neg / 104 neg / 105 neg = =","-12 046 179 600","");

        //test different sign for Divide
        test("1 neg / 2 / 3 neg / 4 = =","0,0104166666666667","");
        test("2 / 3 neg / 4 / 5 neg = =","-0,0066666666666667","");
        test("101 neg / 102 / 103 neg / 104 = =","8,888271227374158e-7","");
        test("102 / 103 neg / 104 / 105 neg = =","-8,636763144391438e-7","");
    }

    @Test
    void testDifferentMainOperations() throws MyException {

        test("1 / 3 * 3 +","1","1  ÷  3  ×  3  +  ");
        test("25 + 25 + 25 * 25 * 25 = =","1 171 875","");
    }

    @Test
    void testExtraActions() throws MyException {

        test("sqr + 5 sqr","25","sqr( 0 )  +  sqr( 5 )");

        test("4 sqrt + = =","6","");
        test("25 sqrt + = =","15","");

        test("1 neg + = =","-3","");
        test("1 neg - = =","1","");
        test("1 neg * = =","-1","");
        test("1 neg / = =","-1","");

        test("0 sqrt sqrt sqrt = =","0","");
        test("1 sqrt sqrt sqrt = =","1","");
        test("16 sqrt sqrt sqrt = =","1,414213562373095","");
        test("256 sqrt sqrt sqrt = =","2","");

        test("0 sqr sqr sqr = =","0","");
        test("0 sqr sqr sqr","0","sqr( sqr( sqr( 0 ) ) )");
        test("1 sqr sqr sqr = =","1","");
        test("1 sqr sqr sqr","1","sqr( sqr( sqr( 1 ) ) )");
        test("16 sqr sqr sqr = =","4 294 967 296","");
        test("16 sqr sqr sqr","4 294 967 296","sqr( sqr( sqr( 16 ) ) )");

        test("0 neg neg neg","0","");
        test("1 neg neg neg = =","-1","");
        test("16 neg neg neg = =","-16","");
        test("256 neg neg neg = =","-256","");

        test("5 sqrt sqr","5","sqr( √( 5 ) )");
        test("25 sqrt 16 sqrt + ","4","√( 16 )  +  ");

        test("25 sqrt + - * / ","5","√( 25 )  \u00F7  ");

        test("25 sqrt + 16 sqrt = =","13","");
        test("25 sqrt + 16 sqrt + = =","27","");

        test("25 sqrt - 16 sqrt = =","-3","");
        test("25 sqrt - 16 sqrt - = =","-1","");

        test("16 sqrt + sqrt = =","8","");
        test("16 sqr + sqr = =","131 328","");

        test("1 + 2 sqr * 3 = =","45","");
        test("1 + 4 sqrt * 3 = =","27","");
        test("1 + 4 1/x * 3 = =","11,25","");

        test("5 sqrt sqr - 4 sqrt -","3","sqr( √( 5 ) )  -  √( 4 )  -  ");
        test("5 sqrt sqr sqrt sqr - 4 sqrt -","3","sqr( √( sqr( √( 5 ) ) ) )  -  √( 4 )  -  ");

        test("5 sqrt sqr - 3 sqrt sqr -","2","sqr( √( 5 ) )  -  sqr( √( 3 ) )  -  ");
        test("5 sqrt sqr - 3 sqrt sqr = =","-1","");

        test("5 sqrt sqr sqrt sqr - 3 sqrt sqr sqrt sqr -","2","sqr( √( sqr( √( 5 ) ) ) )  -  sqr( √( sqr( √( 3 ) ) ) )  -  ");
        test("5 sqrt sqr sqrt sqr - 3 sqrt sqr sqrt sqr = =","-1","");
        test("5 sqrt sqr sqrt sqr + 3 sqrt sqr sqrt sqr +","8","sqr( √( sqr( √( 5 ) ) ) )  +  sqr( √( sqr( √( 3 ) ) ) )  +  ");
        test("5 sqrt sqr sqrt sqr + 3 sqrt sqr sqrt sqr = =","11","");
    }

    @Test
    void testPercent() throws MyException {

        test("%","0","0");
        test("1 %","0","0");
        test("20 %","0","0");
        test("1 %","0","0");
        test("20 %","0","0");

        test("20 + 0 %","0","20  +  0");

        test("20 + 10 % = =","24","");
        test("20 + 10 %","2","20  +  2");
        test("20 - 10 % = =","16","");
        test("20 - 10 %","2","20  -  2");
        test("20 * 10 % = =","80","");
        test("20 * 10 %","2","20  ×  2");
        test("20 / 10 % = =","5","");
        test("20 / 10 %","2","20  ÷  2");

        test("20 + % = =","28","");
        test("20 + %","4","20  +  4");
        test("20 - % = =","12","");
        test("20 - %","4","20  -  4");
        test("20 * % = =","320","");
        test("20 * %","4","20  ×  4");
        test("20 / % = =","1,25","");
        test("20 / %","4","20  ÷  4");

        test("20 + 10 % + 15 % = =","28,6","");
        test("20 - 10 % - 15 % = =","12,6","");
        test("20 * 10 % * 15 % = =","1 440","");
        test("20 / 10 % / 15 % = =","4,444444444444444","");

        test("20 + 10 % + = =","66","");
        test("20 - 10 % - = =","-18","");
        test("20 * 10 % * = =","64 000","");
        test("20 / 10 % / = =","0,1","");

        test("0 % % %","0","0");
    }

    @Test
    void testScalingAndRounding() throws MyException {

        test("10 / 9 * 0,1 *","0,1111111111111111","10  ÷  9  ×  0,1  ×  ");
        test("10 / 11 =","0,9090909090909091","");
        test("1000000000000000 / 1111111111111111 =","0,9000000000000001","");
        test("1,999999999999999 * 0,1 = ","0,1999999999999999","");
        test("1,999999999999999 * 0,1 = =","0,02","");

        test("1 / 3 /","0,3333333333333333","1  ÷  3  ÷  ");
        test("3 1/x","0,3333333333333333","1/( 3 )");
        test("1 / 6 /","0,1666666666666667","1  ÷  6  ÷  ");

        test("2 sqrt","1,414213562373095","√( 2 )");
        test("3 sqrt","1,732050807568877","√( 3 )");
        test("10 / 9 /","1,111111111111111","10  ÷  9  ÷  ");
        test("10 / 3 * ","3,333333333333333","10  ÷  3  ×  ");
        test("20 sqrt","4,472135954999579","√( 20 )");
        test("30 sqrt","5,477225575051661","√( 30 )");

        test("100 / 9 /","11,11111111111111","100  ÷  9  ÷  ");
        test("100 / 3 * ","33,33333333333333","100  ÷  3  ×  ");
        test("200 sqrt","14,14213562373095","√( 200 )");
        test("30 sqrt","5,477225575051661","√( 30 )");

        test("1000000000000000 / 9 /","111 111 111 111 111,1","1000000000000000  ÷  9  ÷  ");
        test("100000000000000 / 9 /","11 111 111 111 111,11","100000000000000  ÷  9  ÷  ");

        test("1000000000000000 neg / 9 =","-111 111 111 111 111,1","");
        test("100000000000000 neg / 9 =","-11 111 111 111 111,11","");

//        test("1000000000000000 neg / 9 /","-111 111 111 111 111,1","1000000000000000  ÷  9  ÷  ");
//        test("100000000000000 neg / 9 /","-11 111 111 111 111,11","100000000000000  ÷  9  ÷  ");

        test("9999999999 * 9999999999 * 99 / 9999999999999999 =","989 999,9998020001","");

    }

    @Test
    void testEMinusValues() throws MyException {

        test("1 * 0,1 =","0,1","");
        test("1 * 0,1 = =","0,01","");
        test("1 * 0,1 = = =","0,001","");
        test("1 * 0,1 = = = =","0,0001","");
        test("1 * 0,1 = = = = =","0,00001","");
        test("25 sqrt sqr sqrt sqr 1/x","0,04","1/( sqr( √( sqr( √( 25 ) ) ) ) )");

        test("1 / 1000000000000000 = ","0,000000000000001","");
        test("1 / 1000000000000000 / 10 = ","0,0000000000000001","");
        test("1 / 1000000000000000 / 10 = =","1,e-17","");
        test("1 / 1000000000000000 / 10 = = *","1,e-17","1,e-17  ×  ");
        test("1 / 1000000000000000 = =","1,e-30","");
        test("22 / 100000000 =","0,00000022","");
        test("22 / 1000000000 =","0,000000022","");
        test("22 / 10000000000 =","0,0000000022","");
        test("22 / 100000000000 =","0,00000000022","");
        test("22 / 1000000000000 =","0,000000000022","");
        test("22 / 10000000000000 =","0,0000000000022","");
        test("22 / 100000000000000 =","0,00000000000022","");
        test("22 / 1000000000000000 =","0,000000000000022","");
        test("22 / 1000000000000000 / 10 =","0,0000000000000022","");
        test("22 / 1000000000000000 / 10 = =","2,2e-16","");
        test("22 / 1000000000000000 / 10 = = =","2,2e-17","");
        test("22 / 1000000000000000 = =","2,2e-29","");

        test("333 / 1000000000000000 = =","3,33e-28","");
        test("333 / 1000000000000000 = =","3,33e-28","");

        test("1 / 1000000000000000 "+enterInLoop(100),"1,e-1500","");
        test("1 / 1000000000000000 "+enterInLoop(666),"1,e-9990","");

        test("0,0000000000000023 * 0,1 =","2,3e-16","");
        test("0,0000000000000234 * 0,1 =","2,34e-15","");
        test("0,0000000000002345 * 0,1 =","2,345e-14","");
        test("0,0000000000023456 * 0,1 =","2,3456e-13","");
        test("0,0000000000234567 * 0,1 =","2,34567e-12","");
        test("0,0000000002345678 * 0,1 =","2,345678e-11","");
        test("0,0000000023456789 * 0,1 =","2,3456789e-10","");
        test("0,0000000234567891 * 0,1 =","2,34567891e-9","");
        test("0,0000002345678912 * 0,1 =","2,345678912e-8","");
        test("0,0000023456789123 * 0,1 =","2,3456789123e-7","");
        test("0,0000123456789123 * 0,1 =","1,23456789123e-6","");
        test("0,0001234567891234 * 0,1 =","1,234567891234e-5","");
        test("0,0012345678912345 * 0,1 =","1,2345678912345e-4","");
        test("0,0123456789123456 * 0,1 =","0,0012345678912346","");
        test("0,1234567891234567 * 0,1 =","0,0123456789123457","");
        test("1,234567891234567 * 0,1 =","0,1234567891234567","");
        test("0,0001111111111111 * 0,1 =","1,111111111111e-5","");

        test("0,1111111111111111 * 0,1 =","0,0111111111111111","");
        test("0,1111111111111111 * 0,1 = =","0,0011111111111111","");
        test("0,1111111111111111 * 0,1 = = =","1,111111111111111e-4","");
        test("0,1111111111111111 * 0,1 = = = =","1,111111111111111e-5","");
        test("0,1111111111111111 * 0,1 = = = = =","1,111111111111111e-6","");
        test("0,1111111111111111 * 0,1 = = = = = =","1,111111111111111e-7","");
    }

    @Test
    void testEPlusValues() throws MyException {

        test("1000000000000000 * =","1,e+30","");
        test("1000000000000000 * = *","1,e+30","1,e+30  ×  ");
        test("256 sqr sqr sqr = =","1,844674407370955e+19","");
        test("9999999999999999 + "+enterInLoop(20),"2,1e+17","");
    }
    @Test
    void testClear() throws MyException {

        test("5 - 3 = C - 2 -","-2","0  -  2  -  ");
    }

    @Test
    void testInvalidValues() throws MyException {

        test("1 neg sqrt");
        test("0 1/x");
        test("1 / 1000000000000000 "+enterInLoop(667));
        test("1 * 0,000000000000001 "+enterInLoop(667));
    }

    private void test(String expression, String display,String history) throws MyException {

        ResponseDTO response = prepareTest(expression);
        assertEquals(display,convertToString(response.getDisplayNumber(),DISPLAY_PATTERN));
        assertEquals(history,response.getHistory());
        controller.handleAction(new Clear());
    }

    private void test(String expression) throws MyException {

        assertThrows(MyException.class,()->prepareTest(expression));
        appModel.toDo(new Clear(),null);
    }

    private ResponseDTO prepareTest(String expression) throws MyException {

        String[] parsedActionStrings = expression.split(" ");
        ResponseDTO response = null;
        for (String str :parsedActionStrings) {
            if (str.matches("\\d+(,\\d+)?")){
                for (char ch : str.toCharArray()) {
                    controller.handleAction(actions.get(ch+""));
                }
            }else {
                response = controller.handleAction(actions.get(str));
            }
        }
        return response;
    }

    private String enterInLoop(int count){

        StringBuilder result = new StringBuilder("=");
        for (int i = 1; i < count; i++) {
            result.append(" =");
        }
        return result.toString();
    }
}