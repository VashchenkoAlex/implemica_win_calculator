package win_calculator;

import org.junit.Rule;
import org.junit.jupiter.api.Test;
import win_calculator.main_operations.*;

import static org.junit.gen5.api.Assertions.assertEquals;
import static win_calculator.MainOperations.*;
import static win_calculator.StringUtils.addSpaces;

class ControllerTest {

    @Rule
    private final Controller controller = new Controller();
    private final Plus plus = new Plus();
    private final Minus minus = new Minus();
    private final Multiply multiply = new Multiply();
    private final Divide divide = new Divide();

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
    private void testFirstChain(long displayedNumber, MainOperation operation, long expResult){

        String display = displayedNumber+"";
        display = setDisplay(controller.doMainOperation(operation,display),display);
        display = setDisplay(controller.doEnter(display),display);
        display = setDisplay(controller.doEnter(display),display);
        display = controller.doEnter(display);
        assertEquals(expResult+"",display);
        resetValues();
    }

    private void testSecondChain(long firstNum,long secondNum,long thirdNum,long fourthNum,
                                 MainOperation operation,String expResult){

        String display = firstNum+"";
        controller.doMainOperation(operation,display);
        display = secondNum+"";
        controller.setWasNumber(true);
        controller.doMainOperation(operation,display);
        display = thirdNum+"";
        controller.setWasNumber(true);
        controller.doMainOperation(operation,display);
        display = fourthNum+"";
        controller.setWasNumber(true);
        display = controller.doEnter(display);
        assertEquals(addSpaces(expResult+"",1),controller.doEnter(display));
        ;
        resetValues();
    }

    private void testThirdChain(long firstNum,long secondNum,MainOperation operation,long expResult){

        String display = firstNum+"";
        controller.doMainOperation(operation,display);
        display = secondNum+"";
        controller.setWasNumber(true);
        display = controller.doMainOperation(operation,display);
        display = controller.doEnter(display);
        assertEquals(expResult+"",controller.doEnter(display));
        resetValues();
    }
    private String setDisplay(String result,String display){

        if (!"".equals(result)){
            display = result;
        }
        return display;
    }

}
