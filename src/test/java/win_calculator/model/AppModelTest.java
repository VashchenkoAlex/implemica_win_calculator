package win_calculator.model;

import org.junit.jupiter.api.Test;
import win_calculator.DTOs.ResponseDTO;
import win_calculator.exceptions.MyException;
import win_calculator.model.nodes.actions.Action;
import win_calculator.model.nodes.actions.clear.BaskSpace;
import win_calculator.model.nodes.actions.clear.Clear;
import win_calculator.model.nodes.actions.clear.ClearDisplay;
import win_calculator.model.nodes.actions.digits.Number;
import win_calculator.model.nodes.actions.enter.Enter;
import win_calculator.model.nodes.actions.extra_operations.*;
import win_calculator.model.nodes.actions.main_operations.*;

import java.math.BigDecimal;
import java.util.HashMap;

import static org.junit.gen5.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

class AppModelTest {

    private AppModel appModel = new AppModel();
    private static final HashMap<String,Action> actions = createMap();
    private static HashMap<String,Action> createMap(){

        HashMap<String,Action> map = new HashMap<>();
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
        map.put("negate",new Negate());
        return map;
    }

    @Test
    void testMainActionOnce() throws MyException {

        test("2 + 2 =","4","");
        test("2 - 2 =","0","");
        test("2 * 2 =","4","");
        test("2 / 2 =","1,0000000000000000","");

        test("0 + = = =","0","");
        test("1 + = = =","4","");
        test("2 + = = =","8","");
        test("13 + = = =","52","");
        test("9999 + = = =","39 996","");

        test("0 - = = =","0","");
        test("1 - = = =","-2","");
        test("2 - = = =","-4","");
        test("13 - = = =","-26","");
        test("9999 - = = =","-19 998","");

        test("0 * = = =","0","");
        test("1 * = = =","1","");
        test("2 * = = =","16","");
        test("13 * = = =","28 561","");
        test("9999 * = = =","9 996 000 599 960 001","");

        test("1 / = = =","1,0000000000000000","");
        test("2 / = = =","0,2500000000000000","");
        test("13 / = = =","0,0059171597633136","");

        test("0.2 + 0.2 =","0,4","");
        test("-0.2 + 0.2 =","0,0","");
    }

    @Test
    void testMainActionTwice() throws MyException {

        test("2 + 3 + = =","15","");
        test("2 - 3 - = =","1","");
        test("2 * 3 * = =","216","");
        test("2 / 3 / = =","1,4999999999999999","");
        test("1 + 2 + 3 = =","9","");
        test("1 - 2 - 3 = =","-7","");
        test("1 * 2 * 3 = =","18","");
        test("1 / 2 / 3 = =","0,0555555555555556","");

    }

    @Test
    void testMainOperationTrice() throws MyException{

        //test positive values for Plus
        test("1 + 2 + 3 + 4 = =","14","");
        test("2 + 3 + 4 + 5 = =","19","");
        test("101 + 102 + 103 + 104 = =","514","");
        test("102 + 103 + 104 + 105 = =","519","");

        //test negative values for Plus
        test("-1 + -2 + -3 + -4 = =","-14","");
        test("-2 + -3 + -4 + -5 = =","-19","");
        test("-101 + -102 + -103 + -104 = =","-514","");
        test("-102 + -103 + -104 + -105 = =","-519","");

        //test different sign for Plus
        test("-1 + 2 + -3 + 4 = =","6","");
        test("2 + -3 + 4 + -5 = =","-7","");
        test("-101 + 102 + -103 + 104 = =","106","");
        test("102 + -103 + 104 + -105 = =","-107","");

        //test positive values for Minus
        test("1 - 2 - 3 - 4 = =","-12","");
        test("2 - 3 - 4 - 5 = =","-15","");
        test("101 - 102 - 103 - 104 = =","-312","");
        test("102 - 103 - 104 - 105 = =","-315","");

        //test negative values for Minus
        test("-1 - -2 - -3 - -4 = =","12","");
        test("-2 - -3 - -4 - -5 = =","15","");
        test("-101 - -102 - -103 - -104 = =","312","");
        test("-102 - -103 - -104 - -105 = =","315","");

        //test different sign for Minus
        test("-1 - 2 - -3 - 4 = =","-8","");
        test("2 - -3 - 4 - -5 = =","11","");
        test("-101 - 102 - -103 - 104 = =","-308","");
        test("102 - -103 - 104 - -105 = =","311","");

        //test positive values for Multiply
        test("1 * 2 * 3 * 4 = =","96","");
        test("2 * 3 * 4 * 5 = =","600","");
        test("101 * 102 * 103 * 104 = =","11 476 922 496","");
        test("102 * 103 * 104 * 105 = =","12 046 179 600","");

        //test negative values for Multiply
        test("-1 * -2 * -3 * -4 = =","-96","");
        test("-2 * -3 * -4 * -5 = =","-600","");
        test("-101 * -102 * -103 * -104 = =","-11 476 922 496","");
        test("-102 * -103 * -104 * -105 = =","-12 046 179 600","");

        //test different sign for Multiply
        test("-1 * 2 * -3 * 4 = =","96","");
        test("2 * -3 * 4 * -5 = =","-600","");
        test("-101 * 102 * -103 * 104 = =","11 476 922 496","");
        test("102 * -103 * 104 * -105 = =","-12 046 179 600","");

        //test positive values for Divide
        test("1 / 2 / 3 / 4 = =","0,0104166666666667","");
        test("2 / 3 / 4 / 5 = =","0,0066666666666667","");
        //test("101 / 102 / 103 / 104 = =","11 476 922 496","");
        //test("102 / 103 / 104 / 105 = =","12 046 179 600","");

        //test negative values for Divide
        test("-1 / -2 / -3 / -4 = =","-0,0104166666666667","");
        test("-2 / -3 / -4 / -5 = =","-0,0066666666666667","");
        //test("-101 / -102 / -103 / -104 = =","-11 476 922 496","");
        //test("-102 / -103 / -104 / -105 = =","-12 046 179 600","");

        //test different sign for Divide
        test("-1 / 2 / -3 / 4 = =","0,0104166666666667","");
        test("2 / -3 / 4 / -5 = =","-0,0066666666666667","");
        //test("-101 / 102 / -103 / 104 = =","11 476 922 496","");
        //test("102 / -103 / 104 / -105 = =","12 046 179 600","");
    }

    @Test
    void testDifferentMainOperations() throws MyException {

        test("25 + 25 + 25 * 25 * 25 = =","1 171 875","");
    }

    @Test
    void testExtraActions() throws MyException{

        test("4 sqrt + = =","6,000000000000000","");
        test("25 sqrt + = =","15,000000000000000","");

        test("0 sqrt sqrt sqrt = =","0","");
        test("1 sqrt sqrt sqrt = =","1,000000000000000","");
        test("16 sqrt sqrt sqrt = =","1,414213562373095","");
        test("256 sqrt sqrt sqrt = =","2,000000000000000","");

    }

    @Test
    void testPercent() throws MyException {

        test("%","0","0");
        test("1 %","0","0");
        test("20 %","0","0");
        test("-1 %","0","0");
        test("-20 %","0","0");

        test("20 + 10 % = =","24,0000000000000000","");
        test("20 - 10 % = =","16,0000000000000000","");
        test("20 * 10 % = =","80,00000000000000000000000000000000","");
        test("20 / 10 % = =","5,0000000000000000","");

        test("20 + % = =","28,0000000000000000","");
        test("20 - % = =","12,0000000000000000","");
        test("20 * % = =","320,00000000000000000000000000000000","");
        test("20 / % = =","1,2500000000000000","");

        test("20 + 10 % + 15 % = =","28,60000000000000000000000000000000","");
        test("20 - 10 % - 15 % = =","12,60000000000000000000000000000000","");
        test("20 * 10 % * 15 % = =","1 440,00000000000000000000000000000000000000000000000000000000000000000000000000000000","");
        test("20 / 10 % / 15 % = =","4,4444444444444445","");

        test("20 + 10 % + = =","66,0000000000000000","");
        test("20 - 10 % - = =","-18,0000000000000000","");
        test("20 * 10 % * = =","64 000,000000000000000000000000000000000000000000000000","");
        test("20 / 10 % / = =","0,1000000000000000","");

        test("0 % % %","0E-32","0");
    }

    @Test
    void testNegate() throws MyException {

        test("1 negate + = =","-3","");
        test("1 negate - = =","1","");
        test("1 negate * = =","-1","");
        test("1 negate / = =","-1,0000000000000000","");
    }

    @Test
    void testInvalidValues() throws MyException {

        test("-1 sqrt");
        test("0 1/x");
    }

    private void test(String expression, String display,String history) throws MyException {

        String[] parsedActionStrings = expression.split(" ");
        Number currentNumber = null;
        ResponseDTO responseDTO = null;
        for (String str :parsedActionStrings) {
            if (str.matches("-?\\d+(\\.\\d+)?")){
                currentNumber = new Number(new BigDecimal(str));
            }else {
                responseDTO = appModel.toDo(actions.get(str),currentNumber);
                currentNumber = null;
            }
        }
        assert responseDTO != null;
        assertEquals(display,responseDTO.getDisplayNumber());
        assertEquals(history,responseDTO.getHistory());
        appModel.toDo(new Clear(),null);
    }

    private void test(String expression) throws MyException {

        String[] parsedActionStrings = expression.split(" ");
        assertThrows(MyException.class,()->appModel.toDo(actions.get(parsedActionStrings[1]),new Number(new BigDecimal(parsedActionStrings[0]))));
        appModel.toDo(new Clear(),null);
    }
}