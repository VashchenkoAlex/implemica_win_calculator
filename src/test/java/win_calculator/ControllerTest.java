package win_calculator;

import org.junit.jupiter.api.Test;
import win_calculator.buttons_handlers.ExtraActionButtonsHandler;
import win_calculator.buttons_handlers.MainActionButtonsHandler;
import win_calculator.extra_operations.ExtraOperation;
import win_calculator.extra_operations.Percent;
import win_calculator.main_operations.*;

import java.math.BigDecimal;

import static org.junit.gen5.api.Assertions.assertEquals;
import static win_calculator.Controller.setInputtedNumber;
import static win_calculator.StringUtils.addCapacity;
import static win_calculator.StringUtils.removeCapacity;
import static win_calculator.StringUtils.replaceComaToDot;

class ControllerTest {

    private final MainActionButtonsHandler mainActBtnsHandler = new MainActionButtonsHandler();
    private final ExtraActionButtonsHandler extraActBtnHandler = new ExtraActionButtonsHandler();
    private final Plus plus = new Plus();
    private final Minus minus = new Minus();
    private final Multiply multiply = new Multiply();
    private final Divide divide = new Divide();
    private final Percent percent = new Percent();

    @Test
    void testChainNumActEnterEnterEnter(){

        //Test Plus
        testFirstChain(0,plus,0);
        testFirstChain(1,plus,4);
        testFirstChain(2,plus,8);

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

        testThirdChain(2,3,plus,15);
    }

    @Test
    void testChainNumActNumEActEnterEnterEnter(){

        testFourthChain(100,10,plus,percent,"130");
        testFourthChain(100,10,minus,percent,"70");
        testFourthChain(100,10,multiply,percent,"100 000");
        testFourthChain(100,10,divide,percent,"0,1");
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

    private void testThirdChain(long firstNum, long secondNum, MainOperation operation, long expResult){

        mainActBtnsHandler.doOperation(operation, BigDecimal.valueOf(firstNum));
        setInputtedNumber(true);
        String display = mainActBtnsHandler.doOperation(operation,BigDecimal.valueOf(secondNum));
        display = mainActBtnsHandler.doEnter(new BigDecimal(display));
        assertEquals(expResult+"", mainActBtnsHandler.doEnter(new BigDecimal(display)));
        mainActBtnsHandler.resetValues();
    }

    private void testFourthChain(long firstNum, long secondNum, MainOperation mOperation, ExtraOperation eOperation,String result){

        mainActBtnsHandler.doOperation(mOperation,BigDecimal.valueOf(firstNum));
        setInputtedNumber(true);
        String display = extraActBtnHandler.doOperation(eOperation,BigDecimal.valueOf(firstNum),BigDecimal.valueOf(secondNum));
        display = mainActBtnsHandler.doEnter(new BigDecimal(replaceComaToDot(removeCapacity(display))));
        display = mainActBtnsHandler.doEnter(new BigDecimal(replaceComaToDot(removeCapacity(display))));
        display = mainActBtnsHandler.doEnter(new BigDecimal(replaceComaToDot(removeCapacity(display))));
        assertEquals(result,display);
    }
    private String setDisplay(String result,String display){

        if (!"".equals(result)){
            display = result;
        }
        return display;
    }

}
