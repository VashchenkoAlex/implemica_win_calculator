package win_calculator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static win_calculator.StringUtils.addCapacity;

class StringUtilsTest {

    private static final int ENTERED_NUMBER = 0;
    private static final int RESULT_NUMBER = 1;

    @Test
    void replaceComaToDot() {

    }

    @Test
    void replaceDotToComa() {
    }

    @Test
    void addSpacesTest() {

        //Positive value for RESULT_NUMBER without coma
        testAddSpaces("1",RESULT_NUMBER,"1");
        testAddSpaces("11",RESULT_NUMBER,"11");
        testAddSpaces("111",RESULT_NUMBER,"111");
        testAddSpaces("1111",RESULT_NUMBER,"1 111");
        testAddSpaces("11111",RESULT_NUMBER,"11 111");
        testAddSpaces("111111",RESULT_NUMBER,"111 111");
        testAddSpaces("1111111",RESULT_NUMBER,"1 111 111");
        testAddSpaces("11111111",RESULT_NUMBER,"11 111 111");
        testAddSpaces("111111111",RESULT_NUMBER,"111 111 111");
        testAddSpaces("1111111111",RESULT_NUMBER,"1 111 111 111");
        testAddSpaces("11111111111111",RESULT_NUMBER,"11 111 111 111 111");
        testAddSpaces("111111111111111",RESULT_NUMBER,"111 111 111 111 111");
        testAddSpaces("1111111111111111",RESULT_NUMBER,"1 111 111 111 111 111");

        //Negative value for RESULT_NUMBER without coma
        testAddSpaces("-1",RESULT_NUMBER,"-1");
        testAddSpaces("-11",RESULT_NUMBER,"-11");
        testAddSpaces("-111",RESULT_NUMBER,"-111");
        testAddSpaces("-1111",RESULT_NUMBER,"-1 111");
        testAddSpaces("-11111",RESULT_NUMBER,"-11 111");
        testAddSpaces("-111111",RESULT_NUMBER,"-111 111");
        testAddSpaces("-1111111",RESULT_NUMBER,"-1 111 111");
        testAddSpaces("-11111111",RESULT_NUMBER,"-11 111 111");
        testAddSpaces("-111111111",RESULT_NUMBER,"-111 111 111");
        testAddSpaces("-1111111111",RESULT_NUMBER,"-1 111 111 111");
        testAddSpaces("-11111111111111",RESULT_NUMBER,"-11 111 111 111 111");
        testAddSpaces("-111111111111111",RESULT_NUMBER,"-111 111 111 111 111");
        testAddSpaces("-1111111111111111",RESULT_NUMBER,"-1 111 111 111 111 111");

        //Positive value for RESULT_NUMBER with coma
        testAddSpaces("1,1",RESULT_NUMBER,"1,1");
        testAddSpaces("1,11111111111111",RESULT_NUMBER,"1,11111111111111");
        testAddSpaces("1,111111111111111",RESULT_NUMBER,"1,111111111111111");
        testAddSpaces("11,11",RESULT_NUMBER,"11,11");
        testAddSpaces("111,111",RESULT_NUMBER,"111,111");
        testAddSpaces("1111,1111",RESULT_NUMBER,"1 111,1111");
        testAddSpaces("11111,11111",RESULT_NUMBER,"11 111,11111");
        testAddSpaces("111111,111111",RESULT_NUMBER,"111 111,111111");
        testAddSpaces("1111111,111111",RESULT_NUMBER,"1 111 111,111111");
        testAddSpaces("11111111,1111111",RESULT_NUMBER,"11 111 111,1111111");
        testAddSpaces("111111111,1111111",RESULT_NUMBER,"111 111 111,1111111");

        //Negative value for RESULT_NUMBER with coma
        testAddSpaces("-1,1",RESULT_NUMBER,"-1,1");
        testAddSpaces("-1,11111111111111",RESULT_NUMBER,"-1,11111111111111");
        testAddSpaces("-1,111111111111111",RESULT_NUMBER,"-1,111111111111111");
        testAddSpaces("-11,11",RESULT_NUMBER,"-11,11");
        testAddSpaces("-111,111",RESULT_NUMBER,"-111,111");
        testAddSpaces("-1111,1111",RESULT_NUMBER,"-1 111,1111");
        testAddSpaces("-11111,11111",RESULT_NUMBER,"-11 111,11111");
        testAddSpaces("-111111,111111",RESULT_NUMBER,"-111 111,111111");
        testAddSpaces("-1111111,111111",RESULT_NUMBER,"-1 111 111,111111");
        testAddSpaces("-11111111,1111111",RESULT_NUMBER,"-11 111 111,1111111");
        testAddSpaces("-111111111,1111111",RESULT_NUMBER,"-111 111 111,1111111");

        //Positive value for ENTERED_NUMBER without coma
        testAddSpaces("1",ENTERED_NUMBER,"1");
        testAddSpaces("11",ENTERED_NUMBER,"11");
        testAddSpaces("111",ENTERED_NUMBER,"1 11");
        testAddSpaces("1111",ENTERED_NUMBER,"11 11");
        testAddSpaces("11111",ENTERED_NUMBER,"111 11");
        testAddSpaces("111111",ENTERED_NUMBER,"1 111 11");
        testAddSpaces("1111111",ENTERED_NUMBER,"11 111 11");
        testAddSpaces("11111111",ENTERED_NUMBER,"111 111 11");
        testAddSpaces("111111111",ENTERED_NUMBER,"1 111 111 11");
        testAddSpaces("1111111111",ENTERED_NUMBER,"11 111 111 11");
        testAddSpaces("11111111111111",ENTERED_NUMBER,"111 111 111 111 11");
        testAddSpaces("111111111111111",ENTERED_NUMBER,"1 111 111 111 111 11");
        testAddSpaces("1111111111111111",ENTERED_NUMBER,"11 111 111 111 111 11");

        //Negative value for ENTERED_NUMBER without coma
        testAddSpaces("-1",ENTERED_NUMBER,"-1");
        testAddSpaces("-11",ENTERED_NUMBER,"-11");
        testAddSpaces("-111",ENTERED_NUMBER,"-1 11");
        testAddSpaces("-1111",ENTERED_NUMBER,"-11 11");
        testAddSpaces("-11111",ENTERED_NUMBER,"-111 11");
        testAddSpaces("-111111",ENTERED_NUMBER,"-1 111 11");
        testAddSpaces("-1111111",ENTERED_NUMBER,"-11 111 11");
        testAddSpaces("-11111111",ENTERED_NUMBER,"-111 111 11");
        testAddSpaces("-111111111",ENTERED_NUMBER,"-1 111 111 11");
        testAddSpaces("-1111111111",ENTERED_NUMBER,"-11 111 111 11");
        testAddSpaces("-11111111111111",ENTERED_NUMBER,"-111 111 111 111 11");
        testAddSpaces("-111111111111111",ENTERED_NUMBER,"-1 111 111 111 111 11");
        testAddSpaces("-1111111111111111",ENTERED_NUMBER,"-11 111 111 111 111 11");

        //Positive value for ENTERED_NUMBER with coma
        testAddSpaces("1,1",ENTERED_NUMBER,"1,1");
        testAddSpaces("1,11111111111111",ENTERED_NUMBER,"1,11111111111111");
        testAddSpaces("1,111111111111111",ENTERED_NUMBER,"1,111111111111111");
        testAddSpaces("11,11",ENTERED_NUMBER,"11,11");
        testAddSpaces("111,111",ENTERED_NUMBER,"111,111");
        testAddSpaces("1111,1111",ENTERED_NUMBER,"1 111,1111");
        testAddSpaces("11111,11111",ENTERED_NUMBER,"11 111,11111");
        testAddSpaces("111111,111111",ENTERED_NUMBER,"111 111,111111");
        testAddSpaces("1111111,111111",ENTERED_NUMBER,"1 111 111,111111");
        testAddSpaces("11111111,1111111",ENTERED_NUMBER,"11 111 111,1111111");
        testAddSpaces("111111111,1111111",ENTERED_NUMBER,"111 111 111,1111111");

        //Negative value for ENTERED_NUMBERwith coma
        testAddSpaces("-1,1",ENTERED_NUMBER,"-1,1");
        testAddSpaces("-1,11111111111111",ENTERED_NUMBER,"-1,11111111111111");
        testAddSpaces("-1,111111111111111",ENTERED_NUMBER,"-1,111111111111111");
        testAddSpaces("-11,11",ENTERED_NUMBER,"-11,11");
        testAddSpaces("-111,111",ENTERED_NUMBER,"-111,111");
        testAddSpaces("-1111,1111",ENTERED_NUMBER,"-1 111,1111");
        testAddSpaces("-11111,11111",ENTERED_NUMBER,"-11 111,11111");
        testAddSpaces("-111111,111111",ENTERED_NUMBER,"-111 111,111111");
        testAddSpaces("-1111111,111111",ENTERED_NUMBER,"-1 111 111,111111");
        testAddSpaces("-11111111,1111111",ENTERED_NUMBER,"-11 111 111,1111111");
        testAddSpaces("-111111111,1111111",ENTERED_NUMBER,"-111 111 111,1111111");

    }

    @Test
    void removeSpaces() {
    }

    @Test
    void isComaAbsent() {
    }

    @Test
    void deleteOneSymbolFromTheEnd() {
    }

    @Test
    void cutLastZeros() {
    }

    @Test
    void cutLastComa() {
    }

    @Test
    void optimizeString() {
    }

    @Test
    void cutMinus() {
    }

    private void testAddSpaces(String inserted, int count, String expected){

        assertEquals(expected, addCapacity(inserted,count));
    }
}