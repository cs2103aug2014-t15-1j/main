package parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestDateParser {

    @Test
    public void testCurrDtGetters() {
        System.out.println("\n>> Testing Current Date/Time...");

        // Cannot use automated tests for this because current time is
        // constantly changing
        System.out.println("DateTime: " + DateParser.getCurrDateTime());
        System.out.println("DateTimeStr: " + DateParser.getCurrDateTimeStr());
        System.out.println("Date: " + DateParser.getCurrDateStr());
        System.out.println("Time: " + DateParser.getCurrTimeStr());

        System.out.println("...success!");
    }
    
    @Test
    public void testValidTime() {
        System.out.println("\n>> Testing isValidTime...");

        // Lower Boundary
        assertTrue(DateParser.isValidTime("0000"));
        assertTrue(DateParser.isValidTime("0001"));
        assertTrue(DateParser.isValidTime("0100"));
        assertFalse(DateParser.isValidTime("-001"));
        // Upper Boundary
        assertTrue(DateParser.isValidTime("2359"));
        assertFalse(DateParser.isValidTime("2400"));
        assertFalse(DateParser.isValidTime("0060"));
        // Extreme
        assertFalse(DateParser.isValidTime("9999"));
        assertFalse(DateParser.isValidTime("abc"));

        System.out.println("...success!");
    }
   
    @Test
    public void testValidDate() {
        System.out.println("\n>> Testing isValidDate...");

        // Lower Boundary
        assertTrue(DateParser.isValidDate("01/01/1819"));
        assertFalse(DateParser.isValidDate("00/01/1819"));
        assertFalse(DateParser.isValidDate("01/00/1819"));
        assertFalse(DateParser.isValidDate("01/01/1818"));
        // Upper Boundary
        assertTrue(DateParser.isValidDate("31/12/9999"));
        assertFalse(DateParser.isValidDate("32/12/9999"));
        assertFalse(DateParser.isValidDate("31/13/9999"));
        // Upper Boundary (Leap Years)
        assertTrue(DateParser.isValidDate("29/02/2016"));
        assertFalse(DateParser.isValidDate("30/02/2016"));
        assertTrue(DateParser.isValidDate("28/02/2100"));
        assertFalse(DateParser.isValidDate("29/02/2100"));
        // Extreme
        assertFalse(DateParser.isValidDate("-1/02/2016"));
        assertFalse(DateParser.isValidDate("01/-2/2016"));
        assertFalse(DateParser.isValidDate("01/02/-016"));
        assertFalse(DateParser.isValidDate("today"));
        assertFalse(DateParser.isValidDate("2359"));

        System.out.println("...success!");
    }
    
    @Test
    public void testValidDateTime() {
        System.out.println("\n>> Testing isValidDateTime...");

        System.out.println("//DateTimeValid:");
        System.out.println("23/04/2014 " + DateParser.isValidDateTime("23/04/2014"));
        System.out.println("2359 " + DateParser.isValidDateTime("2359"));
        System.out.println("23/04/2014 2200 " +
                           DateParser.isValidDateTime("23/04/2014 2200"));
        System.out.println("2200 23/04/2014 " +
                           DateParser.isValidDateTime("2200 23/04/2014"));
        System.out.println("2200 29/02/2014 " +
                           DateParser.isValidDateTime("2200 29/02/2014"));
        System.out.println("2200 29/02/2012 " +
                           DateParser.isValidDateTime("2200 29/02/2012"));
        System.out.println("23/04/2014 2400 " +
                           DateParser.isValidDateTime("23/04/2014 2400"));
        System.out.println("23/13/2014 2200 " +
                           DateParser.isValidDateTime("23/13/2014 2200"));
        System.out.println("23/04/2014 asdd " +
                           DateParser.isValidDateTime("23/04/2014 asdd"));
        System.out.println("aaaaaaaaaa 2200 " +
                           DateParser.isValidDateTime("aaaaaaaaaa 2200"));
        System.out.println("aaaaaaaaaa aaaa " +
                           DateParser.isValidDateTime("aaaaaaaaaa aaaa"));
        
        System.out.println("...success!");
    }
    
    @Test
    public void testParseToDate() {
        System.out.println("\n>> Testing parseToDate...");

        System.out.println("//DateParser.parseToDate:");
        System.out.println("23/04/2014: " + DateParser.parseToDateTime("23/04/2014"));
        System.out.println("2200: " + DateParser.parseToDateTime("2200"));
        System.out.println("23/04/2014 2200: " +
                           DateParser.parseToDateTime("23/04/2014 2200"));
        System.out.println("2200 23/04/2014: " +
                           DateParser.parseToDateTime("2200 23/04/2014"));
        // TODO: test exception
        // System.out.println("exception: " + DateParser.parseToDate("aaa"));

        System.out.println("...success!");
    }
    
    @Test
    public void testContains() {
        System.out.println("\n>> Testing parseToDate...");

        // TODO: contains__ and getFirst__
        System.out.println("...success!");
    }
    
}
