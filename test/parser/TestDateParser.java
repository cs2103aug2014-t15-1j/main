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
        System.out.println("TodayDateStr: " + DateParser.getCurrDateStr());
        System.out.println("TmrDateStr: " + DateParser.getTmrDateStr());
        System.out.println("DateStr 7 days from now: " + DateParser.getDateFromNowStr(7));
        System.out.println("DateStr 30 days from now: " + DateParser.getDateFromNowStr(30));
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
        // Word Dates
        assertTrue(DateParser.isValidDate("today"));
        assertTrue(DateParser.isValidDate("tomorrow"));
        assertTrue(DateParser.isValidDate("tmr"));
        // Extreme
        assertFalse(DateParser.isValidDate("-1/02/2016"));
        assertFalse(DateParser.isValidDate("01/-2/2016"));
        assertFalse(DateParser.isValidDate("01/02/-016"));
        assertFalse(DateParser.isValidDate("2359"));

        System.out.println("...success!");
    }
    
    @Test
    public void testValidDateTime() {
        System.out.println("\n>> Testing isValidDateTime...");

        // Date only cases should follow isValidDate
        assertTrue(DateParser.isValidDateTime("01/01/1819"));
        assertFalse(DateParser.isValidDateTime("00/01/1819"));
        // Leap Years
        assertTrue(DateParser.isValidDateTime("29/02/2012"));
        assertFalse(DateParser.isValidDateTime("30/02/2012"));
        assertTrue(DateParser.isValidDateTime("28/02/2011"));
        assertFalse(DateParser.isValidDateTime("29/02/2011"));
        // Time only cases should follow isValidTime
        assertTrue(DateParser.isValidDateTime("2359"));
        assertFalse(DateParser.isValidDateTime("2400"));
        // Date-Time order (Valid, invalid date, invalid time)
        assertTrue(DateParser.isValidDateTime("01/01/1819 0000"));
        assertFalse(DateParser.isValidDateTime("01/01/1819 000"));
        assertFalse(DateParser.isValidDateTime("01/00/1819 0000"));
        assertFalse(DateParser.isValidDateTime("01/01/1819 2400"));
        assertTrue(DateParser.isValidDateTime("today 0000"));
        // Time-Date order
        assertTrue(DateParser.isValidDateTime("0000 01/01/1819"));
        assertFalse(DateParser.isValidDateTime("000 01/01/1819"));
        assertFalse(DateParser.isValidDateTime("0000 01/00/1819"));
        assertFalse(DateParser.isValidDateTime("2400 01/01/1819"));
        assertTrue(DateParser.isValidDateTime("0000 today"));
        // Extreme cases
        assertFalse(DateParser.isValidDateTime("aaa"));
        assertFalse(DateParser.isValidDateTime("aaaa 01/01/1819"));
        assertFalse(DateParser.isValidDateTime("0000 aa/aa/aaaa"));
        
        System.out.println("...success!");
    }
    
    @Test
    public void testParseToDate() {
        System.out.println("\n>> Testing parseToDate...");

        String expected;
        String result;
        
        // null
        expected = "";
        result = DateParser.parseToDateTime(null).toString();
        assertEquals(expected, result);
        // Empty
        expected = "";
        result = DateParser.parseToDateTime("").toString();
        assertEquals(expected, result);
        // Valid date
        expected = "01/01/1819";
        result = DateParser.parseToDateTime("01/01/1819").toString();
        assertEquals(expected, result);
        // Valid time
        expected = DateParser.getCurrDateStr() + " 0000";
        result = DateParser.parseToDateTime("0000").toString();
        assertEquals(expected, result);
        // Valid Date-Time
        expected = "01/01/1819 2200";
        result = DateParser.parseToDateTime("01/01/1819 2200").toString();
        assertEquals(expected, result);
        // Valid Time-Date
        expected = "01/01/1819 2200";
        result = DateParser.parseToDateTime("2200 01/01/1819").toString();
        assertEquals(expected, result);
        
        System.out.println("...success!");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void failParseInvalid() {
        System.out.println("\n>> Failing parseToDate with invalid input...");

        DateParser.parseToDateTime("day");

        System.out.println("...success!");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void failParseInvalidDate() {
        System.out.println("\n>> Failing parseToDate with invalid date...");

        DateParser.parseToDateTime("23/04/1818");

        System.out.println("...success!");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void failParseInvalidTime() {
        System.out.println("\n>> Failing parseToDate with invalid time...");

        DateParser.parseToDateTime("2400");

        System.out.println("...success!");
    }
    
    @Test
    public void testContains() {
        System.out.println("\n>> Testing parseToDate...");

        // TODO: contains__ and getFirst__
        System.out.println("...success!");
    }
    
}
