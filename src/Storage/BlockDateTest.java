package Storage;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Unit tests for the BlockDate class.
 * 
 * @author Pierce Anderson Fu
 * 
 */

public class BlockDateTest {

    @Test
    public void testDefaultConstructorHasEmptyAttributes() throws Exception {
        BlockDate bD = new BlockDate();
        assertEquals("Empty start date", bD.getStartDate(), "");
        assertEquals("Empty start time", bD.getStartTime(), "");
        assertEquals("Empty end date", bD.getEndDate(), "");
        assertEquals("Empty end time", bD.getEndTime(), "");
    }

    @Test
    public void testConstructorStoresDateTimeArgs() throws Exception {
        DateTime start = new DateTime("1/1/2014", "1111");
        DateTime end = new DateTime("2/2/2014", "2222");
        BlockDate bD = new BlockDate(start, end);
        assertEquals("Cloned start date", bD.getStartDate(), "1/1/2014");
        assertEquals("Cloned start time", bD.getStartTime(), "1111");
        assertEquals("Cloned end date", bD.getEndDate(), "2/2/2014");
        assertEquals("Cloned end time", bD.getEndTime(), "2222");
        start.resetDateTime();
        end.resetDateTime();
        assertEquals("Cloned start date by value", bD.getStartDate(),
                     "1/1/2014");
        assertEquals("Cloned start time by value", bD.getStartTime(), "1111");
        assertEquals("Cloned end date by value", bD.getEndDate(), "2/2/2014");
        assertEquals("Cloned end time by value", bD.getEndTime(), "2222");
    }

    @Test
    public void testResetStartDate() throws Exception {
        DateTime start = new DateTime("1/1/2014", "1111");
        DateTime end = new DateTime("2/2/2014", "2222");
        BlockDate bD = new BlockDate(start, end);
        assertEquals("Cloned start date", bD.getStartDate(), "1/1/2014");
        assertEquals("Cloned start time", bD.getStartTime(), "1111");
        assertEquals("Cloned end date", bD.getEndDate(), "2/2/2014");
        assertEquals("Cloned end time", bD.getEndTime(), "2222");
        bD.resetStartDate();
        assertEquals("Empty start date", bD.getStartDate(), "");
        assertEquals("Empty start time", bD.getStartTime(), "1111");
        assertEquals("Not empty end date", bD.getEndDate(), "2/2/2014");
        assertEquals("Not empty end time", bD.getEndTime(), "2222");
    }

    @Test
    public void testResetStartTime() throws Exception {
        DateTime start = new DateTime("1/1/2014", "1111");
        DateTime end = new DateTime("2/2/2014", "2222");
        BlockDate bD = new BlockDate(start, end);
        assertEquals("Cloned start date", bD.getStartDate(), "1/1/2014");
        assertEquals("Cloned start time", bD.getStartTime(), "1111");
        assertEquals("Cloned end date", bD.getEndDate(), "2/2/2014");
        assertEquals("Cloned end time", bD.getEndTime(), "2222");
        bD.resetStartTime();
        assertEquals("Empty start date", bD.getStartDate(), "1/1/2014");
        assertEquals("Empty start time", bD.getStartTime(), "");
        assertEquals("Not empty end date", bD.getEndDate(), "2/2/2014");
        assertEquals("Not empty end time", bD.getEndTime(), "2222");
    }

    @Test
    public void testResetStart() throws Exception {
        DateTime start = new DateTime("1/1/2014", "1111");
        DateTime end = new DateTime("2/2/2014", "2222");
        BlockDate bD = new BlockDate(start, end);
        assertEquals("Cloned start date", bD.getStartDate(), "1/1/2014");
        assertEquals("Cloned start time", bD.getStartTime(), "1111");
        assertEquals("Cloned end date", bD.getEndDate(), "2/2/2014");
        assertEquals("Cloned end time", bD.getEndTime(), "2222");
        bD.resetStart();
        assertEquals("Empty start date", bD.getStartDate(), "");
        assertEquals("Empty start time", bD.getStartTime(), "");
        assertEquals("Not empty end date", bD.getEndDate(), "2/2/2014");
        assertEquals("Not empty end time", bD.getEndTime(), "2222");
    }

    @Test
    public void testResetEnd() throws Exception {
        DateTime start = new DateTime("1/1/2014", "1111");
        DateTime end = new DateTime("2/2/2014", "2222");
        BlockDate bD = new BlockDate(start, end);
        assertEquals("Cloned start date", bD.getStartDate(), "1/1/2014");
        assertEquals("Cloned start time", bD.getStartTime(), "1111");
        assertEquals("Cloned end date", bD.getEndDate(), "2/2/2014");
        assertEquals("Cloned end time", bD.getEndTime(), "2222");
        bD.resetEnd();
        assertEquals("Not empty start date", bD.getStartDate(), "1/1/2014");
        assertEquals("Not empty start time", bD.getStartTime(), "1111");
        assertEquals("Empty end date", bD.getEndDate(), "");
        assertEquals("Empty end time", bD.getEndTime(), "");
    }

    @Test
    public void testResetEndDate() throws Exception {
        DateTime start = new DateTime("1/1/2014", "1111");
        DateTime end = new DateTime("2/2/2014", "2222");
        BlockDate bD = new BlockDate(start, end);
        assertEquals("Cloned start date", bD.getStartDate(), "1/1/2014");
        assertEquals("Cloned start time", bD.getStartTime(), "1111");
        assertEquals("Cloned end date", bD.getEndDate(), "2/2/2014");
        assertEquals("Cloned end time", bD.getEndTime(), "2222");
        bD.resetEndDate();
        assertEquals("Not empty start date", bD.getStartDate(), "1/1/2014");
        assertEquals("Not empty start time", bD.getStartTime(), "1111");
        assertEquals("Empty end date", bD.getEndDate(), "");
        assertEquals("Empty end time", bD.getEndTime(), "2222");
    }

    @Test
    public void testResetEndTime() throws Exception {
        DateTime start = new DateTime("1/1/2014", "1111");
        DateTime end = new DateTime("2/2/2014", "2222");
        BlockDate bD = new BlockDate(start, end);
        assertEquals("Cloned start date", bD.getStartDate(), "1/1/2014");
        assertEquals("Cloned start time", bD.getStartTime(), "1111");
        assertEquals("Cloned end date", bD.getEndDate(), "2/2/2014");
        assertEquals("Cloned end time", bD.getEndTime(), "2222");
        bD.resetEndTime();
        assertEquals("Not empty start date", bD.getStartDate(), "1/1/2014");
        assertEquals("Not empty start time", bD.getStartTime(), "1111");
        assertEquals("Empty end date", bD.getEndDate(), "2/2/2014");
        assertEquals("Empty end time", bD.getEndTime(), "");
    }

}