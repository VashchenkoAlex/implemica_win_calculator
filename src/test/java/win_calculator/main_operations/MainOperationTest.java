package win_calculator.main_operations;

import org.junit.jupiter.api.Test;
import win_calculator.exceptions.MyException;
import win_calculator.model.nodes.actions.main_operations.*;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class MainOperationTest {

    private static final String FIFTY_UNITS = "11111111111111111111111111111111111111111111111111";
    private static final String FIFTY_NINES = "99999999999999999999999999999999999999999999999999";
    private static final String FIFTY_UNITS_THROUGH_DOT = "1111111111111111111111111.1111111111111111111111111";
    private static final String SIXTEEN_UNITS_AFTER_DOT = "0.1111111111111111";
    private static final String SIXTEEN_NINES_AFTER_DOT = "0.9999999999999999";
    private static final String FIFTY_NINES_THROUGH_DOT = "9999999999999999999999999.9999999999999999999999999";
    private Divide divide = new Divide();
    private Multiply multiply = new Multiply();
    private Plus plus = new Plus();
    private Minus minus = new Minus();
    private MainOperation currentOperation;

    @Test
    void calculateMultiplyWithOneParameter() throws MyException {

        currentOperation = multiply;

        //test whole positive number
        test("0","0");
        test("1","1");
        test("2","4");
        test("99","9801");
        test("100","10000");
        test(Long.MAX_VALUE+"","85070591730234615847396907784232501249");
        test(FIFTY_UNITS,"123456790123456790123456790123456790123456790123454320987654320987654320987654320987654320987654321");
        test(FIFTY_NINES,"9999999999999999999999999999999999999999999999999800000000000000000000000000000000000000000000000001");

        //test whole negative number
        test("-1","1");
        test("-2","4");
        test("-99","9801");
        test("-100","10000");
        test(Integer.MIN_VALUE+"","4611686018427387904");
        test(Long.MIN_VALUE+"","85070591730234615865843651857942052864");
        test("-"+FIFTY_UNITS,"123456790123456790123456790123456790123456790123454320987654320987654320987654320987654320987654321");
        test("-"+FIFTY_NINES,"9999999999999999999999999999999999999999999999999800000000000000000000000000000000000000000000000001");

        //test fractional positive number
        test("0.1","0.01");
        test("0.2","0.04");
        test("0.01","0.0001");
        test("0.02","0.0004");
        test("1.1","1.21");
        test("2.2","4.84");
        test("99.99","9998.0001");
        test("100.01","10002.0001");
        test(SIXTEEN_UNITS_AFTER_DOT,"0.01234567901234567654320987654321");
        test(SIXTEEN_NINES_AFTER_DOT,"0.99999999999999980000000000000001");
        test(Double.MAX_VALUE+"","3.23170060713109998320439596646649E+616");
        test(FIFTY_UNITS_THROUGH_DOT,"1234567901234567901234567901234567901234567901234.54320987654320987654320987654320987654320987654321");
        test(FIFTY_NINES_THROUGH_DOT,"99999999999999999999999999999999999999999999999998.00000000000000000000000000000000000000000000000001");

        //test fractional negative number
        test("-0.1","0.01");
        test("-0.2","0.04");
        test("-0.01","0.0001");
        test("-0.02","0.0004");
        test("-1.1","1.21");
        test("-2.2","4.84");
        test("-99.99","9998.0001");
        test("-100.01","10002.0001");
        test("-"+SIXTEEN_UNITS_AFTER_DOT,"0.01234567901234567654320987654321");
        test("-"+SIXTEEN_NINES_AFTER_DOT,"0.99999999999999980000000000000001");
        test(Double.MIN_VALUE+"","2.401E-647");
        test("-"+FIFTY_UNITS_THROUGH_DOT,"1234567901234567901234567901234567901234567901234.54320987654320987654320987654320987654320987654321");
        test("-"+FIFTY_NINES_THROUGH_DOT,"99999999999999999999999999999999999999999999999998.00000000000000000000000000000000000000000000000001");

    } //DONE


    @Test
    void calculatePlusWithOneParameter() throws MyException {

        currentOperation = plus;

        //test whole positive number
        test("0","0");
        test("1","2");
        test("2","4");
        test("99","198");
        test("100","200");
        test(Long.MAX_VALUE+"","18446744073709551614");
        test(FIFTY_UNITS,"22222222222222222222222222222222222222222222222222");
        test(FIFTY_NINES,"199999999999999999999999999999999999999999999999998");

        //test whole negative number
        test("-1","-2");
        test("-2","-4");
        test("-99","-198");
        test("-100","-200");
        test(Integer.MIN_VALUE+"","-4294967296");
        test(Long.MIN_VALUE+"","-18446744073709551616");
        test("-"+FIFTY_UNITS,"-22222222222222222222222222222222222222222222222222");
        test("-"+FIFTY_NINES,"-199999999999999999999999999999999999999999999999998");

        //test fractional positive number
        test("0.1","0.2");
        test("0.2","0.4");
        test("0.01","0.02");
        test("0.02","0.04");
        test("1.1","2.2");
        test("2.2","4.4");
        test("99.99","199.98");
        test("100.01","200.02");
        test(SIXTEEN_UNITS_AFTER_DOT,"0.2222222222222222");
        test(SIXTEEN_NINES_AFTER_DOT,"1.9999999999999998");
        test(Double.MAX_VALUE+"","3.5953862697246314E+308");
        test(FIFTY_UNITS_THROUGH_DOT,"2222222222222222222222222.2222222222222222222222222");
        test(FIFTY_NINES_THROUGH_DOT,"19999999999999999999999999.9999999999999999999999998");

        //test fractional negative number
        test("-0.1","-0.2");
        test("-0.2","-0.4");
        test("-0.01","-0.02");
        test("-0.02","-0.04");
        test("-1.1","-2.2");
        test("-2.2","-4.4");
        test("-99.99","-199.98");
        test("-100.01","-200.02");
        test("-"+SIXTEEN_UNITS_AFTER_DOT,"-0.2222222222222222");
        test("-"+SIXTEEN_NINES_AFTER_DOT,"-1.9999999999999998");
        test(Double.MIN_VALUE+"","9.8E-324");
        test("-"+FIFTY_UNITS_THROUGH_DOT,"-2222222222222222222222222.2222222222222222222222222");
        test("-"+FIFTY_NINES_THROUGH_DOT,"-19999999999999999999999999.9999999999999999999999998");

    } //DONE

    @Test
    void calculateMinusWithOneParameter() throws MyException {

        currentOperation = minus;

        //test whole positive number
        test("0","0");
        test("1","0");
        test("2","0");
        test("99","0");
        test("100","0");
        test(Long.MAX_VALUE+"","0");
        test(FIFTY_UNITS,"0");
        test(FIFTY_NINES,"0");

        //test whole negative number
        test("-1","0");
        test("-2","0");
        test("99","0");
        test("100","0");
        test(Integer.MIN_VALUE+"","0");
        test(Long.MIN_VALUE+"","0");
        test("-"+FIFTY_UNITS,"0");
        test("-"+FIFTY_NINES,"0");

        //test fractional positive number
        test("0.1","0.0");
        test("0.2","0.0");
        test("0.01","0.00");
        test("0.02","0.00");
        test(SIXTEEN_UNITS_AFTER_DOT,"0E-16");
        test(SIXTEEN_NINES_AFTER_DOT,"0E-16");
        test(Double.MAX_VALUE+"","0E+292");
        test(FIFTY_UNITS_THROUGH_DOT,"0E-25");
        test(FIFTY_NINES_THROUGH_DOT,"0E-25");

        //test fractional negative number
        test("-0.1","0.0");
        test("-0.2","0.0");
        test("-0.01","0.00");
        test("-0.02","0.00");
        test("-"+SIXTEEN_UNITS_AFTER_DOT,"0E-16");
        test("-"+SIXTEEN_NINES_AFTER_DOT,"0E-16");
        test(Double.MIN_VALUE+"","0E-325");
        test("-"+FIFTY_UNITS_THROUGH_DOT,"0E-25");
        test("-"+FIFTY_NINES_THROUGH_DOT,"0E-25");
    } //DONE

    @Test
    void calculateDivideWithOneParameter() throws MyException {

        currentOperation = divide;

        //test whole positive number
        test("1","1.0000000000000000");
        test("2","1.0000000000000000");
        test("99","1.0000000000000000");
        test("100","1.0000000000000000");
        test(Long.MAX_VALUE+"","1.0000000000000000");
        test(FIFTY_UNITS,"1.0000000000000000");
        test(FIFTY_NINES,"1.0000000000000000");

        //test whole negative number
        test("-1","1.0000000000000000");
        test("-2","1.0000000000000000");
        test("-99","1.0000000000000000");
        test("-100","1.0000000000000000");
        test(Integer.MIN_VALUE+"","1.0000000000000000");
        test(Long.MIN_VALUE+"","1.0000000000000000");
        test("-"+FIFTY_UNITS,"1.0000000000000000");
        test("-"+FIFTY_NINES,"1.0000000000000000");

        //test fractional positive number
        test("0.1","1.0000000000000000");
        test("0.2","1.0000000000000000");
        test("0.01","1.0000000000000000");
        test("0.02","1.0000000000000000");
        test(SIXTEEN_UNITS_AFTER_DOT,"1.0000000000000000");
        test(SIXTEEN_NINES_AFTER_DOT,"1.0000000000000000");
        test(Double.MAX_VALUE+"","1.0000000000000000");
        test(FIFTY_UNITS_THROUGH_DOT,"1.0000000000000000");
        test(FIFTY_NINES_THROUGH_DOT,"1.0000000000000000");

        //test fractional negative number
        test("-0.1","1.0000000000000000");
        test("-0.2","1.0000000000000000");
        test("-0.01","1.0000000000000000");
        test("-0.02","1.0000000000000000");
        test("-"+SIXTEEN_UNITS_AFTER_DOT,"1.0000000000000000");
        test("-"+SIXTEEN_NINES_AFTER_DOT,"1.0000000000000000");
        test(Double.MIN_VALUE+"","1.0000000000000000");
        test("-"+FIFTY_UNITS_THROUGH_DOT,"1.0000000000000000");
        test("-"+FIFTY_NINES_THROUGH_DOT,"1.0000000000000000");
    } //DONE


    @Test
    void calculateMultiplyWithTwoParameters() throws MyException {

        currentOperation = multiply;

        //test 0 first number and second whole positive number
        test("0","0","0");
        test("0","1","0");
        test("0","2","0");
        test("0","99","0");
        test("0","100","0");
        test("0",Long.MAX_VALUE+"","0");
        test("0",FIFTY_UNITS,"0");
        test("0",FIFTY_NINES,"0");

        //test 0 first number and second whole negative number
        test("0","-1","0");
        test("0","-2","0");
        test("0","-99","0");
        test("0","-100","0");
        test("0",Integer.MIN_VALUE+"","0");
        test("0",Long.MIN_VALUE+"","0");
        test("0","-"+FIFTY_UNITS,"0");
        test("0","-"+FIFTY_NINES,"0");

        //test 0 first number and second fractional positive number
        test("0","0.1","0.0");
        test("0","0.2","0.0");
        test("0","0.01","0.00");
        test("0","0.02","0.00");
        test("0","1.1","0.0");
        test("0","2.2","0.0");
        test("0","99.99","0.00");
        test("0","100.01","0.00");
        test("0",SIXTEEN_UNITS_AFTER_DOT,"0E-16");
        test("0",SIXTEEN_NINES_AFTER_DOT,"0E-16");
        test("0",Double.MAX_VALUE+"","0E+292");
        test("0",FIFTY_UNITS_THROUGH_DOT,"0E-25");
        test("0",FIFTY_NINES_THROUGH_DOT,"0E-25");

        //test 0 first number and second fractional negative number
        test("0","-0.1","0.0");
        test("0","-0.2","0.0");
        test("0","-0.01","0.00");
        test("0","-0.02","0.00");
        test("0","-1.1","0.0");
        test("0","-2.2","0.0");
        test("0","-99.99","0.00");
        test("0","-100.01","0.00");
        test("0","-"+SIXTEEN_UNITS_AFTER_DOT,"0E-16");
        test("0","-"+SIXTEEN_NINES_AFTER_DOT,"0E-16");
        test("0",Double.MIN_VALUE+"","0E-325");
        test("0","-"+FIFTY_UNITS_THROUGH_DOT,"0E-25");
        test("0","-"+FIFTY_NINES_THROUGH_DOT,"0E-25");

        //test 1 first number and second whole positive number
        test("1","0","0");
        test("1","1","1");
        test("1","2","2");
        test("1","99","99");
        test("1","100","100");
        test("1",Long.MAX_VALUE+"","9223372036854775807");
        test("1",FIFTY_UNITS,"11111111111111111111111111111111111111111111111111");
        test("1",FIFTY_NINES,"99999999999999999999999999999999999999999999999999");

        //test 1 first number and second whole negative number
        test("1","-1","-1");
        test("1","-2","-2");
        test("1","-99","-99");
        test("1","-100","-100");
        test("1",Integer.MIN_VALUE+"","-2147483648");
        test("1",Long.MIN_VALUE+"","-9223372036854775808");
        test("1","-"+FIFTY_UNITS,"-11111111111111111111111111111111111111111111111111");
        test("1","-"+FIFTY_NINES,"-99999999999999999999999999999999999999999999999999");

        //test 1 first number and second fractional positive number
        test("1","0.1","0.1");
        test("1","0.2","0.2");
        test("1","0.01","0.01");
        test("1","0.02","0.02");
        test("1","1.1","1.1");
        test("1","2.2","2.2");
        test("1","99.99","99.99");
        test("1","100.01","100.01");
        test("1",SIXTEEN_UNITS_AFTER_DOT,"0.1111111111111111");
        test("1",SIXTEEN_NINES_AFTER_DOT,"0.9999999999999999");
        test("1",Double.MAX_VALUE+"","1.7976931348623157E+308");
        test("1",FIFTY_UNITS_THROUGH_DOT,"1111111111111111111111111.1111111111111111111111111");
        test("1",FIFTY_NINES_THROUGH_DOT,"9999999999999999999999999.9999999999999999999999999");

        //test 1 first number and second fractional negative number
        test("1","-0.1","-0.1");
        test("1","-0.2","-0.2");
        test("1","-0.01","-0.01");
        test("1","-0.02","-0.02");
        test("1","-1.1","-1.1");
        test("1","-2.2","-2.2");
        test("1","-99.99","-99.99");
        test("1","-100.01","-100.01");
        test("1","-"+SIXTEEN_UNITS_AFTER_DOT,"-0.1111111111111111");
        test("1","-"+SIXTEEN_NINES_AFTER_DOT,"-0.9999999999999999");
        test("1",Double.MIN_VALUE+"","4.9E-324");
        test("1","-"+FIFTY_UNITS_THROUGH_DOT,"-1111111111111111111111111.1111111111111111111111111");
        test("1","-"+FIFTY_NINES_THROUGH_DOT,"-9999999999999999999999999.9999999999999999999999999");

    }


    @Test
    void calculatePlusWithTwoParameters() throws MyException {

        currentOperation = plus;

        //test 0 first number and second whole positive number
        test("0","0","0");
        test("0","1","1");
        test("0","2","2");
        test("0","99","99");
        test("0","100","100");
        test("0",Long.MAX_VALUE+"","9223372036854775807");
        test("0",FIFTY_UNITS,"11111111111111111111111111111111111111111111111111");
        test("0",FIFTY_NINES,"99999999999999999999999999999999999999999999999999");

        //test 0 first number and second whole negative number
        test("0","-1","-1");
        test("0","-2","-2");
        test("0","-99","-99");
        test("0","-100","-100");
        test("0",Integer.MIN_VALUE+"","-2147483648");
        test("0",Long.MIN_VALUE+"","-9223372036854775808");
        test("0","-"+FIFTY_UNITS,"-11111111111111111111111111111111111111111111111111");
        test("0","-"+FIFTY_NINES,"-99999999999999999999999999999999999999999999999999");

        //test 0 first number and second fractional positive number
        test("0","0.1","0.1");
        test("0","0.2","0.2");
        test("0","0.01","0.01");
        test("0","0.02","0.02");
        test("0","1.1","1.1");
        test("0","2.2","2.2");
        test("0","99.99","99.99");
        test("0","100.01","100.01");
        test("0",SIXTEEN_UNITS_AFTER_DOT,"0.1111111111111111");
        test("0",SIXTEEN_NINES_AFTER_DOT,"0.9999999999999999");
        test("0",Double.MAX_VALUE+"","179769313486231570000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
        test("0",FIFTY_UNITS_THROUGH_DOT,"1111111111111111111111111.1111111111111111111111111");
        test("0",FIFTY_NINES_THROUGH_DOT,"9999999999999999999999999.9999999999999999999999999");

        //test 0 first number and second fractional negative number
        test("0","-0.1","-0.1");
        test("0","-0.2","-0.2");
        test("0","-0.01","-0.01");
        test("0","-0.02","-0.02");
        test("0","-1.1","-1.1");
        test("0","-2.2","-2.2");
        test("0","-99.99","-99.99");
        test("0","-100.01","-100.01");
        test("0","-"+SIXTEEN_UNITS_AFTER_DOT,"-0.1111111111111111");
        test("0","-"+SIXTEEN_NINES_AFTER_DOT,"-0.9999999999999999");
        test("0",Double.MIN_VALUE+"","4.9E-324");
        test("0","-"+FIFTY_UNITS_THROUGH_DOT,"-1111111111111111111111111.1111111111111111111111111");
        test("0","-"+FIFTY_NINES_THROUGH_DOT,"-9999999999999999999999999.9999999999999999999999999");

    }

    @Test
    void calculateMinusWithTwoParameters() throws MyException {

        currentOperation = minus;

        //test 0 first number and second whole positive number
        test("0","0","0");
        test("0","1","-1");
        test("0","2","-2");
        test("0","99","-99");
        test("0","100","-100");
        test("0",Long.MAX_VALUE+"","-9223372036854775807");
        test("0",FIFTY_UNITS,"-11111111111111111111111111111111111111111111111111");
        test("0",FIFTY_NINES,"-99999999999999999999999999999999999999999999999999");

        //test 0 first number and second whole negative number
        test("0","-1","1");
        test("0","-2","2");
        test("0","-99","99");
        test("0","-100","100");
        test("0",Integer.MIN_VALUE+"","2147483648");
        test("0",Long.MIN_VALUE+"","9223372036854775808");
        test("0","-"+FIFTY_UNITS,"11111111111111111111111111111111111111111111111111");
        test("0","-"+FIFTY_NINES,"99999999999999999999999999999999999999999999999999");

        //test 0 first number and second fractional positive number
        test("0","0.1","-0.1");
        test("0","0.2","-0.2");
        test("0","0.01","-0.01");
        test("0","0.02","-0.02");
        test("0",SIXTEEN_UNITS_AFTER_DOT,"-0.1111111111111111");
        test("0",SIXTEEN_NINES_AFTER_DOT,"-0.9999999999999999");
        test("0",Double.MAX_VALUE+"","-179769313486231570000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
        test("0",FIFTY_UNITS_THROUGH_DOT,"-1111111111111111111111111.1111111111111111111111111");
        test("0",FIFTY_NINES_THROUGH_DOT,"-9999999999999999999999999.9999999999999999999999999");

        //test 0 first number and second fractional negative number
        test("0","-0.1","0.1");
        test("0","-0.2","0.2");
        test("0","-0.01","0.01");
        test("0","-0.02","0.02");
        test("0","-"+SIXTEEN_UNITS_AFTER_DOT,"0.1111111111111111");
        test("0","-"+SIXTEEN_NINES_AFTER_DOT,"0.9999999999999999");
        test("0",Double.MIN_VALUE+"","-4.9E-324");
        test("0","-"+FIFTY_UNITS_THROUGH_DOT,"1111111111111111111111111.1111111111111111111111111");
        test("0","-"+FIFTY_NINES_THROUGH_DOT,"9999999999999999999999999.9999999999999999999999999");
    }

    @Test
    void calculateDivideWithTwoParameters() throws MyException {

        currentOperation = divide;

        //test 0 first number and second whole positive number
        test("0","1","0E-16");
        test("0","2","0E-16");
        test("0","99","0E-16");
        test("0","100","0E-16");
        test("0",Long.MAX_VALUE+"","0E-16");
        test("0",FIFTY_UNITS,"0E-16");
        test("0",FIFTY_NINES,"0E-16");

        //test 0 first number and second whole negative number
        test("0","-1","0E-16");
        test("0","-2","0E-16");
        test("0","-99","0E-16");
        test("0","-100","0E-16");
        test("0",Integer.MIN_VALUE+"","0E-16");
        test("0",Long.MIN_VALUE+"","0E-16");
        test("0","-"+FIFTY_UNITS,"0E-16");
        test("0","-"+FIFTY_NINES,"0E-16");

        //test 0 first number and second fractional positive number
        test("0","0.1","0E-16");
        test("0","0.2","0E-16");
        test("0","0.01","0E-16");
        test("0","0.02","0E-16");
        test("0",SIXTEEN_UNITS_AFTER_DOT,"0E-16");
        test("0",SIXTEEN_NINES_AFTER_DOT,"0E-16");
        test("0",Double.MAX_VALUE+"","0E-16");
        test("0",FIFTY_UNITS_THROUGH_DOT,"0E-16");
        test("0",FIFTY_NINES_THROUGH_DOT,"0E-16");

        //test 0 first number and second fractional negative number
        test("0","-0.1","0E-16");
        test("0","-0.2","0E-16");
        test("0","-0.01","0E-16");
        test("0","-0.02","0E-16");
        test("0","-"+SIXTEEN_UNITS_AFTER_DOT,"0E-16");
        test("0","-"+SIXTEEN_NINES_AFTER_DOT,"0E-16");
        test("0",Double.MIN_VALUE+"","0E-16");
        test("0","-"+FIFTY_UNITS_THROUGH_DOT,"0E-16");
        test("0","-"+FIFTY_NINES_THROUGH_DOT,"0E-16");
    }

    private void test(String number, String result) throws MyException {

        assertEquals(new BigDecimal(result), currentOperation.calculate(new BigDecimal(number)));
    }

    private void test(String first, String second, String result) throws MyException {

        assertEquals(new BigDecimal(result), currentOperation.calculate(new BigDecimal(first),new BigDecimal(second)));
    }
}