package win_calculator;

import org.junit.jupiter.api.Test;
import win_calculator.buttons_handlers.ExtraActionButtonsHandler;
import win_calculator.buttons_handlers.MainActionButtonsHandler;
import win_calculator.buttons_handlers.PercentBtnHandler;
import win_calculator.extra_operations.ExtraOperation;
import win_calculator.extra_operations.Percent;
import win_calculator.extra_operations.Sqrt;
import win_calculator.main_operations.*;

import java.math.BigDecimal;

import static org.junit.gen5.api.Assertions.assertEquals;
import static win_calculator.Controller.setInputtedNumber;
import static win_calculator.Controller.setOperationBefore;
import static win_calculator.StringUtils.addCapacity;
import static win_calculator.StringUtils.removeCapacity;
import static win_calculator.StringUtils.replaceComaToDot;

class ControllerTest {

    private final MainActionButtonsHandler mainActBtnsHandler = new MainActionButtonsHandler();
    private final ExtraActionButtonsHandler extraActBtnHandler = new ExtraActionButtonsHandler();
    private final PercentBtnHandler percentBtnHandler = new PercentBtnHandler();
    private final Plus plus = new Plus();
    private final Minus minus = new Minus();
    private final Multiply multiply = new Multiply();
    private final Divide divide = new Divide();
    private final Percent percent = new Percent();
    private final Sqrt sqrt = new Sqrt();

    @Test
    void testChainNumActEnterEnterEnter(){

        //Test Plus
        testFirstChain(0,plus,0);
        testFirstChain(1,plus,4);
        testFirstChain(2,plus,8);
        testFirstChain(99,plus,396);

        //Test Minus
        testFirstChain(1,minus,-2);
        testFirstChain(2,minus,-4);
        testFirstChain(99,minus,-198);
    }

    @Test
    void testChainNumActNumActNumActNumEnterEnter(){

        //test positive values for Plus
        testSecondChain(1,2,3,4,plus,"14");
        testSecondChain(2,3,4,5,plus,"19");
        testSecondChain(101,102,103,104,plus,"514");
        testSecondChain(102,103,104,105,plus,"519");

        //test negative values for Plus
        testSecondChain(-1,-2,-3,-4,plus,"-14");
        testSecondChain(-2,-3,-4,-5,plus,"-19");
        testSecondChain(-101,-102,-103,-104,plus,"-514");
        testSecondChain(-102,-103,-104,-105,plus,"-519");

        //test different sign for Plus
        testSecondChain(-1,2,-3,4,plus,"6");
        testSecondChain(2,-3,4,-5,plus,"-7");
        testSecondChain(-101,102,-103,104,plus,"106");
        testSecondChain(102,-103,104,-105,plus,"-107");

        //test positive values for Minus
        testSecondChain(1,2,3,4,minus,"-12");
        testSecondChain(2,3,4,5,minus,"-15");
        testSecondChain(101,102,103,104,minus,"-312");
        testSecondChain(102,103,104,105,minus,"-315");

        //test negative values for Minus
        testSecondChain(-1,-2,-3,-4,minus,"12");
        testSecondChain(-2,-3,-4,-5,minus,"15");
        testSecondChain(-101,-102,-103,-104,minus,"312");
        testSecondChain(-102,-103,-104,-105,minus,"315");

        //test different sign for Minus
        testSecondChain(-1,2,-3,4,minus,"-8");
        testSecondChain(2,-3,4,-5,minus,"11");
        testSecondChain(-101,102,-103,104,minus,"-308");
        testSecondChain(102,-103,104,-105,minus,"311");

        //test positive values for Multiply
        testSecondChain(1,2,3,4,multiply,"96");
        testSecondChain(2,3,4,5,multiply,"600");
        testSecondChain(101,102,103,104,multiply,"11476922496");
        testSecondChain(102,103,104,105,multiply,"12046179600");

        //test negative values for Multiply
        testSecondChain(-1,-2,-3,-4,multiply,"-96");
        testSecondChain(-2,-3,-4,-5,multiply,"-600");
        testSecondChain(-101,-102,-103,-104,multiply,"-11476922496");
        testSecondChain(-102,-103,-104,-105,multiply,"-12046179600");

        //test different sign for Multiply
        testSecondChain(-1,2,-3,4,multiply,"96");
        testSecondChain(2,-3,4,-5,multiply,"-600");
        testSecondChain(-101,102,-103,104,multiply,"11476922496");
        testSecondChain(102,-103,104,-105,multiply,"-12046179600");

        //test positive values for Divide
        testSecondChain(1,2,3,4,divide,"0,0104166666666667");
        testSecondChain(2,3,4,5,divide,"0,0066666666666667");
        //testSecondChain(101,102,103,104,divide,"11476922496");
        //testSecondChain(102,103,104,105,divide,"12046179600");

        //test negative values for Divide
        testSecondChain(-1,-2,-3,-4,divide,"-0,0104166666666667");
        testSecondChain(-2,-3,-4,-5,divide,"-0,0066666666666667");
        //testSecondChain(-101,-102,-103,-104,divide,"-11476922496");
        //testSecondChain(-102,-103,-104,-105,divide,"-12046179600");

        //test different sign for Divide
        testSecondChain(-1,2,-3,4,divide,"0,0104166666666667");
        testSecondChain(2,-3,4,-5,divide,"-0,0066666666666667");
        //testSecondChain(-101,102,-103,104,divide,"11476922496");
        //testSecondChain(102,-103,104,-105,divide,"-12046179600");
    }

    @Test
    void testChainNumActNumActEnterEnter(){

        testThirdChain(2,3,plus,"15");
        testThirdChain(2,3,minus,"1");
        testThirdChain(2,3,multiply,"216");
        testThirdChain(2,3,divide,"1,4999999999999999");
    }

    @Test
    void testChainNumActNumActNumActEnterEnter(){

        testFourthChain(1,2,3,plus,"18");
        testFourthChain(1,2,3,minus,"4");
        testFourthChain(1,2,3,multiply,"216");
        testFourthChain(1,2,3,divide,"5,9999999999999988");
    }

    @Test
    void testChainNumActNumPercentEnterEnterEnter(){

        testFifthChain(100,10,plus,percent,"130");
        testFifthChain(100,10,minus,percent,"70");
        testFifthChain(100,10,multiply,percent,"100 000");
        testFifthChain(100,10,divide,percent,"0,1");
    }

    @Test
    void testChainNumActNumPercentActEnterEnter(){

        testSixthChain(100,20,plus,percent,"360");
        testSixthChain(100,20,minus,percent,"-80");
        testSixthChain(100,20,multiply,percent,"8 000 000 000");
        testSixthChain(100,20,divide,percent,"0,2");
    }

    @Test
    void testChainNumActNumPercentActNumPercentEnterEnter(){

        testSeventhChain(100,20,20,plus,percent,"168");
        testSeventhChain(100,20,20,minus,percent,"48");
        testSeventhChain(100,20,20,multiply,percent,"320 000 000");
        testSeventhChain(100,20,20,divide,percent,"5");
    }

    @Test
    void testChainNumEActEActEActEnterEnter(){

        testEighthChain(0,sqrt,"0");
        testEighthChain(1,sqrt,"1");
        testEighthChain(16,sqrt,"1,414213562373095");
        testEighthChain(256,sqrt,"2");

    }

    @Test
    void testChainNumEActActEnterEnterWithSameAct(){

        testNinthChain(4,plus,sqrt,"6");
        testNinthChain(25,plus,sqrt,"15");
    }

    @Test
    void testChainNumFirstActNumFirstActNumSecondActNumSecondActNumEnterEnter(){

        testTenthChain(25,plus,multiply,"1 171 875");
    }

    @Test
    void testInvalidValues(){

        testFirstInvalidChain(-1,sqrt,"Invalid input");
    }

    private void testFirstChain(long displayedNumber, MainOperation operation, long expResult){

        String display = displayedNumber+"";
        display = setDisplay(mainActBtnsHandler.doOperation(operation,new BigDecimal(display)),display);
        display = setDisplay(mainActBtnsHandler.doEnter(new BigDecimal(display)),display);
        display = setDisplay(mainActBtnsHandler.doEnter(new BigDecimal(display)),display);
        display = mainActBtnsHandler.doEnter(new BigDecimal(display));
        assertEquals(expResult+"",display);
        mainActBtnsHandler.resetValues();
    }

    private void testSecondChain(long firstNum, long secondNum, long thirdNum, long fourthNum,
                                 MainOperation operation, String expResult){

        mainActBtnsHandler.doOperation(operation,BigDecimal.valueOf(firstNum));
        setInputtedNumber(true);
        mainActBtnsHandler.doOperation(operation,BigDecimal.valueOf(secondNum));
        setInputtedNumber(true);
        mainActBtnsHandler.doOperation(operation,BigDecimal.valueOf(thirdNum));
        setInputtedNumber(true);
        String display = replaceComaToDot(removeCapacity(mainActBtnsHandler.doEnter(BigDecimal.valueOf(fourthNum))));
        assertEquals(addCapacity(expResult+"",1), mainActBtnsHandler.doEnter(new BigDecimal(display)));
        mainActBtnsHandler.resetValues();
    }

    private void testThirdChain(long firstNum, long secondNum, MainOperation operation, String expResult){

        mainActBtnsHandler.doOperation(operation, BigDecimal.valueOf(firstNum));
        setInputtedNumber(true);
        String display = mainActBtnsHandler.doOperation(operation,BigDecimal.valueOf(secondNum));
        display = mainActBtnsHandler.doEnter(new BigDecimal(replaceComaToDot(removeCapacity(display))));
        assertEquals(expResult, mainActBtnsHandler.doEnter(new BigDecimal(replaceComaToDot(removeCapacity(display)))));
        mainActBtnsHandler.resetValues();
    }

    private void testFourthChain(long firstNum,long secondNum,long thirdNum, MainOperation operation,String result){

        mainActBtnsHandler.doOperation(operation,BigDecimal.valueOf(firstNum));
        setInputtedNumber(true);
        mainActBtnsHandler.doOperation(operation,BigDecimal.valueOf(secondNum));
        setInputtedNumber(true);
        String display = mainActBtnsHandler.doOperation(operation,BigDecimal.valueOf(thirdNum));
        display = mainActBtnsHandler.doEnter(new BigDecimal(replaceComaToDot(removeCapacity(display))));
        assertEquals(result,mainActBtnsHandler.doEnter(new BigDecimal(replaceComaToDot(removeCapacity(display)))));
        mainActBtnsHandler.resetValues();
    }

    private void testFifthChain(long firstNum, long secondNum, MainOperation mOperation, Percent percent, String result){

        mainActBtnsHandler.doOperation(mOperation,BigDecimal.valueOf(firstNum));
        setInputtedNumber(true);
        String display = percentBtnHandler.doOperation(percent,BigDecimal.valueOf(firstNum),BigDecimal.valueOf(secondNum));
        display = mainActBtnsHandler.doEnter(new BigDecimal(replaceComaToDot(removeCapacity(display))));
        display = mainActBtnsHandler.doEnter(new BigDecimal(replaceComaToDot(removeCapacity(display))));
        display = mainActBtnsHandler.doEnter(new BigDecimal(replaceComaToDot(removeCapacity(display))));
        assertEquals(result,display);
        mainActBtnsHandler.resetValues();
    }

    private void testSixthChain(long firstNum,long secondNum,MainOperation mOperation,Percent percent,String result){

        mainActBtnsHandler.doOperation(mOperation,BigDecimal.valueOf(firstNum));
        setInputtedNumber(true);
        String display = percentBtnHandler.doOperation(percent,BigDecimal.valueOf(firstNum),BigDecimal.valueOf(secondNum));
        display = mainActBtnsHandler.doOperation(mOperation,new BigDecimal(replaceComaToDot(removeCapacity(display))));
        setInputtedNumber(true);
        display = mainActBtnsHandler.doEnter(new BigDecimal(replaceComaToDot(removeCapacity(display))));
        assertEquals(result,mainActBtnsHandler.doEnter(new BigDecimal(replaceComaToDot(removeCapacity(display)))));
        mainActBtnsHandler.resetValues();
    }

    private void testSeventhChain(long first,long second,long third,MainOperation mOperation,Percent percent,String result){

        mainActBtnsHandler.doOperation(mOperation,BigDecimal.valueOf(first));
        setInputtedNumber(true);
        String display = percentBtnHandler.doOperation(percent,BigDecimal.valueOf(first),BigDecimal.valueOf(second));
        display = mainActBtnsHandler.doOperation(mOperation,new BigDecimal(replaceComaToDot(removeCapacity(display))));
        setInputtedNumber(true);
        display = percentBtnHandler.doOperation(percent,new BigDecimal(replaceComaToDot(removeCapacity(display))),BigDecimal.valueOf(third));
        display = mainActBtnsHandler.doEnter(new BigDecimal(replaceComaToDot(removeCapacity(display))));
        assertEquals(result,mainActBtnsHandler.doEnter(new BigDecimal(replaceComaToDot(removeCapacity(display)))));
        mainActBtnsHandler.resetValues();
    }

    private void testEighthChain(long number, ExtraOperation eOperation,String result){

        String display = extraActBtnHandler.doOperation(eOperation,BigDecimal.valueOf(number));
        display = extraActBtnHandler.doOperation(eOperation,new BigDecimal(replaceComaToDot(removeCapacity(display))));
        display = extraActBtnHandler.doOperation(eOperation,new BigDecimal(replaceComaToDot(removeCapacity(display))));
        setOperationBefore(false);
        display = mainActBtnsHandler.doEnter(new BigDecimal(replaceComaToDot(removeCapacity(display))));
        assertEquals(result,mainActBtnsHandler.doEnter(new BigDecimal(replaceComaToDot(removeCapacity(display)))));
        mainActBtnsHandler.resetValues();
    }

    private void testNinthChain(long number,MainOperation mOperation,ExtraOperation eOperation,String result){

        String display = extraActBtnHandler.doOperation(eOperation,BigDecimal.valueOf(number));
        mainActBtnsHandler.doOperation(mOperation,new BigDecimal(replaceComaToDot(removeCapacity(display))));
        display = mainActBtnsHandler.doEnter(new BigDecimal(replaceComaToDot(removeCapacity(display))));
        assertEquals(result,mainActBtnsHandler.doEnter(new BigDecimal(replaceComaToDot(removeCapacity(display)))));
        mainActBtnsHandler.resetValues();
    }

    private void testTenthChain(long num,MainOperation fOperation,MainOperation sOperation,String result){

        setInputtedNumber(true);
        mainActBtnsHandler.doOperation(fOperation,new BigDecimal(num));
        setInputtedNumber(true);
        mainActBtnsHandler.doOperation(fOperation,new BigDecimal(num));
        setInputtedNumber(true);
        mainActBtnsHandler.doOperation(sOperation,new BigDecimal(num));
        setInputtedNumber(true);
        mainActBtnsHandler.doOperation(sOperation,new BigDecimal(num));
        setInputtedNumber(true);
        String display = mainActBtnsHandler.doEnter(new BigDecimal(num));
        assertEquals(result,mainActBtnsHandler.doEnter(new BigDecimal(replaceComaToDot(removeCapacity(display)))));
    }

    private void testFirstInvalidChain(long number,ExtraOperation eOperation,String message){

        assertEquals(message,extraActBtnHandler.doOperation(eOperation,new BigDecimal(number)));
    }

    private String setDisplay(String result,String display){

        if (!"".equals(result)){
            display = result;
        }
        return display;
    }

}
