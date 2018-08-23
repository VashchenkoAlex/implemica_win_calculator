package win_calculator;

import org.junit.jupiter.api.Test;
import win_calculator.model.nodes.actions.extra_operations.ExtraOperation;
import win_calculator.model.nodes.actions.extra_operations.Fraction;
import win_calculator.model.nodes.actions.extra_operations.Sqr;
import win_calculator.model.nodes.actions.extra_operations.Sqrt;

import static org.junit.jupiter.api.Assertions.*;
import static win_calculator.utils.StringUtils.addCapacity;
import static win_calculator.utils.StringUtils.addExtraOperationToString;

class StringUtilsTest {

    private static final int ENTERED_NUMBER = 0;
    private static final int RESULT_NUMBER = 1;
    private static final String SQRT = "√";
    private final Sqr sqr = new Sqr();
    private final Sqrt sqrt = new Sqrt();
    private final Fraction fraction = new Fraction();

    @Test
    void replaceComaToDot() {

    }

    @Test
    void replaceDotToComa() {
    }

    @Test
    void addSpacesTest() {

        //Positive value without coma
        testAddSpaces("1","1");
        testAddSpaces("11","11");
        testAddSpaces("111","111");
        testAddSpaces("1111","1 111");
        testAddSpaces("11111","11 111");
        testAddSpaces("111111","111 111");
        testAddSpaces("1111111","1 111 111");
        testAddSpaces("11111111","11 111 111");
        testAddSpaces("111111111","111 111 111");
        testAddSpaces("1111111111","1 111 111 111");
        testAddSpaces("11111111111111","11 111 111 111 111");
        testAddSpaces("111111111111111","111 111 111 111 111");
        testAddSpaces("1111111111111111","1 111 111 111 111 111");

        //Negative value without coma
        testAddSpaces("-1","-1");
        testAddSpaces("-11","-11");
        testAddSpaces("-111","-111");
        testAddSpaces("-1111","-1 111");
        testAddSpaces("-11111","-11 111");
        testAddSpaces("-111111","-111 111");
        testAddSpaces("-1111111","-1 111 111");
        testAddSpaces("-11111111","-11 111 111");
        testAddSpaces("-111111111","-111 111 111");
        testAddSpaces("-1111111111","-1 111 111 111");
        testAddSpaces("-11111111111111","-11 111 111 111 111");
        testAddSpaces("-111111111111111","-111 111 111 111 111");
        testAddSpaces("-1111111111111111","-1 111 111 111 111 111");

        //Positive value with coma
        testAddSpaces("1,1","1,1");
        testAddSpaces("1,11111111111111","1,11111111111111");
        testAddSpaces("1,111111111111111","1,111111111111111");
        testAddSpaces("11,11","11,11");
        testAddSpaces("111,111","111,111");
        testAddSpaces("1111,1111","1 111,1111");
        testAddSpaces("11111,11111","11 111,11111");
        testAddSpaces("111111,111111","111 111,111111");
        testAddSpaces("1111111,111111","1 111 111,111111");
        testAddSpaces("11111111,1111111","11 111 111,1111111");
        testAddSpaces("111111111,1111111","111 111 111,1111111");

        //Negative value with coma
        testAddSpaces("-1,1","-1,1");
        testAddSpaces("-1,11111111111111","-1,11111111111111");
        testAddSpaces("-1,111111111111111","-1,111111111111111");
        testAddSpaces("-11,11","-11,11");
        testAddSpaces("-111,111","-111,111");
        testAddSpaces("-1111,1111","-1 111,1111");
        testAddSpaces("-11111,11111","-11 111,11111");
        testAddSpaces("-111111,111111","-111 111,111111");
        testAddSpaces("-1111111,111111","-1 111 111,111111");
        testAddSpaces("-11111111,1111111","-11 111 111,1111111");
        testAddSpaces("-111111111,1111111","-111 111 111,1111111");
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

    @Test
    void addExtraOperationToStringTest(){

        testAddExtra("","10",sqr,"sqr( 10 )");
        testAddExtra("","10",sqrt,SQRT+"( 10 )");
        testAddExtra("","10",fraction,"1/( 10 )");

        testAddExtra("0  -  ","1",sqr,"0  -  sqr( 1 )");
        testAddExtra("0  -  ","1",sqrt,"0  -  "+SQRT+"( 1 )");
        testAddExtra("0  -  ","1",fraction,"0  -  1/( 1 )");

        testAddExtra(SQRT+"( 25 )  +  ","25",sqr,SQRT+"( 25 )  +  sqr( 25 )");
        testAddExtra(SQRT+"( 25 )  +  ","25",sqrt,SQRT+"( 25 )  +  "+SQRT+"( 25 )");
        testAddExtra(SQRT+"( 25 )  +  ","25",fraction,SQRT+"( 25 )  +  1/( 25 )");

        testAddExtra(SQRT+"( 625 )","25",sqr,"sqr( "+SQRT+"( 625 ) )");
        testAddExtra(SQRT+"( 625 )","25",sqrt,SQRT+"( "+SQRT+"( 625 ) )");
        testAddExtra(SQRT+"( 625 )","25",fraction,"1/( "+SQRT+"( 625 ) )");

        testAddExtra(SQRT+"( "+SQRT+"( 625 ) )","5",sqr,"sqr( "+SQRT+"( "+SQRT+"( 625 ) ) )");
        testAddExtra(SQRT+"( "+SQRT+"( 625 ) )","5",sqrt,SQRT+"( "+SQRT+"( "+SQRT+"( 625 ) ) )");
        testAddExtra(SQRT+"( "+SQRT+"( 625 ) )","5",fraction,"1/( "+SQRT+"( "+SQRT+"( 625 ) ) )");

        testAddExtra(SQRT+"( "+SQRT+"( "+SQRT+"( 625 ) ) )  -  "+SQRT+"( "+SQRT+"( 625 ) )","5",sqr,SQRT+"( "+SQRT+"( "+SQRT+"( 625 ) ) )  -  sqr( "+SQRT+"( "+SQRT+"( 625 ) ) )");
        testAddExtra(SQRT+"( "+SQRT+"( "+SQRT+"( 625 ) ) )  -  "+SQRT+"( "+SQRT+"( 625 ) )","5",sqrt,SQRT+"( "+SQRT+"( "+SQRT+"( 625 ) ) )  -  "+SQRT+"( "+SQRT+"( "+SQRT+"( 625 ) ) )");
        testAddExtra(SQRT+"( "+SQRT+"( "+SQRT+"( 625 ) ) )  -  "+SQRT+"( "+SQRT+"( 625 ) )","5",fraction,SQRT+"( "+SQRT+"( "+SQRT+"( 625 ) ) )  -  1/( "+SQRT+"( "+SQRT+"( 625 ) ) )");

    }

    private void testAddSpaces(String inserted, String expected){

        assertEquals(expected, addCapacity(inserted));
    }

    private void testAddExtra(String historyStr, String displayStr, ExtraOperation eOperation,String expectedStr){

        //assertEquals(expectedStr,addExtraOperationToString(historyStr,displayStr,eOperation.getMemoryType()));
    }
}