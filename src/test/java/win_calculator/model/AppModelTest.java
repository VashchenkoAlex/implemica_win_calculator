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
import static win_calculator.utils.AppUtils.convertToString;

class AppModelTest {

    private FXMLViewController controller = new FXMLViewController();
    private static final HashMap<String, Action> actions = createMap();
    private static final String DISPLAY_PATTERN = "#############,###.################";
    private static final String IS_DIGIT_REGEX = "\\d+(,\\d+)?";
    private static final String COMA = ",";

    private static HashMap<String, Action> createMap() {

        HashMap<String, Action> map = new HashMap<>();
        map.put("0", new ZeroDigit());
        map.put("1", new OneDigit());
        map.put("2", new TwoDigit());
        map.put("3", new ThreeDigit());
        map.put("4", new FourDigit());
        map.put("5", new FiveDigit());
        map.put("6", new SixDigit());
        map.put("7", new SevenDigit());
        map.put("8", new EightDigit());
        map.put("9", new NineDigit());
        map.put(",", new Coma());
        map.put("+", new Add());
        map.put("-", new Subtract());
        map.put("*", new Multiply());
        map.put("/", new Divide());
        map.put("%", new Percent());
        map.put("sqrt", new Sqrt());
        map.put("sqr", new Sqr());
        map.put("1/x", new Fraction());
        map.put("CE", new ClearDisplay());
        map.put("C", new Clear());
        map.put("<-", new BaskSpace());
        map.put("=", new Enter());
        map.put("±", new Negate());
        return map;
    }

    @Test
    void testMainOperations() throws MyException {

        //test Add
        test("+", "0", "0  +  ");
        test("+ =", "0", "");
        test("+ 2 =", "2", "");
        test("2 + 2 =", "4", "");
        test("2 + 2 = +", "4", "4  +  ");
        test("0 + = = =", "0", "");
        test("1 + = = =", "4", "");
        test("2 + = = =", "8", "");
        test("13 + = = =", "52", "");
        test("9999 + = = =", "39 996", "");
        test("0,2 + 0,2 =", "0,4", "");
        test("0,2 ± + 0,2 =", "0", "");
        test("2 + 3 + = =", "15", "");
        test("1 + 2 + 3 = =", "9", "");
        test("1 + 2 + 3 = + 4 +", "10", "6  +  4  +  ");
        test("1 + 2 + 3 + 4 = =", "14", "");
        test("2 + 3 + 4 + 5 = =", "19", "");
        test("101 + 102 + 103 + 104 = =", "514", "");
        test("102 + 103 + 104 + 105 = =", "519", "");
        test("1 + 2 =", "3", "");
        test("9 + 5 =", "14", "");
        test("123 + 3 =", "126", "");
        test("123456789 + 987654321 =", "1 111 111 110", "");
        test("724387928792 + 724387928792 =", "1 448 775 857 584", "");
        test("724387928792 + 724387928792 + 724387928792 + 724387928792 =", "2 897 551 715 168", "");
        test("724387928792 + 724387928792 + 724387928792 + 724387928792 + 724387928792 + 724387928792 =", "4 346 327 572 752", "");

        test("123456789 ± + 01111 =", "-123 455 678", "");
        test("00003456 + 00002 ± =", "3 454", "");
        test("000 + 2 =", "2", "");
        test("2 + 0000 =", "2", "");
        test("2 ± + 0,00000 =", "-2", "");
        test("2 + 0 =", "2", "");
        test("2 ± + 0 =", "-2", "");
        test("0 + 2 ± =", "-2", "");
        test("0 + 0002 ± =", "-2", "");

        test("5,6 + 2,7 =", "8,3", "");
        test("0,99 + 0,03 =", "1,02", "");
        test("0,999999999999 + 0,0000000003 =", "1,000000000299", "");
        test("0000,999999999999 + 0000,0000000003 =", "1,000000000299", "");
        test("0,999999999999 + 000,0000000003 =", "1,000000000299", "");
        test("123456789,987654321 + 987654321,123456789 =", "1 111 111 111,111111", "");
        test("1,111111111 + 2,999999999 =", "4,11111111", "");

        test("1,111111111 ± + 2,999999999 =", "1,888888888", "");
        test("1,111111111 ± + 2,999999999 ± ± =", "1,888888888", "");
        test("1,111111111 ± ± + 2,999999999 ± ± =", "4,11111111", "");
        test("1,111111111 + 2,999999999 ± =", "-1,888888888", "");
        test("1,111111111 + 2,999999999 ± ± =", "4,11111111", "");

        test("1 - * / + 2 =", "3", "");
        test("1 - * / + + - + 2 =", "3", "");
        test("1 - * / + + - + 2 =", "3", "");
        test("123456789 * / + + - + 2 =", "123 456 791", "");
        test("123456789 * / + = =", "370 370 367", "");

        test("999 ± + = = = =", "-4 995", "");
        test("191919 + 2 ± =", "191 917", "");
        test("0,99 + 0,03 ± =", "0,96", "");
        test("35 ± + 23 ± =", "-58", "");

        test("2 + + + = = = =", "10", "");
        test("2 + = = = =", "10", "");

        test("1 - * / + + - + 2 +", "3", "1  +  2  +  ");
        test("1234567 + - + 2 ± +", "1 234 565", "1234567  +  -2  +  ");
        test("1 - * / + + - + 2 ± +", "-1", "1  +  -2  +  ");
        test("2 ± + + + +", "-2", "-2  +  ");

        //Test Subtract
        test("-", "0", "0  -  ");
        test("- =", "0", "");
        test("- 2 =", "-2", "");
        test("2 - 2 =", "0", "");
        test("2 - 3 =", "-1", "");
        test("2 - 2 = -", "0", "0  -  ");
        test("0 - = = =", "0", "");
        test("1 - = = =", "-2", "");
        test("2 - = = =", "-4", "");
        test("13 - = = =", "-26", "");
        test("7 - 3 =", "4", "");
        test("2 - 8 =", "-6", "");
        test("5 - 5 =", "0", "");
        test("99 - 5 =", "94", "");
        test("35 - 8 =", "27", "");
        test("234 - 123 =", "111", "");
        test("9999 - = = =", "-19 998", "");
        test("0,2 - 0,2 =", "0", "");
        test("0,2 ± - 0,2 =", "-0,4", "");
        test("2 - 3 - = =", "1", "");
        test("1 - 2 - 3 = =", "-7", "");
        test("1 - 2 - 3 = - 4 -", "-8", "-4  -  4  -  ");
        test("1 - 2 - 3 - 4 = =", "-12", "");
        test("2 - 3 - 4 - 5 = =", "-15", "");
        test("101 - 102 - 103 - 104 = =", "-312", "");
        test("102 - 103 - 104 - 105 = =", "-315", "");

        test("123456789 ± * / - 01111 =", "-123 457 900", "");
        test("00003456 + - 00002 ± =", "3 458", "");
        test("000 / - 2 =", "-2", "");
        test("2 - - 0000 =", "2", "");
        test("2 ± - + - 0,00000 =", "-2", "");
        test("2 - / - 0 =", "2", "");
        test("2 ± - * * - 0 =", "-2", "");
        test("0 - 2 ± =", "2", "");
        test("0 - 0002 ± =", "2", "");

        test("99 - 5 ± =", "104", "");
        test("99 - 5 ± ± ± =", "104", "");
        test("99 ± ± - 5 ± ± ± =", "104", "");
        test("35 ± - 8 ± =", "-27", "");
        test("99 + - 5 ± =", "104", "");
        test("35 ± * - 8 ± =", "-27", "");
        test("99 - - 5 ± =", "104", "");
        test("35 ± * / + - 8 ± =", "-27", "");

        test("2,5 - 0,5 =", "2", "");
        test("2,5 / - 0,5 =", "2", "");
        test("123456789,9876543 - 987654321,1234567 =", "-864 197 531,1358024", "");
        test("987654321,1234567 - 123456789,9876543 =", "864 197 531,1358024", "");
        test("987654321,1234567 + / * * - 987654321,1234567 ± =", "1 975 308 642,246913", "");
        test("123456789,1234567 ± - / * + + - 123456789,9876543 =", "-246 913 579,111111", "");

        test("999 ± - = = = =", "2 997", "");
        test("191919 + - 2 =", "191 917", "");
        test("191919 + + + + + - - - 2 =", "191 917", "");
        test("191919 * * * * / / / - 2 =", "191 917", "");
        test("11111 * * * * / / / - = = = = = = = = = = = = = =", "-144 443", "");

        //Test Multiply
        test("*", "0", "0  ×  ");
        test("* =", "0", "");
        test("* 2 =", "0", "");
        test("1 * 1 =", "1", "");
        test("2 * 2 =", "4", "");
        test("2 * 2 = *", "4", "4  ×  ");
        test("1 * * * 9 *", "9", "1  ×  9  ×  ");
        test("1 * * * 9 =", "9", "");
        test("1 * * * 9 = = = =", "6 561", "");
        test("1 + / * 2 =", "2", "");
        test("0 * = = =", "0", "");
        test("1 * = = =", "1", "");
        test("2 * = = =", "16", "");
        test("13 * = = =", "28 561", "");
        test("9999 * = = =", "9 996 000 599 960 001", "");
        test("98765432 * 9 =", "888 888 888", "");
        test("0,2 * 0,2 =", "0,04", "");
        test("0,2 ± * 0,2 =", "-0,04", "");
        test("0,2 ± + / - * 5 +", "-1", "-0,2  ×  5  +  ");
        test("0,3 ± ± ± + / - * 3 ± ± +", "-0,9", "-0,3  ×  3  +  ");
        test(", + / - * 5 ± ± =", "0", "");
        test(", 0 + / - * 6 =", "0", "");
        test("0,0 + / - * 7 =", "0", "");
        test("0 + * 8 =", "0", "");
        test(", + / - * 1 ± ± +", "0", "0  ×  1  +  ");
        test("0,0000 + - / * 2 =", "0", "");
        test("0 , + * 3 =", "0", "");
        test("012345 + / * 4 =", "49 380", "");
        test("012345 + / * 0005 +", "61 725", "12345  ×  5  +  ");
        test("2 * 3 * = =", "216", "");
        test("1 * 2 * 3 = =", "18", "");
        test("1 * 2 * 3 = * 4 *", "24", "6  ×  4  ×  ");
        test("1 * 2 * 3 * 4 = =", "96", "");
        test("2 * 3 * 4 * 5 = =", "600", "");
        test("101 * 102 * 103 * 104 = =", "11 476 922 496", "");
        test("102 * 103 * 104 * 105 = =", "12 046 179 600", "");
        test("987654321 * 987654321 =", "9,75461057789971e+17", "");
        test("9876543210987654 * 9876543210987654 =", "9,754610579850632e+31", "");
        test("9876543210987654 * 9876543210987654 * 9876543210987654 =", "9,63418328982521e+47", "");
        test("0,987654321 * 0,987654321 =", "0,975461057789971", "");

        //Test Divide
        test("/", "0", "0  ÷  ");
        test("/ 2 =", "0", "");
        test("2 / / / / ", "2", "2  ÷  ");
        test("0 / 2 =", "0", "");
        test("0 / 987654321 ± =", "0", "");
        test("0 ± / 987654321 ± =", "0", "");
        test("2 / 2 =", "1", "");
        test("10 / =", "1", "");
        test("10 / = = =", "0,01", "");
        test("10 / 10 =", "1", "");
        test("2 / 2 = /", "1", "1  ÷  ");
        test("1 / = = =", "1", "");
        test("2 / = = =", "0,25", "");
        test("2 / / / / = = =", "0,25", "");
        test("13 / = = =", "0,0059171597633136", "");
        test("9999 / = = =", "1,000200030004001e-8", "");
        test("0,2 / 0,2 =", "1", "");
        test("0,2 ± / 0,2 =", "-1", "");
        test("2 / 3 =", "0,6666666666666667", "");
        test("2 / 3 / = =", "1,5", "");
        test("3 / 9 =", "0,3333333333333333", "");
        test("9 / 5 =", "1,8", "");
        test("10 / 3 =", "3,333333333333333", "");
        test("10 / 3 = =", "1,111111111111111", "");
        test("10 / 3 = = =", "0,3703703703703704", "");
        test("19191919191 / 354834693643 =", "0,0540869298713756", "");
        test("1 / 2 / 3 =", "0,1666666666666667", "");
        test("1 / 2 / 3 = =", "0,0555555555555556", "");
        test("1 / 2 / 3 = / 4 /", "0,0416666666666667", "0,1666666666666667  ÷  4  ÷  ");
        test("1 / 2 / 3 / 4 = =", "0,0104166666666667", "");
        test("2 / 3 / 4 / 5 = =", "0,0066666666666667", "");
        test("101 / 102 / 103 / 104 = =", "8,888271227374158e-7", "");
        test("102 / 103 / 104 / 105 = =", "8,636763144391438e-7", "");

        test("91919191919 / = ", "1", "");
        test("91919191919 / = = =", "1,183552711030181e-22", "");

        test("00000 ± / 0000743278423 ± =", "0", "");
        test("0000743278423 ± / 0000743278423 =", "-1", "");
        test("438974723 / 438974723 ± =", "-1", "");
        test("438974723 ± / 438974723 =", "-1", "");
        test("7865947546 ± / 7865947546 ± =", "1", "");
        test("828742387 ± / 7865947546 ± =", "0,1053582396975725", "");

        test("1000000 / = = = = = = = = = = = = = = = = =", "1,e-96", "");
        test("1000000 / = = = = = = = = = = = = = = = = = = =", "1,e-108", "");
        test("1 / 99999999 = = = = = = = = = = =", "1,000000110000007e-88", "");
        test("1 / 99999999 = = = = = = = = = = = =", "1,000000120000008e-96", "");
        test("1 / 99999999 = = = = = = = = = = = = =", "1,000000130000009e-104", "");
        test("2 / 8888888888 = = = = = = = = = =", "6,49464205743146e-100", "");
        test("2 / 8888888888 = = = = = = = = = = = = = = = = = = = = = ", "2,372646119533627e-209", "");
        test("2 / 8888888888 = = = = = = = = = = = = = = = = = = = = = +", "2,372646119533627e-209", "2,372646119533627e-209  +  ");
        test("2 / 8888888888 = = = = = = = = = = = = = = = = = = = = = /", "2,372646119533627e-209", "2,372646119533627e-209  ÷  ");
        test("2 / 8888888888 = = = = = = = = = = = = = = = = = = = = = + / *", "2,372646119533627e-209", "2,372646119533627e-209  ×  ");
        test("2 / 8888888888 = = = = = = = = = = = = = = = = = = = = = + / * 2 / 2 =", "2,372646119533627e-209", "");
        test("2 / 8888888888 = = = = = = = = =", "5,773015161583996e-90", "");
    }

    @Test
    void testDifferentOperations() throws MyException {

        test("± / * - +", "0", "negate( 0 )  +  ");
        test("± / * - + %", "0", "negate( 0 )  +  0");
        test("/ * * / - + / *", "0", "0  ×  ");
        test("/ * * / - + / * % % ", "0", "0  ×  0");
        test("/ * * / - + / * % % =", "0", "");
        test("/ / + + - - / / * * - - + + / / * * = ± = = =", "0", "");
        test(", = = = = / - + * = ±", "0", "negate( 0 )");
        test(", = = * / - + = ±", "0", "negate( 0 )");
        test(", 0 , , = = - + / * = ±", "0", "negate( 0 )");
        test("0 , = = - + / * =", "0", "");
        test("0 , 0000000 = = / * * / - + / * =", "0", "");
        test("- = - +", "0", "0  +  ");

        test("4 / + =", "8", "");
        test("/ + 4 =", "4", "");
        test("4 + / =", "1", "");
        test("4 + / 2 =", "2", "");
        test("25 + 25 + 25 * 25 * 25 = =", "1 171 875", "");
        test("9876543210 + 123456789 / 123456789 * 123456789 - 123456789 ± =", "10 123 456 788", "");
        test("5 / 3 * 3 =", "5", "");
        test("2 + 5 / 3 * 3 =", "7", "");
        test("10 + 20 * 30 + 40 / 3 * 3 -", "940", "10  +  20  ×  30  +  40  ÷  3  ×  3  -  ");
        test("2 * 5 / 3 * 3 =", "10", "");
        test("10 / 3 *", "3,333333333333333", "10  ÷  3  ×  ");
        test("10 / 3 * sqr", "11,11111111111111", "10  ÷  3  ×  sqr( 3,333333333333333 )");
        test("9 / 3 * 3 =", "9", "");
        test("10 / 3 + 1 =", "4,333333333333333", "");

        test("2 ± + 2 * 2 -", "0", "-2  +  2  ×  2  -  ");
        test("2 + 2 + * 2 ± =", "-8", "");
        test("2 + 2 * 2 ± =", "-8", "");
        test("2 + 2 * 2 ± = =", "16", "");
        test("2 + 2 * 2 ± = = =", "-32", "");
        test("2 + 3 * 7 ± =", "-35", "");
        test("2 + 3 * 7 ± = =", "245", "");
        test("2 + 3 * 7 ± = = =", "-1 715", "");
        test("2 + 2 ± + * 2 = = = =", "0", "");
        test("2 + 2 * =", "16", "");
        test("2 + 2 * = = =", "256", "");
        test("23 + 12 * / 2 + = = =", "70", "");

        test("3 + 3 * 3 =", "18", "");
        test("4 + 4 - * 4 =", "32", "");
        test("5 + 5 - * 5 = = = =", "6 250", "");
        test("6 + 6 + * 6 + = = = =", "360", "");
        test("1 + 2 - 3 / 7 * 9 =", "0", "");
        test("1 ± + 2 - 3 / 7 * 9 =", "-2,571428571428571", "");
        test("7 ± + 8,8 * + 9,9 =", "11,7", "");
        test("1 ± + 2,2 * + 3,3 =", "4,5", "");
        test("1 / 2 * 13 ± + 1,1 * + 0,5 -", "-4,9", "1  ÷  2  ×  -13  +  1,1  +  0,5  -  ");

        test("/ 2 =", "0", "");
        test("2 = / =", "1", "");
        test("2 = / = =", "0,5", "");
        test("2 = / = = =", "0,25", "");
        test("2 = + - / / = = =", "0,25", "");
        test("2 + = + - / / = = =", "0,0625", "");
        test("1 + = = - +", "3", "3  +  ");
        test("1 / + = = 2 +", "2", "2  +  ");
        test("1 / + = = 2 + = ", "4", "");
        test("1 / + = = - + 2 = ", "5", "");
        test("2 + 2 = + 2 =", "6", "");
        test("1 / + = = - + 2 = = ", "7", "");
        test("2 + - / * + 2 = + + - - + 2 =", "6", "");
        test("19 = * =", "361", "");
        test("191919 + = - 2 =", "383 836", "");
        test("191919 + + + + + = - - - 2 =", "383 836", "");
        test("0 + 0,3 = = = = * 3 = = =", "32,4", "");
        test("000000 + 00000,3 = = = = * 00003 = = =", "32,4", "");
        test("0 , , , + 0 , , , , 7 = = * 000003 , , , , = = =", "37,8", "");
        test("/ 2 = = 2 + 3 = = =", "11", "");
        test("/ 2 = = 2 + 3 =", "5", "");
        test("/ 2 = = 2 / 3 =", "0,6666666666666667", "");
        test("/ 2 = = 2 / 3 = = =", "0,0740740740740741", "");
        test("/ 2 = = 2 / 3 = + 2 = =", "4,666666666666667", "");
    }

    @Test
    void testExtraActions() throws MyException {

        test("sqr + 5 sqr", "25", "sqr( 0 )  +  sqr( 5 )");

        test("4 sqrt + = =", "6", "");
        test("25 sqrt + = =", "15", "");

        test("0 sqrt sqrt sqrt = =", "0", "");
        test("1 sqrt sqrt sqrt = =", "1", "");
        test("16 sqrt sqrt sqrt = =", "1,414213562373095", "");
        test("256 sqrt sqrt sqrt = =", "2", "");

        test("0 sqr sqr sqr = =", "0", "");
        test("0 sqr sqr sqr", "0", "sqr( sqr( sqr( 0 ) ) )");
        test("1 sqr sqr sqr = =", "1", "");
        test("1 sqr sqr sqr", "1", "sqr( sqr( sqr( 1 ) ) )");
        test("16 sqr sqr sqr = =", "4 294 967 296", "");
        test("16 sqr sqr sqr", "4 294 967 296", "sqr( sqr( sqr( 16 ) ) )");

        test("5 sqrt sqr", "5", "sqr( √( 5 ) )");
        test("25 sqrt 16 sqrt + ", "4", "√( 16 )  +  ");

        test("25 sqrt + - * / ", "5", "√( 25 )  ÷  ");

        test("25 sqrt + 16 sqrt = =", "13", "");
        test("25 sqrt + 16 sqrt = sqrt", "3", "√( 9 )");
        test("25 sqrt + 16 sqrt = sqrt sqr", "9", "sqr( √( 9 ) )");
        test("25 sqrt + 16 sqrt + = =", "27", "");

        test("25 sqrt - 16 sqrt = =", "-3", "");
        test("25 sqrt - 16 sqrt - = =", "-1", "");

        test("16 sqrt + sqrt = =", "8", "");
        test("16 sqr + sqr = =", "131 328", "");

        test("1 + 2 sqr * 3 = =", "45", "");
        test("1 + 4 sqrt * 3 = =", "27", "");
        test("1 + 4 1/x * 3 = =", "11,25", "");
        test("9999999999999999 1/x 1/x", "9 999 999 999 999 999", "1/( 1/( 9999999999999999 ) )");
        test("10 + 1/x = 1/x", "0,099009900990099", "1/( 10,1 )");
        test("9999999999999999 1/x 1/x = 1/x", "0,0000000000000001", "1/( 9999999999999999 )");

        test("5 sqrt sqr - 4 sqrt -", "3", "sqr( √( 5 ) )  -  √( 4 )  -  ");
        test("5 sqrt sqr sqrt sqr - 4 sqrt -", "3", "sqr( √( sqr( √( 5 ) ) ) )  -  √( 4 )  -  ");

        test("5 sqrt sqr - 3 sqrt sqr -", "2", "sqr( √( 5 ) )  -  sqr( √( 3 ) )  -  ");
        test("5 sqrt sqr - 3 sqrt sqr = =", "-1", "");

        test("5 sqrt sqr sqrt sqr - 3 sqrt sqr sqrt sqr -", "2", "sqr( √( sqr( √( 5 ) ) ) )  -  sqr( √( sqr( √( 3 ) ) ) )  -  ");
        test("5 sqrt sqr sqrt sqr - 3 sqrt sqr sqrt sqr = =", "-1", "");
        test("5 sqrt sqr sqrt sqr + 3 sqrt sqr sqrt sqr +", "8", "sqr( √( sqr( √( 5 ) ) ) )  +  sqr( √( sqr( √( 3 ) ) ) )  +  ");
        test("5 sqrt sqr sqrt sqr + 3 sqrt sqr sqrt sqr = =", "11", "");
    }

    @Test
    void testNegate() throws MyException {

        test("0 ± -", "0", "0  -  ");
        test("± -", "0", "negate( 0 )  -  ");
        test("5 ± ±", "5", "");
        test("5 ± ± ± -", "-5", "-5  -  ");
        test("5 ± ± ± - 6 - ±", "11", "-5  -  6  -  negate( -11 )");
        test("5 ± ± ± - 6 - ± ±", "-11", "-5  -  6  -  negate( negate( -11 ) )");
        test("5 ± ± ± - 6 - ± ± ±", "11", "-5  -  6  -  negate( negate( negate( -11 ) ) )");

        test("20 ±", "-20", "");
        test("20 + ±", "-20", "20  +  negate( 20 )");
        test("20 + 20 = ±", "-40", "negate( 40 )");
        test("20 + 20 = ± + 20 -", "-20", "negate( 40 )  +  20  -  ");

        test("1 ± + = =", "-3", "");
        test("1 ± - = =", "1", "");
        test("1 ± * = =", "-1", "");
        test("1 ± / = =", "-1", "");
        test("2 ± ± ± ± + = = = =", "10", "");
        test("2 ± + = = = =", "-10", "");

        test("1 ± ± ± = =", "-1", "");
        test("16 ± ± ± = =", "-16", "");
        test("256 ± ± ± = =", "-256", "");

        test("1 ± +", "-1", "-1  +  ");
        test("1 ± + 2 ±", "-2", "-1  +  ");
        test("1 ± + 2 ± +", "-3", "-1  +  -2  +  ");
        test("1 ± + 2 ± + 3 ±", "-3", "-1  +  -2  +  ");
        test("1 ± + 2 ± + 3 ± +", "-6", "-1  +  -2  +  -3  +  ");
        test("1 ± + 2 ± + 3 ± + 4 ±", "-4", "-1  +  -2  +  -3  +  ");
        test("1 ± + 2 ± + 3 ± + 4 ± =", "-10", "");
        test("1 ± + 2 ± + 3 ± + 4 ± = =", "-14", "");
        test("2 ± + 3 ± + 4 ± + 5 ± = =", "-19", "");
        test("101 ± + 102 ± + 103 ± + 104 ± = =", "-514", "");
        test("102 ± + 103 ± + 104 ± + 105 ± = =", "-519", "");
        test("1 ± + 2 + 3 ± + 4 = =", "6", "");
        test("2 + 3 ± + 4 + 5 ± = =", "-7", "");
        test("101 ± + 102 + 103 ± + 104 = =", "106", "");
        test("102 + 103 ± + 104 + 105 ± = =", "-107", "");

        test("1 ± - 2 ± - 3 ± - 4 ± = =", "12", "");
        test("2 ± - 3 ± - 4 ± - 5 ± = =", "15", "");
        test("101 ± - 102 ± - 103 ± - 104 ± = =", "312", "");
        test("102 ± - 103 ± - 104 ± - 105 ± = =", "315", "");
        test("1 ± - 2 - 3 ± - 4 = =", "-8", "");
        test("2 - 3 ± - 4 - 5 ± = =", "11", "");
        test("101 ± - 102 - 103 ± - 104 = =", "-308", "");
        test("102 - 103 ± - 104 - 105 ± = =", "311", "");

        test("1 ± * 9 =", "-9", "");
        test("1 ± * 2 ± * 3 ± * 4 ± = =", "-96", "");
        test("2 ± * 3 ± * 4 ± * 5 ± = =", "-600", "");
        test("101 ± * 102 ± * 103 ± * 104 ± = =", "-11 476 922 496", "");
        test("102 ± * 103 ± * 104 ± * 105 ± = =", "-12 046 179 600", "");
        test("1 ± * 2 * 3 ± * 4 = =", "96", "");
        test("2 * 3 ± * 4 * 5 ± = =", "-600", "");
        test("101 ± * 102 * 103 ± * 104 = =", "11 476 922 496", "");
        test("102 * 103 ± * 104 * 105 ± = =", "-12 046 179 600", "");
        test("98765432 ± * 9 ± =", "888 888 888", "");
        test("98765432 ± * 9 =", "-888 888 888", "");

        test("1 ± / 2 ± / 3 ± / 4 ± = =", "-0,0104166666666667", "");
        test("2 ± / 3 ± / 4 ± / 5 ± = =", "-0,0066666666666667", "");
        test("101 ± / 102 ± / 103 ± / 104 ± = =", "-8,888271227374158e-7", "");
        test("102 ± / 103 ± / 104 ± / 105 ± = =", "-8,636763144391438e-7", "");
        test("1 ± / 2 / 3 ± / 4 = =", "0,0104166666666667", "");
        test("2 / 3 ± / 4 / 5 ± = =", "-0,0066666666666667", "");
        test("101 ± / 102 / 103 ± / 104 = =", "8,888271227374158e-7", "");
        test("102 / 103 ± / 104 / 105 ± = =", "-8,636763144391438e-7", "");
    }

    @Test
    void testPercent() throws MyException {

        test("%", "0", "0");
        test("0 % % %", "0", "0");
        test("0 % =", "0", "");
        test("1 % % %", "0", "0");
        test("10 % % % % % % % % % % % % % % % % % % % % % % % % % % % % % % % % % %", "0", "0");
        test("1 %", "0", "0");
        test("20 %", "0", "0");
        test("1 %", "0", "0");
        test("20 %", "0", "0");
        test("9999999999999999 % =", "0", "");
        test("9999999999999999 % = %", "0", "0");
        test("9999999999999999 % = % %", "0", "0");

        test("20 + 0 %", "0", "20  +  0");
        test("20 + 20 % = %", "5,76", "5,76");

        test("20 + 10 % = =", "24", "");
        test("20 + 10 %", "2", "20  +  2");
        test("20 - 10 % = =", "16", "");
        test("20 - 10 %", "2", "20  -  2");
        test("20 * 10 % = =", "80", "");
        test("20 * 10 %", "2", "20  ×  2");
        test("20 / 10 % = =", "5", "");
        test("20 / 10 %", "2", "20  ÷  2");

        test("20 + % = =", "28", "");
        test("20 + %", "4", "20  +  4");
        test("20 - % = =", "12", "");
        test("20 - %", "4", "20  -  4");
        test("20 * % = =", "320", "");
        test("20 * %", "4", "20  ×  4");
        test("20 / % = =", "1,25", "");
        test("20 / %", "4", "20  ÷  4");

        test("20 + 10 % + 15 % = =", "28,6", "");
        test("20 - 10 % - 15 % = =", "12,6", "");
        test("20 * 10 % * 15 % = =", "1 440", "");
        test("20 / 10 % / 15 % = =", "4,444444444444444", "");

        test("20 + 10 % + = =", "66", "");
        test("20 - 10 % - = =", "-18", "");
        test("20 * 10 % * = =", "64 000", "");
        test("20 / 10 % / = =", "0,1", "");
        test("2 + 3 - 4 * 5 / 6 % =", "16,66666666666667", "");
        test("3 % + 4 - 5 * 6 ± / 7 %", "0,42", "0  +  4  -  5  ×  -6  ÷  0,42");
        test("3 % + 4 - 5 * 6 ± / 7 % =", "14,28571428571429", "");
        test("10 % + 9 % - 8 + 7 * 6 % =", "0,06", "");

        test("20 ± + %", "4", "-20  +  4");
        test("20 ± + % =", "-16", "");
        test("20 + 50 % = =", "40", "");
        test("20 + 50 % = = =", "50", "");

        test("20 - %", "4", "20  -  4");
        test("20 - % = = =", "8", "");
        test("20 - 50 % = = =", "-10", "");
        test("20 ± - %", "4", "-20  -  4");
        test("20 ± - % =", "-24", "");
        test("20 ± - + * - %", "4", "-20  -  4");
        test("20 ± - 50 % = = =", "10", "");

        test("20 / % =", "5", "");
        test("20 / % = = =", "0,3125", "");
        test("20 / 50 % =", "2", "");
        test("20 / 50 % = =", "0,2", "");
        test("20 / 1234567 % =", "8,100005913004316e-5", "");

        test("20 ± % % =", "0", "");
        test("20 ± + 10 ± % % =", "-20,4", "");
        test("20 ± % ± % =", "0", "");
        test("20 ± % ± % ± =", "0", "");
        test("20 ± ± ± % ± % ± =", "0", "");
        test("1234567899876543 ± ± ± % ± % ± =", "0", "");
        test("1234567899876543 ± ± ± % ± % ± = = =", "0", "");

        test("320 - 20 % ", "64", "320  -  64");
        test("320 - 20 % =", "256", "");
        test("320 * 20 % =", "20 480", "");
        test("320 * + / - % =", "-704", "");
        test("320 + - % =", "-704", "");
        test("320 ± - % = =", "-2 368", "");

        test("12345 / 2 %", "246,9", "12345  ÷  246,9");
        test("12345 / 2 % =", "50", "");
        test("2 / 12345 %", "246,9", "2  ÷  246,9");
        test("2 / 12345 % =", "0,0081004455245038", "");

        test("25 + sqrt %","1,25","25  +  1,25");
        test("25 + sqrt ± %","-1,25","25  +  -1,25");
        test("25 sqr + sqrt ± % % %","-6 103,515625","sqr( 25 )  +  -6103,515625");
    }

    @Test
    void testScalingAndRounding() throws MyException {

        test("10 / 9 * 0,1 *", "0,1111111111111111", "10  ÷  9  ×  0,1  ×  ");
        test("10 / 11 =", "0,9090909090909091", "");
        test("1000000000000000 / 1111111111111111 =", "0,9000000000000001", "");
        test("1,999999999999999 * 0,1 = ", "0,1999999999999999", "");
        test("1,999999999999999 * 0,1 = =", "0,02", "");

        test("1 / 3 /", "0,3333333333333333", "1  ÷  3  ÷  ");
        test("3 1/x", "0,3333333333333333", "1/( 3 )");
        test("1 / 6 /", "0,1666666666666667", "1  ÷  6  ÷  ");
        test("1 / 3 * 3 +", "1", "1  ÷  3  ×  3  +  ");
        test("1 / 3 * 3 - 1 =", "0", "");
//        test("1 / 3 * 10000000000 - 3333333333 * 10000000000 - 3333333333 * 10000000000 - 3333333333 * 10000000000 - 3333333333 * 10000000000 - 3333333333 * 10000000000 - 3333333333 * 3 - 1 =", "0", "");


        test("2 sqrt", "1,414213562373095", "√( 2 )");
        test("3 sqrt", "1,732050807568877", "√( 3 )");
        test("10 / 9 /", "1,111111111111111", "10  ÷  9  ÷  ");
        test("10 / 3 * ", "3,333333333333333", "10  ÷  3  ×  ");
        test("20 sqrt", "4,472135954999579", "√( 20 )");
        test("30 sqrt", "5,477225575051661", "√( 30 )");

        test("100 / 9 /", "11,11111111111111", "100  ÷  9  ÷  ");
        test("100 / 3 * ", "33,33333333333333", "100  ÷  3  ×  ");
        test("200 sqrt", "14,14213562373095", "√( 200 )");
        test("30 sqrt", "5,477225575051661", "√( 30 )");

        test("1000000000000000 / 9 /", "111 111 111 111 111,1", "1000000000000000  ÷  9  ÷  ");
        test("100000000000000 / 9 /", "11 111 111 111 111,11", "100000000000000  ÷  9  ÷  ");

        test("1000000000000000 ± / 9 =", "-111 111 111 111 111,1", "");
        test("100000000000000 ± / 9 =", "-11 111 111 111 111,11", "");

        test("1000000000000000 ± / 9 /", "-111 111 111 111 111,1", "-1000000000000000  ÷  9  ÷  ");
        test("100000000000000 ± / 9 /", "-11 111 111 111 111,11", "-100000000000000  ÷  9  ÷  ");

        test("9999999999 * 9999999999 * 99 / 9999999999999999 =", "989 999,9998020001", "");

    }

    @Test
    void testEMinusValues() throws MyException {

        test("1 * 0,1 =", "0,1", "");
        test("1 * 0,1 = =", "0,01", "");
        test("1 * 0,1 = = =", "0,001", "");
        test("1 * 0,1 = = = =", "0,0001", "");
        test("1 * 0,1 = = = = =", "0,00001", "");
        test("25 sqrt sqr sqrt sqr 1/x", "0,04", "1/( sqr( √( sqr( √( 25 ) ) ) ) )");

        test("1 / 1000000000000000 = ", "0,000000000000001", "");
        test("1 / 1000000000000000 / 10 = ", "0,0000000000000001", "");
        test("1 / 1000000000000000 / 10 = =", "1,e-17", "");
        test("1 / 1000000000000000 / 10 = = *", "1,e-17", "1,e-17  ×  ");
        test("1 / 1000000000000000 = =", "1,e-30", "");
        test("22 / 100000000 =", "0,00000022", "");
        test("22 / 1000000000 =", "0,000000022", "");
        test("22 / 10000000000 =", "0,0000000022", "");
        test("22 / 100000000000 =", "0,00000000022", "");
        test("22 / 1000000000000 =", "0,000000000022", "");
        test("22 / 10000000000000 =", "0,0000000000022", "");
        test("22 / 100000000000000 =", "0,00000000000022", "");
        test("22 / 1000000000000000 =", "0,000000000000022", "");
        test("22 / 1000000000000000 / 10 =", "0,0000000000000022", "");
        test("22 / 1000000000000000 / 10 = =", "2,2e-16", "");
        test("22 / 1000000000000000 / 10 = = =", "2,2e-17", "");
        test("22 / 1000000000000000 = =", "2,2e-29", "");

        test("333 / 1000000000000000 = =", "3,33e-28", "");
        test("333 / 1000000000000000 = =", "3,33e-28", "");

        test("1 / 1000000000000000 " + addEquals(100), "1,e-1500", "");
        test("1 / 1000000000000000 " + addEquals(666), "1,e-9990", "");
        test("1 / 10 " + addEquals(9999), "1,e-9999", "");

        test("0,0000000000000023 * 0,1 =", "2,3e-16", "");
        test("0,0000000000000234 * 0,1 =", "2,34e-15", "");
        test("0,0000000000002345 * 0,1 =", "2,345e-14", "");
        test("0,0000000000023456 * 0,1 =", "2,3456e-13", "");
        test("0,0000000000234567 * 0,1 =", "2,34567e-12", "");
        test("0,0000000002345678 * 0,1 =", "2,345678e-11", "");
        test("0,0000000023456789 * 0,1 =", "2,3456789e-10", "");
        test("0,0000000234567891 * 0,1 =", "2,34567891e-9", "");
        test("0,0000002345678912 * 0,1 =", "2,345678912e-8", "");
        test("0,0000023456789123 * 0,1 =", "2,3456789123e-7", "");
        test("0,0000123456789123 * 0,1 =", "1,23456789123e-6", "");
        test("0,0001234567891234 * 0,1 =", "1,234567891234e-5", "");
        test("0,0012345678912345 * 0,1 =", "1,2345678912345e-4", "");
        test("0,0123456789123456 * 0,1 =", "0,0012345678912346", "");
        test("0,1234567891234567 * 0,1 =", "0,0123456789123457", "");
        test("1,234567891234567 * 0,1 =", "0,1234567891234567", "");
        test("0,0001111111111111 * 0,1 =", "1,111111111111e-5", "");

        test("0,1111111111111111 * 0,1 =", "0,0111111111111111", "");
        test("0,1111111111111111 * 0,1 = =", "0,0011111111111111", "");
        test("0,1111111111111111 * 0,1 = = =", "1,111111111111111e-4", "");
        test("0,1111111111111111 * 0,1 = = = =", "1,111111111111111e-5", "");
        test("0,1111111111111111 * 0,1 = = = = =", "1,111111111111111e-6", "");
        test("0,1111111111111111 * 0,1 = = = = = =", "1,111111111111111e-7", "");

        test("0,0000000000000001 sqr sqr sqr sqr sqr sqr sqr sqr sqr", "1,e-8192", "sqr( sqr( sqr( sqr( sqr( sqr( sqr( sqr( sqr( 0,0000000000000001 ) ) ) ) ) ) ) ) )");

        test("5 / 10000 / 3 =", "1,666666666666667e-4", "");
        test("5 / 3 = = = = / 100 =", "6,17283950617284e-4", "");
    }

    @Test
    void testEPlusValues() throws MyException {

        test("1000000000000000 + ", "1 000 000 000 000 000", "1000000000000000  +  ");
        test("1000000000000000 * 10 *", "1,e+16", "1000000000000000  ×  10  ×  ");
        test("1000000000000000 * 10 * 10 *", "1,e+17", "1000000000000000  ×  10  ×  10  ×  ");
        test("1000000000000000 * 10 * 10 / 10 /", "1,e+16", "1000000000000000  ×  10  ×  10  ÷  10  ÷  ");
        test("1000000000000000 + = = = = = = = = =", "1,e+16", "");
        test("1000000000000000 + = = = = = = = = = + 1 =", "1,e+16", "");
        test("1000000000000000 * 10 " + addEquals(500), "1,e+515", "");
        test("1000000000000000 * 10 " + addEquals(5000), "1,e+5015", "");
        test("1000000000000000 * 10 " + addEquals(9983), "1,e+9998", "");
        test("1000000000000000 * 10 " + addEquals(9984), "1,e+9999", "");

        test("1000000000000000 * =", "1,e+30", "");
        test("1000000000000000 * = *", "1,e+30", "1,e+30  ×  ");
        test("256 sqr sqr sqr = =", "1,844674407370955e+19", "");
        test("9999999999999999 + " + addEquals(20), "2,1e+17", "");
    }

    @Test
    void testClear() throws MyException {

        test("5 - 3 = C - 2 -", "-2", "0  -  2  -  ");
        test("5 - 2333 CE", "0", null);

    }

    @Test
    void testClearEntered() throws MyException {

//        test("5 ± ± ± - 6 - ± ± ± CE 25 -", "-11", "-5  -  6  -  25  -  ");
    }

    @Test
    void testInvalidValues() throws MyException {

        test("/ =");
        test("1 ± sqrt");
        test("0 1/x");
        test("/ , , -");
        test("+ / 0 -");
        test(", = + / , , -");
        test(", , , , , = = , , , , , = = + - * / , , , , , -");
        test("1 / 10 " + addEquals(10000));
        test("1 * 10 " + addEquals(10000));
        test("1 / 1000000000000000 " + addEquals(667));
        test("1 * 0,000000000000001 " + addEquals(667));
        test("1000000000000000 * " + addEquals(667));
        test("0,0000000000000001 sqr sqr sqr sqr sqr sqr sqr sqr sqr sqr");

    }

    private void test(String expression, String display, String history) throws MyException {

        ResponseDTO response = prepareTest(expression);
        assertEquals(display, convertToString(response.getDisplayNumber(), DISPLAY_PATTERN));
        assertEquals(history, response.getHistory());
        controller.handleAction(new Clear());
    }


    private void test(String expression) throws MyException {

        assertThrows(MyException.class, () -> prepareTest(expression));
        controller.handleAction(new Clear());
    }

    private ResponseDTO prepareTest(String expression) throws MyException {

        String[] parsedActionStrings = expression.split(" ");
        ResponseDTO response = null;
        for (String str : parsedActionStrings) {
            if (str.matches(IS_DIGIT_REGEX) || COMA.equals(str)) {
                for (char ch : str.toCharArray()) {
                    response = controller.handleAction(actions.get(ch + ""));
                }
            } else {
                response = controller.handleAction(actions.get(str));
            }
        }
        return response;
    }

    private String addEquals(int count) {

        StringBuilder result = new StringBuilder("=");
        for (int i = 1; i < count; i++) {
            result.append(" =");
        }
        return result.toString();
    }
}