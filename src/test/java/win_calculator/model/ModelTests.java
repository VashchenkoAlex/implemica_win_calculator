package win_calculator.model;

import org.junit.jupiter.api.Test;
import win_calculator.model.exceptions.OperationException;
import win_calculator.model.nodes.events.Event;

import java.math.BigDecimal;
import java.util.HashMap;

import static org.junit.gen5.api.Assertions.assertEquals;
import static org.junit.gen5.api.Assertions.assertThrows;
import static win_calculator.TestUtils.createMap;
import static win_calculator.controller.utils.ControllerUtils.convertToString;

class ModelTests {

    private static final HashMap<String, Event> events = createMap();
    private static final String IS_DIGIT_REGEX = "\\d+(,\\d+)?";
    private static final String E = "e";
    private static final AppModel model = new AppModel();
    private static final String DISPLAY_PATTERN = "#############,###.################";

    @Test
    void testMaxExponent() throws OperationException {

//        test("-9.999999999999999e+9999 - 1.e+9983 = = = =","-9,999999999999999e+9999");
        test("9.999999999999999e+9999 + 1.e+9983 = = = =","9,999999999999999e+9999");

        test("9.999999999999999e+9999 + 1.e+9983 = = = = =");
        test("9.999999999999999e+9999 + 1.e+9984 =");
    }

    private void test(String expression,String expected) throws OperationException {


        assertEquals(expected,convertToString(prepareTest(expression),DISPLAY_PATTERN));
    }

    private void test(String expression){

        assertThrows(OperationException.class,()->prepareTest(expression));
    }

    private BigDecimal prepareTest(String expression) throws OperationException {

        String[] parsedEventStrings = expression.split(" ");
        BigDecimal response = null;
        BigDecimal number = null;
        for (String str : parsedEventStrings) {
            if (str.matches(IS_DIGIT_REGEX) || str.contains(E) || str.contains(".")) {
                number = new BigDecimal(str);
            } else {
                response = model.toDo(number,events.get(str));
                number = null;
            }
        }
        return response;
    }
}
