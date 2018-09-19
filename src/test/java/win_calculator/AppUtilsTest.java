package win_calculator;

import org.junit.jupiter.api.Test;
import win_calculator.exceptions.MyException;

import java.math.BigDecimal;

import static org.junit.gen5.api.Assertions.assertEquals;
import static win_calculator.utils.AppUtils.addCapacity;
import static win_calculator.utils.AppUtils.checkOnOverflow;


class AppUtilsTest {

    @Test
    void addCapacityTest() {

        //Positive value without coma
        testCapacity("1","1");
        testCapacity("11","11");
        testCapacity("111","111");
        testCapacity("1111","1 111");
        testCapacity("11111","11 111");
        testCapacity("111111","111 111");
        testCapacity("1111111","1 111 111");
        testCapacity("11111111","11 111 111");
        testCapacity("111111111","111 111 111");
        testCapacity("1111111111","1 111 111 111");
        testCapacity("11111111111111","11 111 111 111 111");
        testCapacity("111111111111111","111 111 111 111 111");
        testCapacity("1111111111111111","1 111 111 111 111 111");

        //Negative value without coma
        testCapacity("-1","-1");
        testCapacity("-11","-11");
        testCapacity("-111","-111");
        testCapacity("-1111","-1 111");
        testCapacity("-11111","-11 111");
        testCapacity("-111111","-111 111");
        testCapacity("-1111111","-1 111 111");
        testCapacity("-11111111","-11 111 111");
        testCapacity("-111111111","-111 111 111");
        testCapacity("-1111111111","-1 111 111 111");
        testCapacity("-11111111111111","-11 111 111 111 111");
        testCapacity("-111111111111111","-111 111 111 111 111");
        testCapacity("-1111111111111111","-1 111 111 111 111 111");

        //Positive value with coma
        testCapacity("1,1","1,1");
        testCapacity("1,11111111111111","1,11111111111111");
        testCapacity("1,111111111111111","1,111111111111111");
        testCapacity("11,11","11,11");
        testCapacity("111,111","111,111");
        testCapacity("1111,1111","1 111,1111");
        testCapacity("11111,11111","11 111,11111");
        testCapacity("111111,111111","111 111,111111");
        testCapacity("1111111,111111","1 111 111,111111");
        testCapacity("11111111,1111111","11 111 111,1111111");
        testCapacity("111111111,1111111","111 111 111,1111111");

        //Negative value with coma
        testCapacity("-1,1","-1,1");
        testCapacity("-1,11111111111111","-1,11111111111111");
        testCapacity("-1,111111111111111","-1,111111111111111");
        testCapacity("-11,11","-11,11");
        testCapacity("-111,111","-111,111");
        testCapacity("-1111,1111","-1 111,1111");
        testCapacity("-11111,11111","-11 111,11111");
        testCapacity("-111111,111111","-111 111,111111");
        testCapacity("-1111111,111111","-1 111 111,111111");
        testCapacity("-11111111,1111111","-11 111 111,1111111");
        testCapacity("-111111111,1111111","-111 111 111,1111111");

        testCapacity("111 111 111,1111111","111 111 111,1111111");
    }

    @Test
    void testOnOverflow(){

        testOverflow("1e9998","1E+9998");
        testOverflow("1e9999","1E+9999");
        testOverflow("1e10000","Overflow");
        testOverflow("1e10001","Overflow");
        testOverflow("1e-9998","1E-9998");
        testOverflow("1e-9999","1E-9999");
        testOverflow("1e-10000","Overflow");
        testOverflow("1e-10001","Overflow");
    }

    private void testCapacity(String inserted, String expected){

        assertEquals(expected,addCapacity(inserted));
    }

    private void testOverflow(String inserted, String expected){

        String result;
        try {
            result = checkOnOverflow(new BigDecimal(inserted)).toString();
        } catch (MyException e) {
            result = e.getMessage();
        }
        assertEquals(expected,result);
    }

}