package win_calculator;

import org.junit.jupiter.api.Test;
import win_calculator.exceptions.MyException;
import win_calculator.main_operations.*;

import java.math.BigDecimal;

import static org.junit.gen5.api.Assertions.assertEquals;
import static win_calculator.MainOperations.*;

class MainOperationsTest {

    private static final String FIFTY_UNITS = "11111111111111111111111111111111111111111111111111";
    private static final String FIFTY_NINES = "99999999999999999999999999999999999999999999999999";
    private static final String LONG_MAX_PLUS_ONE = "9223372036854775808";

    @Test
    void testSelectOperation() throws MyException {

        //test PLUS positive first, 0 second, 0 previousResult
        test(0,0,0,plus(),0);
        test(1,0,0,plus(),2);
        test(2,0,0,plus(),4);
        test(99,0,0,plus(),198);
        test(100,0,0,plus(),200);
        test(101,0,0,plus(),202);
        test(Integer.MAX_VALUE-2,0,0,plus(),4294967290L);
        test(Integer.MAX_VALUE-1,0,0,plus(),4294967292L);
        test(Integer.MAX_VALUE,0,0,plus(),4294967294L);
        test(Integer.MAX_VALUE+1L,0,0,plus(),4294967296L);
        test(Integer.MAX_VALUE+2L,0,0,plus(),4294967298L);
        test(Long.MAX_VALUE-2+"","0","0",plus(),"18446744073709551610");
        test(Long.MAX_VALUE-1+"","0","0",plus(),"18446744073709551612");
        test(Long.MAX_VALUE+"","0","0",plus(),"18446744073709551614");
        test(LONG_MAX_PLUS_ONE,"0","0",plus(),"18446744073709551616");
        test("9223372036854775809","0","0",plus(),"18446744073709551618");
        test(FIFTY_UNITS,"0","0",plus(),"22222222222222222222222222222222222222222222222222");
        test(FIFTY_NINES,"0","0",plus(),"199999999999999999999999999999999999999999999999998");

        //test PLUS negative first, 0 second, 0 previousResult
        test(-1,0,0,plus(),-2);
        test(-2,0,0,plus(),-4);
        test(-Integer.MAX_VALUE+2,0,0,plus(),-4294967290L);
        test(-Integer.MAX_VALUE+1,0,0,plus(),-4294967292L);
        test(-Integer.MAX_VALUE,0,0,plus(),-4294967294L);
        test(-Integer.MAX_VALUE-1L,0,0,plus(),-4294967296L);
        test(-Integer.MAX_VALUE-2L,0,0,plus(),-4294967298L);
        test(-Long.MAX_VALUE+2L+"","0","0",plus(),"-18446744073709551610");
        test(-Long.MAX_VALUE+1L+"","0","0",plus(),"-18446744073709551612");
        test(-Long.MAX_VALUE+"","0","0",plus(),"-18446744073709551614");
        test("-"+LONG_MAX_PLUS_ONE,"0","0",plus(),"-18446744073709551616");
        test("-9223372036854775809","0","0",plus(),"-18446744073709551618");
        test("-"+FIFTY_UNITS,"0","0",plus(),"-22222222222222222222222222222222222222222222222222");
        test("-"+FIFTY_NINES,"0","0",plus(),"-199999999999999999999999999999999999999999999999998");

        //test PLUS positive first, 1 second, 0 previousResult
        test(0,1,0,plus(),1);
        test(1,1,0,plus(),2);
        test(2,1,0,plus(),3);
        test(Integer.MAX_VALUE-2,1,0,plus(),2147483646);
        test(Integer.MAX_VALUE-1,1,0,plus(),2147483647);
        test(Integer.MAX_VALUE,1,0,plus(),2147483648L);
        test(Integer.MAX_VALUE+1L,1,0,plus(),2147483649L);
        test(Integer.MAX_VALUE+2L,1,0,plus(),2147483650L);
        test(Long.MAX_VALUE-2,1,0,plus(),9223372036854775806L);
        test(Long.MAX_VALUE-1,1,0,plus(),9223372036854775807L);
        test(Long.MAX_VALUE+"","1","0",plus(),"9223372036854775808");
        test(LONG_MAX_PLUS_ONE,"1","0",plus(),"9223372036854775809");
        test("9223372036854775809","1","0",plus(),"9223372036854775810");
        test(FIFTY_UNITS,"1","0",plus(),"11111111111111111111111111111111111111111111111112");
        test(FIFTY_NINES,"1","0",plus(),"100000000000000000000000000000000000000000000000000");

        //test PLUS positive first, 1 second, 5 previousResult
        test(0,1,5,plus(),1);
        test(1,1,5,plus(),2);
        test(2,1,5,plus(),3);
        test(Integer.MAX_VALUE-2,1,5,plus(),2147483646);
        test(Integer.MAX_VALUE-1,1,5,plus(),2147483647);
        test(Integer.MAX_VALUE,1,5,plus(),2147483648L);
        test(Integer.MAX_VALUE+1L,1,5,plus(),2147483649L);
        test(Integer.MAX_VALUE+2L,1,5,plus(),2147483650L);
        test(Long.MAX_VALUE-2,1,5,plus(),9223372036854775806L);
        test(Long.MAX_VALUE-1,1,5,plus(),9223372036854775807L);
        test(Long.MAX_VALUE+"","1","5",plus(),"9223372036854775808");
        test(LONG_MAX_PLUS_ONE,"1","5",plus(),"9223372036854775809");
        test("9223372036854775809","1","5",plus(),"9223372036854775810");
        test(FIFTY_UNITS,"1","5",plus(),"11111111111111111111111111111111111111111111111112");
        test(FIFTY_NINES,"1","5",plus(),"100000000000000000000000000000000000000000000000000");

        //test PLUS negative first, 1 second, 0 previousResult
        test(-1,1,0,plus(),0);
        test(-2,1,0,plus(),-1);
        test(-Integer.MAX_VALUE+2,1,0,plus(),-2147483644);
        test(-Integer.MAX_VALUE+1,1,0,plus(),-2147483645);
        test(-Integer.MAX_VALUE,1,0,plus(),-2147483646);
        test(-Integer.MAX_VALUE-1L,1,0,plus(),-2147483647);
        test(-Integer.MAX_VALUE-2L,1,0,plus(),-2147483648);
        test(-Long.MAX_VALUE+2,1,0,plus(),-9223372036854775804L);
        test(-Long.MAX_VALUE+1,1,0,plus(),-9223372036854775805L);
        test(-Long.MAX_VALUE,1,0,plus(),-9223372036854775806L);
        test("-"+LONG_MAX_PLUS_ONE,"1","0",plus(),"-9223372036854775807");
        test("-9223372036854775809","1","0",plus(),"-9223372036854775808");
        test("-"+FIFTY_UNITS,"1","0",plus(),"-11111111111111111111111111111111111111111111111110");
        test("-"+FIFTY_NINES,"1","0",plus(),"-99999999999999999999999999999999999999999999999998");

        //test PLUS positive first, 2 second, 0 previousResult
        test(0,2,0,plus(),2);
        test(1,2,0,plus(),3);
        test(2,2,0,plus(),4);
        test(Integer.MAX_VALUE-2,2,0,plus(),2147483647);
        test(Integer.MAX_VALUE-1,2,0,plus(),2147483648L);
        test(Integer.MAX_VALUE,2,0,plus(),2147483649L);
        test(Integer.MAX_VALUE+1L,2,0,plus(),2147483650L);
        test(Integer.MAX_VALUE+2L,2,0,plus(),2147483651L);
        test(Long.MAX_VALUE-2,2,0,plus(),9223372036854775807L);
        test(Long.MAX_VALUE-1+"","2","0",plus(),"9223372036854775808");
        test(Long.MAX_VALUE+"","2","0",plus(),"9223372036854775809");
        test(LONG_MAX_PLUS_ONE,"2","0",plus(),"9223372036854775810");
        test("9223372036854775809","2","0",plus(),"9223372036854775811");
        test(FIFTY_UNITS,"2","0",plus(),"11111111111111111111111111111111111111111111111113");
        test(FIFTY_NINES,"2","0",plus(),"100000000000000000000000000000000000000000000000001");

        //test PLUS negative first, 2 second, 0 previousResult
        test(-1,2,0,plus(),1);
        test(-2,2,0,plus(),0);
        test(-Integer.MAX_VALUE+2,2,0,plus(),-2147483643);
        test(-Integer.MAX_VALUE+1,2,0,plus(),-2147483644);
        test(-Integer.MAX_VALUE,2,0,plus(),-2147483645);
        test(-Integer.MAX_VALUE-1L,2,0,plus(),-2147483646);
        test(-Integer.MAX_VALUE-2L,2,0,plus(),-2147483647);
        test(-Long.MAX_VALUE+2,2,0,plus(),-9223372036854775803L);
        test(-Long.MAX_VALUE+1,2,0,plus(),-9223372036854775804L);
        test(-Long.MAX_VALUE,2,0,plus(),-9223372036854775805L);
        test("-"+LONG_MAX_PLUS_ONE,"2","0",plus(),"-9223372036854775806");
        test("-9223372036854775809","2","0",plus(),"-9223372036854775807");
        test("-"+FIFTY_UNITS,"2","0",plus(),"-11111111111111111111111111111111111111111111111109");
        test("-"+FIFTY_NINES,"2","0",plus(),"-99999999999999999999999999999999999999999999999997");

        //test PLUS positive first, Integer.MAX_VALUE-1 second, 0 previousResult
        test(0,Integer.MAX_VALUE-1,0,plus(),2147483646);
        test(1,Integer.MAX_VALUE-1,0,plus(),2147483647);
        test(2,Integer.MAX_VALUE-1,0,plus(),2147483648L);
        test(Integer.MAX_VALUE-2,Integer.MAX_VALUE-1,0,plus(),4294967291L);
        test(Integer.MAX_VALUE-1,Integer.MAX_VALUE-1,0,plus(),4294967292L);
        test(Integer.MAX_VALUE,Integer.MAX_VALUE-1,0,plus(),4294967293L);
        test(Integer.MAX_VALUE+1L,Integer.MAX_VALUE-1,0,plus(),4294967294L);
        test(Integer.MAX_VALUE+2L,Integer.MAX_VALUE-1,0,plus(),4294967295L);
        test(Long.MAX_VALUE-2+"",Integer.MAX_VALUE-1+"","0",plus(),"9223372039002259451");
        test(Long.MAX_VALUE-1+"",Integer.MAX_VALUE-1+"","0",plus(),"9223372039002259452");
        test(Long.MAX_VALUE+"",Integer.MAX_VALUE-1+"","0",plus(),"9223372039002259453");
        test(LONG_MAX_PLUS_ONE,Integer.MAX_VALUE-1+"","0",plus(),"9223372039002259454");
        test("9223372036854775809",Integer.MAX_VALUE-1+"","0",plus(),"9223372039002259455");
        test(FIFTY_UNITS,Integer.MAX_VALUE-1+"","0",plus(),"11111111111111111111111111111111111111113258594757");
        test(FIFTY_NINES,Integer.MAX_VALUE-1+"","0",plus(),"100000000000000000000000000000000000000002147483645");

        //test PLUS negative first, Integer.MAX_VALUE-1 second, 0 previousResult
        test(-1,Integer.MAX_VALUE-1,0,plus(),2147483645);
        test(-2,Integer.MAX_VALUE-1,0,plus(),2147483644);
        test(-Integer.MAX_VALUE+2,Integer.MAX_VALUE-1,0,plus(),1);
        test(-Integer.MAX_VALUE+1,Integer.MAX_VALUE-1,0,plus(),0);
        test(-Integer.MAX_VALUE,Integer.MAX_VALUE-1,0,plus(),-1);
        test(-Integer.MAX_VALUE-1L,Integer.MAX_VALUE-1,0,plus(),-2);
        test(-Integer.MAX_VALUE-2L,Integer.MAX_VALUE-1,0,plus(),-3);
        test(-Long.MAX_VALUE+2,Integer.MAX_VALUE-1,0,plus(),-9223372034707292159L);
        test(-Long.MAX_VALUE+1,Integer.MAX_VALUE-1,0,plus(),-9223372034707292160L);
        test(-Long.MAX_VALUE,Integer.MAX_VALUE-1,0,plus(),-9223372034707292161L);
        test("-"+LONG_MAX_PLUS_ONE,Integer.MAX_VALUE-1+"","0",plus(),"-9223372034707292162");
        test("-9223372036854775809",Integer.MAX_VALUE-1+"","0",plus(),"-9223372034707292163");
        test("-"+FIFTY_UNITS,Integer.MAX_VALUE-1+"","0",plus(),"-11111111111111111111111111111111111111108963627465");
        test("-"+FIFTY_NINES,Integer.MAX_VALUE-1+"","0",plus(),"-99999999999999999999999999999999999999997852516353");

        //test PLUS positive first, Integer.MAX_VALUE second, 0 previousResult
        test(0,Integer.MAX_VALUE,0,plus(),2147483647);
        test(1,Integer.MAX_VALUE,0,plus(),2147483648L);
        test(2,Integer.MAX_VALUE,0,plus(),2147483649L);
        test(Integer.MAX_VALUE-2,Integer.MAX_VALUE,0,plus(),4294967292L);
        test(Integer.MAX_VALUE-1,Integer.MAX_VALUE,0,plus(),4294967293L);
        test(Integer.MAX_VALUE,Integer.MAX_VALUE,0,plus(),4294967294L);
        test(Integer.MAX_VALUE+1L,Integer.MAX_VALUE,0,plus(),4294967295L);
        test(Integer.MAX_VALUE+2L,Integer.MAX_VALUE,0,plus(),4294967296L);
        test(Long.MAX_VALUE-2+"",Integer.MAX_VALUE+"","0",plus(),"9223372039002259452");
        test(Long.MAX_VALUE-1+"",Integer.MAX_VALUE+"","0",plus(),"9223372039002259453");
        test(Long.MAX_VALUE+"",Integer.MAX_VALUE+"","0",plus(),"9223372039002259454");
        test(LONG_MAX_PLUS_ONE,Integer.MAX_VALUE+"","0",plus(),"9223372039002259455");
        test("9223372036854775809",Integer.MAX_VALUE+"","0",plus(),"9223372039002259456");
        test(FIFTY_UNITS,Integer.MAX_VALUE+"","0",plus(),"11111111111111111111111111111111111111113258594758");
        test(FIFTY_NINES,Integer.MAX_VALUE+"","0",plus(),"100000000000000000000000000000000000000002147483646");

        //test PLUS negative first, Integer.MAX_VALUE second, 0 previousResult
        test(-1,Integer.MAX_VALUE,0,plus(),2147483646);
        test(-2,Integer.MAX_VALUE,0,plus(),2147483645);
        test(-Integer.MAX_VALUE+2,Integer.MAX_VALUE,0,plus(),2);
        test(-Integer.MAX_VALUE+1,Integer.MAX_VALUE,0,plus(),1);
        test(-Integer.MAX_VALUE,Integer.MAX_VALUE,0,plus(),0);
        test(-Integer.MAX_VALUE-1L,Integer.MAX_VALUE,0,plus(),-1);
        test(-Integer.MAX_VALUE-2L,Integer.MAX_VALUE,0,plus(),-2);
        test(-Long.MAX_VALUE+2,Integer.MAX_VALUE,0,plus(),-9223372034707292158L);
        test(-Long.MAX_VALUE+1,Integer.MAX_VALUE,0,plus(),-9223372034707292159L);
        test(-Long.MAX_VALUE,Integer.MAX_VALUE,0,plus(),-9223372034707292160L);
        test("-"+LONG_MAX_PLUS_ONE,Integer.MAX_VALUE+"","0",plus(),"-9223372034707292161");
        test("-9223372036854775809",Integer.MAX_VALUE+"","0",plus(),"-9223372034707292162");
        test("-"+FIFTY_UNITS,Integer.MAX_VALUE+"","0",plus(),"-11111111111111111111111111111111111111108963627464");
        test("-"+FIFTY_NINES,Integer.MAX_VALUE+"","0",plus(),"-99999999999999999999999999999999999999997852516352");

        //test PLUS positive first, Integer.MAX_VALUE+1 second, 0 previousResult
        test(0,Integer.MAX_VALUE+1L,0,plus(),2147483648L);
        test(1,Integer.MAX_VALUE+1L,0,plus(),2147483649L);
        test(2,Integer.MAX_VALUE+1L,0,plus(),2147483650L);
        test(Integer.MAX_VALUE-2,Integer.MAX_VALUE+1L,0,plus(),4294967293L);
        test(Integer.MAX_VALUE-1,Integer.MAX_VALUE+1L,0,plus(),4294967294L);
        test(Integer.MAX_VALUE,Integer.MAX_VALUE+1L,0,plus(),4294967295L);
        test(Integer.MAX_VALUE+1L,Integer.MAX_VALUE+1L,0,plus(),4294967296L);
        test(Integer.MAX_VALUE+2L,Integer.MAX_VALUE+1L,0,plus(),4294967297L);
        test(Long.MAX_VALUE-2+"",Integer.MAX_VALUE+1L+"","0",plus(),"9223372039002259453");
        test(Long.MAX_VALUE-1+"",Integer.MAX_VALUE+1L+"","0",plus(),"9223372039002259454");
        test(Long.MAX_VALUE+"",Integer.MAX_VALUE+1L+"","0",plus(),"9223372039002259455");
        test(LONG_MAX_PLUS_ONE,Integer.MAX_VALUE+1L+"","0",plus(),"9223372039002259456");
        test("9223372036854775809",Integer.MAX_VALUE+1L+"","0",plus(),"9223372039002259457");
        test(FIFTY_UNITS,Integer.MAX_VALUE+1L+"","0",plus(),"11111111111111111111111111111111111111113258594759");
        test(FIFTY_NINES,Integer.MAX_VALUE+1L+"","0",plus(),"100000000000000000000000000000000000000002147483647");

        //test PLUS negative first, Integer.MAX_VALUE+1 second, 0 previousResult
        test(-1,Integer.MAX_VALUE+1L,0,plus(),2147483647);
        test(-2,Integer.MAX_VALUE+1L,0,plus(),2147483646);
        test(-Integer.MAX_VALUE+2,Integer.MAX_VALUE+1L,0,plus(),3);
        test(-Integer.MAX_VALUE+1,Integer.MAX_VALUE+1L,0,plus(),2);
        test(-Integer.MAX_VALUE,Integer.MAX_VALUE+1L,0,plus(),1);
        test(-Integer.MAX_VALUE-1L,Integer.MAX_VALUE+1L,0,plus(),0);
        test(-Integer.MAX_VALUE-2L,Integer.MAX_VALUE+1L,0,plus(),-1);
        test(-Long.MAX_VALUE+2,Integer.MAX_VALUE+1L,0,plus(),-9223372034707292157L);
        test(-Long.MAX_VALUE+1,Integer.MAX_VALUE+1L,0,plus(),-9223372034707292158L);
        test(-Long.MAX_VALUE,Integer.MAX_VALUE+1L,0,plus(),-9223372034707292159L);
        test("-"+LONG_MAX_PLUS_ONE,Integer.MAX_VALUE+1L+"","0",plus(),"-9223372034707292160");
        test("-9223372036854775809",Integer.MAX_VALUE+1L+"","0",plus(),"-9223372034707292161");
        test("-"+FIFTY_UNITS,Integer.MAX_VALUE+1L+"","0",plus(),"-11111111111111111111111111111111111111108963627463");
        test("-"+FIFTY_NINES,Integer.MAX_VALUE+1L+"","0",plus(),"-99999999999999999999999999999999999999997852516351");


        //test PLUS positive first, Long.MAX_VALUE-1 second, 0 previousResult
        test(0,Long.MAX_VALUE-1,0,plus(),9223372036854775806L);
        test(1,Long.MAX_VALUE-1,0,plus(),9223372036854775807L);
        test("2",Long.MAX_VALUE-1+"","0",plus(),"9223372036854775808");
        test(Integer.MAX_VALUE-2+"",Long.MAX_VALUE-1+"","0",plus(),"9223372039002259451");
        test(Integer.MAX_VALUE-1+"",Long.MAX_VALUE-1+"","0",plus(),"9223372039002259452");
        test(Integer.MAX_VALUE+"",Long.MAX_VALUE-1+"","0",plus(),"9223372039002259453");
        test(Integer.MAX_VALUE+1L+"",Long.MAX_VALUE-1+"","0",plus(),"9223372039002259454");
        test(Integer.MAX_VALUE+2L+"",Long.MAX_VALUE-1+"","0",plus(),"9223372039002259455");
        test(Long.MAX_VALUE-2+"",Long.MAX_VALUE-1+"","0",plus(),"18446744073709551611");
        test(Long.MAX_VALUE-1+"",Long.MAX_VALUE-1+"","0",plus(),"18446744073709551612");
        test(Long.MAX_VALUE+"",Long.MAX_VALUE-1+"","0",plus(),"18446744073709551613");
        test(LONG_MAX_PLUS_ONE,Long.MAX_VALUE-1+"","0",plus(),"18446744073709551614");
        test("9223372036854775809",Long.MAX_VALUE-1+"","0",plus(),"18446744073709551615");
        test(FIFTY_UNITS,Long.MAX_VALUE-1+"","0",plus(),"11111111111111111111111111111120334483147965886917");
        test(FIFTY_NINES,Long.MAX_VALUE-1+"","0",plus(),"100000000000000000000000000000009223372036854775805");

        //test PLUS negative first, Long.MAX_VALUE-1 second, 0 previousResult
        test(-1,Long.MAX_VALUE-1,0,plus(),9223372036854775805L);
        test(-2,Long.MAX_VALUE-1,0,plus(),9223372036854775804L);
        test(-Integer.MAX_VALUE+2,Long.MAX_VALUE-1,0,plus(),9223372034707292161L);
        test(-Integer.MAX_VALUE+1,Long.MAX_VALUE-1,0,plus(),9223372034707292160L);
        test(-Integer.MAX_VALUE,Long.MAX_VALUE-1,0,plus(),9223372034707292159L);
        test(-Integer.MAX_VALUE-1L,Long.MAX_VALUE-1,0,plus(),9223372034707292158L);
        test(-Integer.MAX_VALUE-2L,Long.MAX_VALUE-1,0,plus(),9223372034707292157L);
        test(-Long.MAX_VALUE+2,Long.MAX_VALUE-1,0,plus(),1);
        test(-Long.MAX_VALUE+1,Long.MAX_VALUE-1,0,plus(),0);
        test(-Long.MAX_VALUE,Long.MAX_VALUE-1,0,plus(),-1);
        test("-"+LONG_MAX_PLUS_ONE,Long.MAX_VALUE-1+"","0",plus(),"-2");
        test("-9223372036854775809",Long.MAX_VALUE-1+"","0",plus(),"-3");
        test("-"+FIFTY_UNITS,Long.MAX_VALUE-1+"","0",plus(),"-11111111111111111111111111111101887739074256335305");
        test("-"+FIFTY_NINES,Long.MAX_VALUE-1+"","0",plus(),"-99999999999999999999999999999990776627963145224193");

        //test PLUS positive first, Long.MAX_VALUE second, 0 previousResult
        test(0,Long.MAX_VALUE,0,plus(),9223372036854775807L);
        test("1",Long.MAX_VALUE+"","0",plus(),"9223372036854775808");
        test("2",Long.MAX_VALUE+"","0",plus(),"9223372036854775809");
        test(Integer.MAX_VALUE-2+"",Long.MAX_VALUE+"","0",plus(),"9223372039002259452");
        test(Integer.MAX_VALUE-1+"",Long.MAX_VALUE+"","0",plus(),"9223372039002259453");
        test(Integer.MAX_VALUE+"",Long.MAX_VALUE+"",0+"",plus(),"9223372039002259454");
        test(Integer.MAX_VALUE+1L+"",Long.MAX_VALUE+"","0",plus(),"9223372039002259455");
        test(Integer.MAX_VALUE+2L+"",Long.MAX_VALUE+"","0",plus(),"9223372039002259456");
        test(Long.MAX_VALUE-2+"",Long.MAX_VALUE+"","0",plus(),"18446744073709551612");
        test(Long.MAX_VALUE-1+"",Long.MAX_VALUE+"","0",plus(),"18446744073709551613");
        test(Long.MAX_VALUE+"",Long.MAX_VALUE+"","0",plus(),"18446744073709551614");
        test(LONG_MAX_PLUS_ONE,Long.MAX_VALUE+"","0",plus(),"18446744073709551615");
        test("9223372036854775809",Long.MAX_VALUE+"","0",plus(),"18446744073709551616");
        test(FIFTY_UNITS,Integer.MAX_VALUE+"","0",plus(),"11111111111111111111111111111111111111113258594758");
        test(FIFTY_NINES,Integer.MAX_VALUE+"","0",plus(),"100000000000000000000000000000000000000002147483646");

        //test PLUS negative first, Long.MAX_VALUE second, 0 previousResult
        test(-1,Long.MAX_VALUE,0,plus(),9223372036854775806L);
        test(-2,Long.MAX_VALUE,0,plus(),9223372036854775805L);
        test(-Integer.MAX_VALUE+1,Long.MAX_VALUE,0,plus(),9223372034707292161L);
        test(-Integer.MAX_VALUE,Long.MAX_VALUE,0,plus(),9223372034707292160L);
        test(-Integer.MAX_VALUE-1L,Long.MAX_VALUE,0,plus(),9223372034707292159L);
        test(-Long.MAX_VALUE+1,Long.MAX_VALUE,0,plus(),1);
        test(-Long.MAX_VALUE,Long.MAX_VALUE,0,plus(),0);
        test("-"+LONG_MAX_PLUS_ONE,Long.MAX_VALUE+"","0",plus(),"-1");
        test("-"+FIFTY_UNITS,Long.MAX_VALUE+"","0",plus(),"-11111111111111111111111111111101887739074256335304");
        test("-"+FIFTY_NINES,Long.MAX_VALUE+"","0",plus(),"-99999999999999999999999999999990776627963145224192");

        //test PLUS positive first, Long.MAX_VALUE+1 second, 0 previousResult
        test("0", LONG_MAX_PLUS_ONE,"0",plus(),LONG_MAX_PLUS_ONE);
        test("1", LONG_MAX_PLUS_ONE,"0",plus(),"9223372036854775809");
        test("2", LONG_MAX_PLUS_ONE,"0",plus(),"9223372036854775810");
        test(Integer.MAX_VALUE-1+"", LONG_MAX_PLUS_ONE,"0",plus(),"9223372039002259454");
        test(Integer.MAX_VALUE+"", LONG_MAX_PLUS_ONE,"0",plus(),"9223372039002259455");
        test(Integer.MAX_VALUE+1L+"", LONG_MAX_PLUS_ONE,"0",plus(),"9223372039002259456");
        test(Long.MAX_VALUE-1+"", LONG_MAX_PLUS_ONE,"0",plus(),"18446744073709551614");
        test(Long.MAX_VALUE+"", LONG_MAX_PLUS_ONE,"0",plus(),"18446744073709551615");
        test(LONG_MAX_PLUS_ONE, LONG_MAX_PLUS_ONE,"0",plus(),"18446744073709551616");
        test(FIFTY_UNITS,Integer.MAX_VALUE+"","0",plus(),"11111111111111111111111111111111111111113258594758");
        test(FIFTY_NINES,Integer.MAX_VALUE+"","0",plus(),"100000000000000000000000000000000000000002147483646");

        //test PLUS negative first, Long.MAX_VALUE+1 second, 0 previousResult
        test("-1", LONG_MAX_PLUS_ONE,"0",plus(),"9223372036854775807");
        test("-2", LONG_MAX_PLUS_ONE,"0",plus(),"9223372036854775806");
        test(-Integer.MAX_VALUE+1+"", LONG_MAX_PLUS_ONE,"0",plus(),"9223372034707292162");
        test(-Integer.MAX_VALUE+"", LONG_MAX_PLUS_ONE,"0",plus(),"9223372034707292161");
        test(-Integer.MAX_VALUE-1L+"", LONG_MAX_PLUS_ONE,"0",plus(),"9223372034707292160");
        test(-Long.MAX_VALUE+1+"", LONG_MAX_PLUS_ONE,"0",plus(),"2");
        test(-Long.MAX_VALUE+"", LONG_MAX_PLUS_ONE,"0",plus(),"1");
        test("-"+ LONG_MAX_PLUS_ONE, LONG_MAX_PLUS_ONE,"0",plus(),"0");
        test("-"+FIFTY_UNITS, LONG_MAX_PLUS_ONE,"0",plus(),"-11111111111111111111111111111101887739074256335303");
        test("-"+FIFTY_NINES, LONG_MAX_PLUS_ONE,"0",plus(),"-99999999999999999999999999999990776627963145224191");

    }

    private void test(long first,long second,long previousResult,MainOperation operation,long expectedResult) throws MyException {

        setFirstNumber(first+"");
        setSecondNumber(second+"");
        setResult(previousResult+"");
        selectOperation(operation,false);
        assertEquals(BigDecimal.valueOf(expectedResult), getResult());
    }

    private void test(String first,String second,String previousResult,MainOperation operation,String expectedResult) throws MyException {

        setFirstNumber(first);
        setSecondNumber(second);
        setResult(previousResult);
        selectOperation(operation,false);
        assertEquals(new BigDecimal(expectedResult), getResult());
    }

    private MainOperation plus(){
        return new Plus();
    }

    private MainOperation minus(){
        return new Minus();
    }

    private MainOperation multiply(){
        return new Multiply();
    }

    private MainOperation divide(){
        return new Divide();
    }
}
