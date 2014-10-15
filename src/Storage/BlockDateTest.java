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
        assertEquals("Empty start date", "", bD.getStartDate());
        assertEquals("Empty start time", "", bD.getStartTime());
        assertEquals("Empty end date", "", bD.getEndDate());
        assertEquals("Empty end time", "", bD.getEndTime());
    }

    @Test
    public void testConstructorStoresDateTimeArgs() throws Exception {
        DateTime start = new DateTime("1/1/2014", "1111");
        DateTime end = new DateTime("2/2/2014", "2222");
        BlockDate bD = new BlockDate(start, end);
        assertEquals("Cloned start date", "1/1/2014", bD.getStartDate());
        assertEquals("Cloned start time", "1111", bD.getStartTime());
        assertEquals("Cloned end date", "2/2/2014", bD.getEndDate());
        assertEquals("Cloned end time", "2222", bD.getEndTime());
        start.resetDateTime();
        end.resetDateTime();
        assertEquals("Cloned start date by value", bD.getStartDate(),
                     "1/1/2014");
        assertEquals("Cloned start time by value", "1111", bD.getStartTime());
        assertEquals("Cloned end date by value", "2/2/2014", bD.getEndDate());
        assertEquals("Cloned end time by value", "2222", bD.getEndTime());
    }

    @Test
    public void testResetStartDate() throws Exception {
        DateTime start = new DateTime("1/1/2014", "1111");
        DateTime end = new DateTime("2/2/2014", "2222");
        BlockDate bD = new BlockDate(start, end);
        assertEquals("Cloned start date", "1/1/2014", bD.getStartDate());
        assertEquals("Cloned start time", "1111", bD.getStartTime());
        assertEquals("Cloned end date", "2/2/2014", bD.getEndDate());
        assertEquals("Cloned end time", "2222", bD.getEndTime());
        bD.resetStartDate();
        assertEquals("Empty start date", "", bD.getStartDate());
        assertEquals("Empty start time", "1111", bD.getStartTime());
        assertEquals("Not empty end date", "2/2/2014", bD.getEndDate());
        assertEquals("Not empty end time", "2222", bD.getEndTime());
    }

    @Test
    public void testResetStartTime() throws Exception {
        DateTime start = new DateTime("1/1/2014", "1111");
        DateTime end = new DateTime("2/2/2014", "2222");
        BlockDate bD = new BlockDate(start, end);
        assertEquals("Cloned start date", "1/1/2014", bD.getStartDate());
        assertEquals("Cloned start time", "1111", bD.getStartTime());
        assertEquals("Cloned end date", "2/2/2014", bD.getEndDate());
        assertEquals("Cloned end time", "2222", bD.getEndTime());
        bD.resetStartTime();
        assertEquals("Empty start date", "1/1/2014", bD.getStartDate());
        assertEquals("Empty start time", "", bD.getStartTime());
        assertEquals("Not empty end date", "2/2/2014", bD.getEndDate());
        assertEquals("Not empty end time", "2222", bD.getEndTime());
    }

    @Test
    public void testResetStart() throws Exception {
        DateTime start = new DateTime("1/1/2014", "1111");
        DateTime end = new DateTime("2/2/2014", "2222");
        BlockDate bD = new BlockDate(start, end);
        assertEquals("Cloned start date", "1/1/2014", bD.getStartDate());
        assertEquals("Cloned start time", "1111", bD.getStartTime());
        assertEquals("Cloned end date", "2/2/2014", bD.getEndDate());
        assertEquals("Cloned end time", "2222", bD.getEndTime());
        bD.resetStart();
        assertEquals("Empty start date", "", bD.getStartDate());
        assertEquals("Empty start time", "", bD.getStartTime());
        assertEquals("Not empty end date", "2/2/2014", bD.getEndDate());
        assertEquals("Not empty end time", "2222", bD.getEndTime());
    }

    @Test
    public void testResetEnd() throws Exception {
        DateTime start = new DateTime("1/1/2014", "1111");
        DateTime end = new DateTime("2/2/2014", "2222");
        BlockDate bD = new BlockDate(start, end);
        assertEquals("Cloned start date", "1/1/2014", bD.getStartDate());
        assertEquals("Cloned start time", "1111", bD.getStartTime());
        assertEquals("Cloned end date", "2/2/2014", bD.getEndDate());
        assertEquals("Cloned end time", "2222", bD.getEndTime());
        bD.resetEnd();
        assertEquals("Not empty start date", "1/1/2014", bD.getStartDate());
        assertEquals("Not empty start time", "1111", bD.getStartTime());
        assertEquals("Empty end date", "", bD.getEndDate());
        assertEquals("Empty end time", "", bD.getEndTime());
    }

    @Test
    public void testResetEndDate() throws Exception {
        DateTime start = new DateTime("1/1/2014", "1111");
        DateTime end = new DateTime("2/2/2014", "2222");
        BlockDate bD = new BlockDate(start, end);
        assertEquals("Cloned start date", "1/1/2014", bD.getStartDate());
        assertEquals("Cloned start time", "1111", bD.getStartTime());
        assertEquals("Cloned end date", "2/2/2014", bD.getEndDate());
        assertEquals("Cloned end time", "2222", bD.getEndTime());
        bD.resetEndDate();
        assertEquals("Not empty start date", "1/1/2014", bD.getStartDate());
        assertEquals("Not empty start time", "1111", bD.getStartTime());
        assertEquals("Empty end date", "", bD.getEndDate());
        assertEquals("Empty end time", "2222", bD.getEndTime());
    }

    @Test
    public void testResetEndTime() throws Exception {
        DateTime start = new DateTime("1/1/2014", "1111");
        DateTime end = new DateTime("2/2/2014", "2222");
        BlockDate bD = new BlockDate(start, end);
        assertEquals("Cloned start date", "1/1/2014", bD.getStartDate());
        assertEquals("Cloned start time", "1111", bD.getStartTime());
        assertEquals("Cloned end date", "2/2/2014", bD.getEndDate());
        assertEquals("Cloned end time", "2222", bD.getEndTime());
        bD.resetEndTime();
        assertEquals("Not empty start date", "1/1/2014", bD.getStartDate());
        assertEquals("Not empty start time", "1111", bD.getStartTime());
        assertEquals("Empty end date", "2/2/2014", bD.getEndDate());
        assertEquals("Empty end time", "", bD.getEndTime());
    }
}