package win_calculator;

import org.junit.jupiter.api.Test;
import static org.junit.gen5.api.Assertions.assertEquals;
import static win_calculator.utils.AppUtils.addCapacity;


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

        //
        testCapacity("111 111 111,1111111","111 111 111,1111111");
    }

    private void testCapacity(String inserted, String expected){

        assertEquals(expected,addCapacity(inserted));
    }

}