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
    private static final HashMap<String,Action> actions = createMap();
    private static final String DISPLAY_PATTERN = "#############,###.################";
    private static final String IS_DIGIT_REGEX = "\\d+(,\\d+)?";
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
        map.put("±",new Negate());
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
        test("0,2 ± + 0,2 =","0","");
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

        //test positive values for Minus
        test("1 - 2 - 3 - 4 = =","-12","");
        test("2 - 3 - 4 - 5 = =","-15","");
        test("101 - 102 - 103 - 104 = =","-312","");
        test("102 - 103 - 104 - 105 = =","-315","");

        //test negative values for Minus
        test("1 ± - 2 ± - 3 ± - 4 ± = =","12","");
        test("2 ± - 3 ± - 4 ± - 5 ± = =","15","");
        test("101 ± - 102 ± - 103 ± - 104 ± = =","312","");
        test("102 ± - 103 ± - 104 ± - 105 ± = =","315","");

        //test different sign for Minus
        test("1 ± - 2 - 3 ± - 4 = =","-8","");
        test("2 - 3 ± - 4 - 5 ± = =","11","");
        test("101 ± - 102 - 103 ± - 104 = =","-308","");
        test("102 - 103 ± - 104 - 105 ± = =","311","");

        //test positive values for Multiply
        test("1 * 2 * 3 * 4 = =","96","");
        test("2 * 3 * 4 * 5 = =","600","");
        test("101 * 102 * 103 * 104 = =","11 476 922 496","");
        test("102 * 103 * 104 * 105 = =","12 046 179 600","");

        //test negative values for Multiply
        test("1 ± * 2 ± * 3 ± * 4 ± = =","-96","");
        test("2 ± * 3 ± * 4 ± * 5 ± = =","-600","");
        test("101 ± * 102 ± * 103 ± * 104 ± = =","-11 476 922 496","");
        test("102 ± * 103 ± * 104 ± * 105 ± = =","-12 046 179 600","");

        //test different sign for Multiply
        test("1 ± * 2 * 3 ± * 4 = =","96","");
        test("2 * 3 ± * 4 * 5 ± = =","-600","");
        test("101 ± * 102 * 103 ± * 104 = =","11 476 922 496","");
        test("102 * 103 ± * 104 * 105 ± = =","-12 046 179 600","");

        //test positive values for Divide
        test("1 / 2 / 3 / 4 = =","0,0104166666666667","");
        test("2 / 3 / 4 / 5 = =","0,0066666666666667","");
        test("101 / 102 / 103 / 104 = =","8,888271227374158e-7","");
        test("102 / 103 / 104 / 105 = =","8,636763144391438e-7","");

        //test negative values for Divide
        test("1 ± / 2 ± / 3 ± / 4 ± = =","-0,0104166666666667","");
        test("2 ± / 3 ± / 4 ± / 5 ± = =","-0,0066666666666667","");
        test("101 ± / 102 ± / 103 ± / 104 ± = =","-8,888271227374158e-7","");
        test("102 ± / 103 ± / 104 ± / 105 ± = =","-8,636763144391438e-7","");

        //test different sign for Divide
        test("1 ± / 2 / 3 ± / 4 = =","0,0104166666666667","");
        test("2 / 3 ± / 4 / 5 ± = =","-0,0066666666666667","");
        test("101 ± / 102 / 103 ± / 104 = =","8,888271227374158e-7","");
        test("102 / 103 ± / 104 / 105 ± = =","-8,636763144391438e-7","");
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
    void testNegate() throws MyException {

        test("0 ± -","0","0  -  ");
        test("± -","0","negate( 0 )  -  ");

        test("20 ±","-20","");
        test("20 + ±","-20","20  +  negate( 20 )");

        test("1 ± + = =","-3","");
        test("1 ± - = =","1","");
        test("1 ± * = =","-1","");
        test("1 ± / = =","-1","");

        test("0 ± ± ±","0","");
        test("1 ± ± ± = =","-1","");
        test("16 ± ± ± = =","-16","");
        test("256 ± ± ± = =","-256","");

        test("1 ± +","-1","-1  +  ");
        test("1 ± + 2","2",null);
        test("1 ± + 2 ±","-2","-1  +  ");
        test("1 ± + 2 ± +","-3","-1  +  -2  +  ");
        test("1 ± + 2 ± + 3","3",null);
        test("1 ± + 2 ± + 3 ±","-3","-1  +  -2  +  ");
        test("1 ± + 2 ± + 3 ± +","-6","-1  +  -2  +  -3  +  ");
        test("1 ± + 2 ± + 3 ± + 4","4",null);
        test("1 ± + 2 ± + 3 ± + 4 ±","-4","-1  +  -2  +  -3  +  ");
        test("1 ± + 2 ± + 3 ± + 4 ± =","-10","");
        test("1 ± + 2 ± + 3 ± + 4 ± = =","-14","");
        test("2 ± + 3 ± + 4 ± + 5 ± = =","-19","");
        test("101 ± + 102 ± + 103 ± + 104 ± = =","-514","");
        test("102 ± + 103 ± + 104 ± + 105 ± = =","-519","");

        test("1 ± + 2 + 3 ± + 4 = =","6","");
        test("2 + 3 ± + 4 + 5 ± = =","-7","");
        test("101 ± + 102 + 103 ± + 104 = =","106","");
        test("102 + 103 ± + 104 + 105 ± = =","-107","");
    }

    @Test
    void testPercent() throws MyException {

        test("%","0","0");
        test("0 % % %","0","0");
        test("1 % % %","0","0");
        test("1 %","0","0");
        test("20 %","0","0");
        test("1 %","0","0");
        test("20 %","0","0");

        test("20 + 0 %","0","20  +  0");
        test("20 + 20 % = %","5,76","5,76");

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

        test("1000000000000000 ± / 9 =","-111 111 111 111 111,1","");
        test("100000000000000 ± / 9 =","-11 111 111 111 111,11","");

//        test("1000000000000000 ± / 9 /","-111 111 111 111 111,1","1000000000000000  ÷  9  ÷  ");
//        test("100000000000000 ± / 9 /","-11 111 111 111 111,11","100000000000000  ÷  9  ÷  ");

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

        test("1000000000000000 + ","1 000 000 000 000 000","1000000000000000  +  ");
        test("1000000000000000 * =","1,e+30","");
        test("1000000000000000 * = *","1,e+30","1,e+30  ×  ");
        test("256 sqr sqr sqr = =","1,844674407370955e+19","");
        test("9999999999999999 + "+enterInLoop(20),"2,1e+17","");
    }
    @Test
    void testClear() throws MyException {

        test("5 - 3 = C - 2 -","-2","0  -  2  -  ");
        test("5 - 2333 CE","0",null);

    }

    @Test
    void testInvalidValues() throws MyException {

        test("1 ± sqrt");
        test("0 1/x");
        test("1 / 1000000000000000 "+enterInLoop(667));
        test("1 * 0,000000000000001 "+enterInLoop(667));
        test("1000000000000000 * "+enterInLoop(667));
    }

    private void test(String expression, String display,String history) throws MyException {

        ResponseDTO response = prepareTest(expression);
        assertEquals(display,convertToString(response.getDisplayNumber(),DISPLAY_PATTERN));
        assertEquals(history,response.getHistory());
        controller.handleAction(new Clear());
    }

    private void test(String expression) throws MyException {

        assertThrows(MyException.class,()->prepareTest(expression));
        controller.handleAction(new Clear());
    }

    private ResponseDTO prepareTest(String expression) throws MyException {

        String[] parsedActionStrings = expression.split(" ");
        ResponseDTO response = null;
        for (String str :parsedActionStrings) {
            if (str.matches(IS_DIGIT_REGEX)){
                for (char ch : str.toCharArray()) {
                    response = controller.handleAction(actions.get(ch+""));
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
    /////////////////////////////////////////////////////////////////
//    @Test
//    public void testExpression() throws Exception {
//        //test many buttons, that font size is decreasing
//        testStyle("11111111111111111111111111111111111111111111111111111");

//        testApp("9876543210 + 123456789 / 123456789 * 123456789 - 123456789 ± =", "10123456788");
//        testApp("10 / 3 * 3 =", "10");
//        testApp("2 + 10 / 3 * 3 =", "12");
//        testApp("22 + 22 * 22 + 10 / 3 * 3 =", "516");
//        testApp("2 * 10 / 3 * 3 =", "20");
//        testApp("10 / 3 *", "3,333333333333333");
//        testApp("9 / 3 * 3 =", "9");
//        testApp("10 / 3 + 1 =", "4,333333333333333");
//
//        testApp("2 ± + 2 * 2 =", "2");
//        testApp("2 + 2 + * 2 ± =", "-8");
//        testApp("2 + 2 * 2 ± =", "-2");
//        testApp("2 + 2 * 2 ± = =", "4");
//        testApp("2 + 2 * 2 ± = = =", "-8");
//        testApp("2 + 3 * 7 ± =", "-19");
//        testApp("2 + 3 * 7 ± = =", "133");
//        testApp("2 + 3 * 7 ± = = =", "-931");
//        testApp("2 + 2 ± + * 2 = = = =", "0");
//        testApp("2 + 2 * =", "6");
//        testApp("2 + 2 * = = =", "24");
//        testApp("23 + 12 * / 2 + = = =", "116");
//
//        testApp("2 ± + 3 - 4 / 7 * 9 =", "-4,142857142857143");
//        testApp("2 + 2 * 2 =", "6");
//        testApp("2 + 2 + * 2 =", "8");
//        testApp("2 + 2 + * 2 = = = =", "64");
//        testApp("2 + 2 + * 2 + = = = =", "40");
//        testApp("2 + 3 - 4 / 7 * 9 =", "-0,1428571428571429");
//        testApp("4 ± + 5.5 * + 2.5 =", "4");
//        testApp("2 / 5 * 23 ± + 5.5 * + 2.5 =", "-1,2");
//    }
//
//    @Test
//    public void testMOperation() throws Exception {
//        testApp("mc mr", "0");
//        testApp("2 m+ mr", "2");
//        testApp("10 m+ mr", "12");
//        testApp("112 m- mr", "-100");
//        testApp("999999 m+ m- mr", "-100");
//        testApp("100 m+ 200", "200");
//        testApp("300 m- 999", "999");
//        testApp("mr", "-300");
//        testApp("mr 234", "234");
//        testApp("mc 190", "190");
//        testApp("mr", "0");
//
//        //test button m+
//        testApp("mc mr", "0");
//        testApp("22 + 2 = m+ mr", "24");
//        testApp("999999 + 999999 - 999999 = m+ mr", "1000023");
//        testApp("999999 m+ mr", "2000022");
//        testApp("999999 + 1 m+ + 2 m+ mr", "2000025");
//        testApp("mc mr", "0");
//        testApp("22 + 2 = m+ 2", "2");
//        testApp("22 + 2 = m+ 2 + / * + = =", "6");
//        testApp("mr", "48");
//        testApp("mc mr", "0");
//
//        //test button m-
//        testApp("22 + 2 = m- mr", "-24");
//        testApp("22 + 2 = m- mr 23", "23");
//        testApp("999999 + 999999 - 999999 m+ = m- mr", "-48");
//        testApp("mc mr", "0");
//        testApp("999999 m- mr", "-999999");
//        testApp("999999 m- mr", "-1999998");
//        testApp("mc mr", "0");
//        testApp("22 + 2 = m- mr 23 + =", "46");
//        testApp("mc mr", "0");
//        testApp("1234567890 + 2 = m- mr * 23 + ", "-28395061516");
//        testApp("mc mr", "0");
//        testApp("1234567890 + 2 = m- mr * 23 + =", "-56790123032");
//        testApp("mc mr", "0");
//
//        //test with = m+,m-,mc,mr,+,-,/,*,%
//        testApp("22 + 2 = m+ 2 + / * + = = + 22 =", "28");
//        testApp("22 + 2 = m+ 2 + / * + = 2 + = = + 4 = =", "14");
//        testApp("22 + 2 = m+ 2 / * + = = 34", "34");
//        testApp("22 + 2 = m- 2 * / * + = =", "6");
//        testApp("22 + 2 = m- 8 - / + * = * 8 = + 3 =", "515");
//        testApp("22 + 2 = m- 123 / + * = * 8 = + 123 =", "121155");
//        testApp("123 / + * = * 8 = + 123 = = = mr", "0");
//        testApp("12 + 34 = m+ m- 2 + / * + = = + 22 =", "28");
//        testApp("3425 * 3220923 = m- m+ 2 + / * + = 2 + = = + 4 = =", "14");
//        testApp("784334 + 23784 = m+ m- mr 2 / * + = = 34", "34");
//        testApp("784334 + 23784 = m+ + 2 / * + = = ", "2424360");
//        testApp("mc", "0");
//        testApp("mr", "0");
//
//        testApp("1000000000 * = m+ mr", "1e18");
//        testApp("mc mr", "0");
//        testApp("1000000000 * = m+ mr m+ mr", "2e18");
//        testApp("mc mr", "0");
//    }
//
//    @Test
//    public void testOperation() throws Exception {
//        //test only operation and equal
//        testApp("/ * * / - + / *  = ±", "0");
//        testApp("/ * * / - + / * % % = ±", "0");
//        testApp("/ / + * - * / - + / *  = ± = = =", "0");
//        testApp("/ * - + / + = = - + / *  = ±", "0");
//        testApp(". = = = = / * * / - + / *  = ±", "0");
//        testApp("......... = = = = / * * / - + / *  = ±", "0");
//        testApp("......... = = ..... = = / * * / ..... - + / *  = ±", "0");
//        testApp(".0... = = = = / * * / - + / *  = ±", "0");
//        testApp("0. = = = = / * * / - + / *  = ±", "0");
//        testApp("0.0000000 = = = = / * * / - + / *  = ±", "0");
//
//        //operation with equal in different position
//        testApp("/ 2 =", "0");
//        testApp("2 = / =", "1");
//        testApp("2 = / = =", "0,5");
//        testApp("2 = / = = =", "0,25");
//        testApp("2 = + - / / = = =", "0,25");
//        testApp("2 + = + - / / = = =", "0,0625");
//
//        testApp("9999999999 % =", "99999999,99");
//        testApp("9999999999 % = % %", "9999,999999");
//        testApp("9999999999 % = % =", "999999,9999");
//        testApp("9999999999 % = % % % = =", "99,99999999");
//        testApp("9999999999 = % = % % % = =", "99,99999999");
//        testApp("9999999999 % % % = % % % = =", "0,009999999999");
//
//        testApp("1 + = = - +", "3");
//        testApp("1 / + = = 2 + = ", "4");
//        testApp("1 / + = = - + 2 = ", "5");
//        testApp("2 + 2 = + 2 =", "6");
//        testApp("1 / + = = - + 2 = = ", "7");
//        testApp("2 + - / * + 2 = + + - - + 2 =", "6");
//
//        testApp("19 = * =", "361");
//        testApp("191919 + = - 2 =", "383836");
//        testApp("191919 + + + + + = - - - 2 =", "383836");
//        testApp("0 + 0.3 = = = = * 3 = = =", "32,4");
//        testApp("000000 + 00000.3 = = = = * 00003 = = =", "32,4");
//        testApp("0 . . . . + 0 . . . . 3 = = = = * 000003 . . . . = = =", "32,4");
//
//        testApp("/ 2 = = 2 + 3 = = =", "11");
//        testApp("/ 2 = = 2 + 3 =", "5");
//        testApp("/ 2 = = 2 / 3 =", "0,6666666666666667");
//        testApp("/ 2 = = 2 / 3 = = =", "0,07407407407407407");
//        testApp("/ 2 = = 2 / 3 = + 2 = =", "4,666666666666667");
//    }
//
//    @Test
//    public void testPercentOperation() throws Exception {
//        testApp("3 + 3 - 4 + 6 * 2 % =", "2,72");
//        testApp("3 % + 3 - 4 + 6 * 2 % =", "-0,25");
//        testApp("20 % + 3 % - 4 + 6 * 2 % =", "-3,074");
//        testApp("20 % + 3 % - 4 + 6 * 2 %", "0,12");
//
//        testApp("2 * 2 % ", "0,04");
//        testApp("2 * % =", "0,08");
//        testApp("2 * 2 % =", "0,08");
//        testApp("2 * 2 % = =", "0,0032");
//        testApp("2 * 2 % = = =", "0,000128");
//
//        testApp("2 ± + % =", "-1,96");
//        testApp("2 ± + %", "0,04");
//        testApp("2 + % =", "2,04");
//        testApp("2 + 2 % =", "2,04");
//        testApp("2 + 2 % = =", "2,08");
//        testApp("2 + 2 % = = =", "2,12");
//
//        testApp("2 - 2 % = = =", "1,88");
//        testApp("2 - % = = =", "1,88");
//        testApp("2 - %", "0,04");
//        testApp("2 ± - %", "0,04");
//        testApp("2 ± - % =", "-2,04");
//        testApp("2 ± - + / - %", "0,04");
//        testApp("2 ± - 2 % = = =", "-1,88");
//
//        testApp("2 / % =", "50");
//        testApp("2 / % = = =", "31250");
//        testApp("2 / 2 % = =", "1250");
//        testApp("2 / 2 % =", "50");
//
//        testApp("2 % =", "0,02");
//        testApp("2 % % =", "0,0002");
//        testApp("2 % % = = = =", "0,0002");
//        testApp("2 % = = =  % = = = =", "0,0002");
//        testApp("2 = = = % = = = = % = =", "0,0002");
//
//        testApp("2 ± % % =", "-0,0002");
//        testApp("2 ± % ± % =", "0,0002");
//        testApp("2 ± % ± % ± =", "-0,0002");
//        testApp("2 ± ± ± % ± % ± =", "-0,0002");
//        testApp("1234567899876543 ± ± ± % ± % ± =", "-123456789987,6543");
//        testApp("1234567899876543 ± ± ± % ± % ± = = =", "-123456789987,6543");
//
//        testApp("0 % =", "0");
//        testApp("0 % ", "0");
//        testApp("100 - 10 % ", "10");
//        testApp("100 - 10 % =", "90");
//        testApp("100 * 10 % =", "1000");
//        testApp("100 * + / - % =", "0");
//        testApp("100 + - % =", "0");
//        testApp("100 ± - % = =", "-300");
//        testApp("100 % ± = = =", "-1");
//        testApp("10 %", "0,1");
//
//        testApp("23500 / 2 %", "470");
//        testApp("23500 / 2 % =", "50");
//        testApp("2 / 23500 %", "470");
//        testApp("2 / 23500 % =", "0,00425531914893617");
//        testApp("9 % % % % % % % ", "0,00000000000009");
//        testApp("9 % % % % % % % % ", "9e-16");
//        testApp("9 % % % % % % % % = = = = =", "9e-16");
//        testApp("9 % % % % % % % % % % % % % % % %  " +
//                "% % % % % % % % % % % % % % % % % % % " +
//                "% % % % % % % % % % % % % ", "Не определено");
//    }
//
//    @Test
//    public void testMultiplyOperation() throws Exception {
//        testApp("98765432 * 9 =", "888888888");
//        testApp("98765432 ± * 9 ± =", "888888888");
//        testApp("98765432 ± * 9 =", "-888888888");
//        testApp("724387928792 * 724387928792 =", "5,247378713795637e23");
//        testApp("724387928792 * 724387928792 * 724387928792 =", "3,80113779807365e35");
//        testApp("987654321 * 987654321 =", "9,75461057789971e17");
//        testApp("0.987654321 * .987654321 =", "0,975461057789971");
//
//        testApp("1 * 1 =", "1");
//        testApp("1 ± * 9 =", "-9");
//        testApp("1 * * * 9 =", "9");
//        testApp("1 * * * 9 = = = =", "6561");
//        testApp("1 + / * 9 =", "9");
//        testApp(".1 ± + / - * 9 =", "-0,9");
//        testApp(".1 ± ± ± + / - * 9 ± ± =", "-0,9");
//
//        testApp(". + / - * 9 ± ± =", "0");
//        testApp(".0 + / - * 9 =", "0");
//        testApp("0.0 + / - * 9 =", "0");
//        testApp("0 +  * 9 =", "0");
//        testApp(". + / - * 9 ± ± = = =", "0");
//        testApp("0.0000 + - / * 9 =", "0");
//        testApp("0. +  * 9 =", "0");
//        testApp("0345 + / * 9 =", "3105");
//        testApp("0345 + / * 0009 =", "3105");
//
//        testApp("1000000000 * = = = = =", "1e54");
//        testApp("1000000000 * = = = = = = = = = = = =", "1e117");
//        testApp("99 * = = = = = = = = = = = = = = = = = = =", "8,179069375972309e39");
//        testApp("1000000000 * = = = = = = = = = = = = = = = = = = =", "Не определено");
//        testApp("99 * = = = = = = = = = = = = = = = = = = = = = " +
//                "= = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =" +
//                " = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =", "Не определено");
//        testApp("99999999 * = = = = = = = = = =", "9,999998900000055e87");
//        testApp("99999999 * = = = = = = = = = = = = = = = = = = =", "9,99999800000019e159");
//        testApp("99999999 * = = = = = = = = = = = = = = = = = = = =", "Не определено");
//
//        testAlertShown();
//        assertWindowIsMinimized();
//    }
//
//    @Test
//    public void testExit() throws Exception {
//        final boolean[] invoked = new boolean[1];
//
//        ExitController.instance = new ExitController() {
//            @Override
//            public void exit() {
//                invoked[0] = true;
//            }
//        };
//        //click exit
//        WaitForAsyncUtils.waitForFxEvents();
//        Button close = find("#close");
//        Platform.runLater(close::fire);
//        WaitForAsyncUtils.waitForFxEvents();
//        assertTrue(invoked[0]);
//    }
//
//    @Test
//    public void testDivideOperation() throws Exception {
//        testApp("2 a / 3 =", "0,6666666666666667");
//        testApp("2 / 3 =", "0,6666666666666667");
//        testApp("10 / 3 =", "3,333333333333333");
//        testApp("10 / 3 = =", "1,111111111111111");
//        testApp("10 / 3 = = =", "0,3703703703703704");
//        testApp("3 / 9 =", "0,3333333333333333");
//        testApp("9 / 5 =", "1,8");
//        testApp("3 / 3 =", "1");
//        testApp("10 / 10 =", "1");
//        testApp("19191919191 / 354834693643 =", "0,05408692987137564");
//
//        testApp("2 / / / / ", "2");
//        testApp("2 / / / / = = =", "0,25");
//        testApp("10 / =", "1");
//        testApp("10 / = = =", "0,01");
//        testApp("91919191919 / = ", "1");
//        testApp("91919191919 / = = =", "1,183552711030181e-22");
//
//        testApp("0 / 2 =", "0");
//        testApp("0 / 438974723 ± =", "0");
//        testApp("0 ± / 74287482 ± =", "0");
//        testApp("00000 ± / 0000743278423 ± =", "0");
//        testApp("0000743278423 ± / 0000743278423  =", "-1");
//        testApp("438974723 / 438974723 ± =", "-1");
//        testApp("438974723 ± / 438974723 =", "-1");
//        testApp("7865947546 ± / 7865947546 ± =", "1");
//        testApp("828742387 ± / 7865947546 ± =", "0,1053582396975725");
//
//        testApp("0 / = 2", "2");
//        testApp("2 / 0 * 2 =", "2");
//        testApp("0 / = 1234456789", "1234456789");
//        testApp("0 / = 564755", "564755");
//        testApp("0 / 0 = 54321", "54321");
//        testApp("0 / .0 = 564755", "564755");
//        testApp("0 / 0. = 564755", "564755");
//        testApp("0 / 0.0000 = 564755", "564755");
//        testApp("0 / .00000 = 564755", "564755");
//
//        testApp("2 / 0 =", "Не определено");
//        testApp("0 / =", "Не определено");
//        testApp("124124 / 0 =", "Не определено");
//        testApp("21367 / 0 =", "Не определено");
//        testApp("0 / = %", "Не определено");
//        testApp("0 / = % % % % %", "Не определено");
//        testApp("0 / / = = = = ", "Не определено");
//        testApp("3410853457 ± / 0 =", "Не определено");
//        testApp("84748375782740336014 ± / . =", "Не определено");
//        testApp("2374387246262 ± / .0 =", "Не определено");
//        testApp("2374387246262 ± / .000 =", "Не определено");
//        testApp("3410853457 ± / 0. =", "Не определено");
//        testApp("459574754937 * / 0 . =", "Не определено");
//        testApp("9876567493 ± + - * / 0 =", "Не определено");
//
//        testApp("1000000 / = = = = = = = = = = = = = = = = =", "Не определено");
//        testApp("1000000 / = = = = = = = = = = = = = = = = = = =", "Не определено");
//        testApp("1 / 99999999 = = = = = = = = = = =", "1,000000110000007e-88");
//        testApp("1 / 99999999 = = = = = = = = = = = =", "Не определено");
//        testApp("1 / 99999999 = = = = = = = = = = = = =", "Не определено");
//        testApp("2 / 8888888888 = = = = = = = = = =", "Не определено");
//        testApp("2 / 8888888888 = = = = = = = = = = = " +
//                "= = = = = = = = = = ", "Не определено");
//        testApp("2 / 8888888888 = = = = = = = = = = = " +
//                "= = = = = = = = = = +", "Не определено");
//        testApp("2 / 8888888888 = = = = = = = = = = = " +
//                "= = = = = = = = = = /", "Не определено");
//        testApp("2 / 8888888888 = = = = = = = = = = = " +
//                "= = = = = = = = = = + / *", "Не определено");
//        testApp("2 / 8888888888 = = = = = = = = = = = " +
//                "= = = = = = = = = = + / * 2 / 2 =", "1");
//        testApp("2 / 8888888888 = = = = = = = = =", "5,773015161583996e-90");
//
//    }
//
//    @Test
//    public void testSubtractOperation() throws Exception {
//        testApp("2 - 3 =", "-1");
//        testApp("9 - 5 =", "4");
//        testApp("3 - 9 =", "-6");
//        testApp("5 - 5 =", "0");
//        testApp("239 - 359 =", "-120");
//        testApp("99 - 5 =", "94");
//        testApp("35 - 8 =", "27");
//
//        testApp("123456789 ±  * / - 01111 =", "-123457900");
//        testApp("00003456 + - 00002 ± =", "3458");
//        testApp("000 / - 2 =", "-2");
//        testApp("2 - - 0000 =", "2");
//        testApp("2 ± - + - 0.00000 =", "-2");
//        testApp("2 - / - 0 =", "2");
//        testApp("2 ± - * * - 0 =", "-2");
//        testApp("0 - 2 ± =", "2");
//        testApp("0 - 0002 ± =", "2");
//
//        testApp("99 - 5 ± =", "104");
//        testApp("99 - 5 ± ± ± =", "104");
//        testApp("99 ± ± - 5 ± ± ± =", "104");
//        testApp("35 ± - 8 ± =", "-27");
//        testApp("99 + - 5 ± =", "104");
//        testApp("35 ± * - 8 ± =", "-27");
//        testApp("99 - - 5 ± =", "104");
//        testApp("35 ± * / + - 8 ± =", "-27");
//
//        testApp("2.5 - 0.5 =", "2");
//        testApp("2.5  / - 0.5 =", "2");
//        testApp("123456789.987654321  - 987654321.123456789 =", "-864197531,1358025");
//        testApp("987654321.123456789 - 123456789.987654321 =", "864197531,1358025");
//        testApp("987654321.123456789 + / * * - 987654321.123456789 ± =", "1975308642,246914");
//        testApp("123456789.123456789 ± - / * + + - 123456789.987654321 =", "-246913579,1111111");
//
//        testApp("999 ± - = = = =", "2997");
//        testApp("191919 + - 2 =", "191917");
//        testApp("191919 + + + + + - - - 2 =", "191917");
//        testApp("191919 * * * * / / / - 2 =", "191917");
//        testApp("11111 * * * * / / / - = = = = = = = = = = = = = =", "-144443");
//    }
//
//    @Test
//    public void testAddOperation() throws Exception {
//        testApp("1 + 2 =", "3");
//        testApp("9 + 5 =", "14");
//        testApp("123 + 3 =", "126");
//        testApp("123456789 + 987654321 =", "1111111110");
//        testApp("724387928792 + 724387928792 =", "1448775857584");
//        testApp("724387928792 + 724387928792 + 724387928792 + 724387928792 =", "2897551715168");
//        testApp("724387928792 + 724387928792 + 724387928792 + 724387928792 + 724387928792 + 724387928792 =", "4346327572752");
//
//        testApp("123456789 ± + 01111 =", "-123455678");
//        testApp("00003456 + 00002 ± =", "3454");
//        testApp("000 + 2 =", "2");
//        testApp("2 + 0000 =", "2");
//        testApp("2 ± + 0.00000 =", "-2");
//        testApp("2 + 0 =", "2");
//        testApp("2 ± + 0 =", "-2");
//        testApp("0 + 2 ± =", "-2");
//        testApp("0 + 0002 ± =", "-2");
//
//        testApp("5.6 + 2.7 =", "8,3");
//        testApp("0.99 + 0.03 =", "1,02");
//        testApp("0.999999999999 + 0.0000000003 =", "1,000000000299");
//        testApp("0000.999999999999 + 0000.0000000003 =", "1,000000000299");
//        testApp("0.999999999999 + 000.0000000003 =", "1,000000000299");
//        testApp("123456789.987654321 + 987654321.123456789 =", "1111111111,111111");
//        testApp("1.111111111 + 2.999999999 =", "4,11111111");
//
//        testApp("1.111111111 ± + 2.999999999 =", "1,888888888");
//        testApp("1.111111111 ± + 2.999999999 ± ± =", "1,888888888");
//        testApp("1.111111111 ± ± + 2.999999999 ± ± =", "4,11111111");
//        testApp("1.111111111 + 2.999999999 ± =", "-1,888888888");
//        testApp("1.111111111 + 2.999999999 ± ± =", "4,11111111");
//
//        testApp("1 - * / + 2 =", "3");
//        testApp("1 - * / + + - + 2 =", "3");
//        testApp("1 - * / + + - + 2 =", "3");
//        testApp("123456789 * / + + - + 2 =", "123456791");
//        testApp("123456789 * / + = =", "370370367");
//
//        testApp("999 ± + = = = =", "-4995");
//        testApp("191919 + 2 ± =", "191917");
//        testApp("0.99 + 0.03 ± =", "0,96");
//        testApp("35 ± + 23 ± =", "-58");
//
//        testApp("2 + + + = = = =", "10");
//        testApp("2 + = = = =", "10");
//        testApp("2 ± ± ± ± + = = = =", "10");
//        testApp("2 ± + = = = =", "-10");
//
//        testApp("1 - * / + + - + 2 ", "2");
//        testApp("1123324 + - + 2 ±", "-2");
//        testApp("1 - * / + + - + 2 ±", "-2");
//        testApp("1 - * / + + - + 2 ±", "-2");
//        testApp("2 ± + + + +", "-2");
//    }
//
//    @Test
//    public void testMouseClickedOnButton() throws Exception {
//        testClickMouseButton("AC 12 + 34 - 56 * 78 / 90 = ", "-2,533333333333333");
//        testClickMouseButton("AC 1 . . . 2 + 34 - 56 * 78 / 90 = ", "-13,33333333333333");
//        testClickMouseButton("AC 1 . . . 2 + 34 - 56 * 78 / 90 = AC", "0");
//
//        testClickMouseButton("AC 9876543210 % % + 36487136 - 374138274 * 93755 / 3890 = ", "-8979834690,108306");
//        testClickMouseButton("AC 9876543210 % % + 36487136 - 374138274 * 93755 / 3890 =  % %", "-897983,4690108306");
//        testClickMouseButton("AC 9876543210 % %  =", "987654,321");
//        testClickMouseButton("AC 9876543210 ± ± ± ± ± ± ± ± % %  = ", "987654,321");
//
//        testClickMouseButton("AC . 00000001", "0,00000001");
//        testClickMouseButton("AC . 00000001 ±", "-0,00000001");
//        testClickMouseButton("AC . 000 . . . 00001 = = = =", "0,00000001");
//        testClickMouseButton("AC . 00000001 % % % % %", "1e-18");
//
//        //test click on mc m+,m-,mr.mc
//        testClickMouseButton("AC 2 m+ mr", "2");
//        testClickMouseButton("AC 112 m- mr", "-110");
//        testClickMouseButton("AC 999999 m+ m- mr", "-110");
//        testClickMouseButton("AC 100 m+ 200", "200");
//        testClickMouseButton("AC 300 m- 999", "999");
//        testClickMouseButton("AC mr", "-310");
//        testClickMouseButton("AC mr 234", "234");
//        testClickMouseButton("AC mc 190", "190");
//        testClickMouseButton("AC mr", "0");
//    }
//
//    @Test
//    public void testKeyBoard() throws Exception {
//        testAppKeyEvent("12 + 34 - 56 * 78 / 90 = ", "-2,533333333333333");
//        testAppKeyEvent("1 . . . 2 + 34 - 56 * 78 / 90 = ", "-13,33333333333333");
//        testAppKeyEvent("1 . . . 2 + 34 - 56 * 78 / 90 = AC", "0");
//        testAppKeyEvent("1 + 2 + 3 + 4 =", "10");
//
//        testAppKeyEvent("9876543210 % % + 36487136 - 374138274 * 93755 / 3890 = ", "-8979834690,108306");
//        testAppKeyEvent("9876543210 % % + 36487136 - 374138274 * 93755 / 3890 = % %", "-897983,4690108306");
//        testAppKeyEvent(" 9876543210 % %  =", "987654,321");
//        testAppKeyEvent(" 9876543210 ± ± ± ± ± ± ± ± % %  = ", "987654,321");
//        testAppKeyEvent(" 9876543210 ±  % %  = ", "-987654,321");
//        testAppKeyEvent(" 9876543210 ± ± % % % % % % % %  = ", "0,000000987654321");
//
//        testAppKeyEvent(" . 00000001", "0,00000001");
//        testAppKeyEvent("", "0");
//        testAppKeyEvent(" . 00000001 ±", "-0,00000001");
//        testAppKeyEvent(" . 000 . . . 00001 = = = =", "0,00000001");
//        testAppKeyEvent(" . 00000001 % % %", "0,00000000000001");
//    }
//
//    @Test
//    public void testMouseDragged() throws Exception {
//        assertWindowLocation(700, 400);
//        assertWindowLocation(700, 300);
//        assertWindowLocation(800, 300);
//        assertWindowLocation(600, 500);
//        assertWindowLocation(500, 400);
//        assertWindowLocation(400, 300);
//        assertWindowLocation(900, 400);
//        assertWindowLocation(200, 700);
//        assertWindowLocation(1400, 400);
//        assertWindowLocation(1300, 200);
//        assertWindowLocation(1200, 100);
//        assertWindowLocation(1100, 300);
//
//        Random random = new Random();
//        assertWindowLocation(random.nextInt(1200) + 100, random.nextInt(750) + 50);
//        assertWindowLocation(random.nextInt(1200) + 100, random.nextInt(750) + 50);
//        assertWindowLocation(random.nextInt(1200) + 100, random.nextInt(750) + 50);
//        assertWindowLocation(random.nextInt(1200) + 100, random.nextInt(750) + 50);
//        assertWindowLocation(700, 400);
//    }
}