package database;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Unit tests for the BlockDate class.
 * 
 * @author Pierce Anderson Fu
 * 
 */

public class BlockDateTest {

    @Test
    public void testDefaultConstructorHasEmptyAttributes() {
        BlockDate bD = new BlockDate();
        assertEquals("Blank ID", 0, bD.getId());
        assertEquals("Empty start date", "", bD.getStartDate());
        assertEquals("Empty start time", "", bD.getStartTime());
        assertEquals("Empty end date", "", bD.getEndDate());
        assertEquals("Empty end time", "", bD.getEndTime());
    }

    @Test
    public void testConstructorStoresDateTimeArgs() {
        DateTime start = new DateTime("11/12/2014", "1111");
        DateTime end = new DateTime("13/12/2014", "2222");
        BlockDate bD = new BlockDate(start, end);
        assertEquals("Cloned start date", "11/12/2014", bD.getStartDate());
        assertEquals("Cloned start time", "1111", bD.getStartTime());
        assertEquals("Cloned end date", "13/12/2014", bD.getEndDate());
        assertEquals("Cloned end time", "2222", bD.getEndTime());
        start.resetDateTime();
        end.resetDateTime();
        assertEquals("Cloned start date by value", bD.getStartDate(),
                     "11/12/2014");
        assertEquals("Cloned start time by value", "1111", bD.getStartTime());
        assertEquals("Cloned end date by value", "13/12/2014", bD.getEndDate());
        assertEquals("Cloned end time by value", "2222", bD.getEndTime());
    }

    @Test
    public void testToString() {
        DateTime start = new DateTime("11/12/2014", "1111");
        DateTime end = new DateTime("13/12/2014", "2222");
        BlockDate bD = new BlockDate(start, end);

        assertEquals("Matches toString pattern",
                     "11/12/2014 1111 to 13/12/2014 2222", bD.toString());
    }

    /**
     * Is this end earlier, equal, later than that start
     */
    @Test
    public void testCompareTo() {
        DateTime start1 = new DateTime("11/12/2014", "1111");
        DateTime end1 = new DateTime("13/12/2014", "2222");
        BlockDate bD1 = new BlockDate(start1, end1);

        DateTime start2 = new DateTime("13/12/2014", "2223");
        DateTime end2 = new DateTime("13/12/2014", "2359");
        BlockDate bD2 = new BlockDate(start2, end2);

        assertTrue("First ends before second starts", bD1.compareTo(bD2) < 0);

        bD2.setStartTime("2222");
        assertTrue("First ends when second starts", bD1.compareTo(bD2) == 0);

        bD2.setStartTime("2221");
        assertTrue("First ends before second starts", bD1.compareTo(bD2) > 0);
    }

    /**
     * compares id, to facilitate sorting by id
     */
    @Test
    public void testCompare() {
        DateTime start = new DateTime("11/12/2014", "1111");
        DateTime end = new DateTime("13/12/2014", "2222");
        BlockDate bD1 = new BlockDate(start, end);
        BlockDate bD2 = new BlockDate(start, end);

        assertTrue("First ID is smaller than second", bD1.getId() < bD2.getId());

        assertTrue("First ID is smaller than second", bD1.compare(bD1, bD2) < 0);
        assertTrue("Second ID is later than first", bD1.compare(bD2, bD1) > 0);
        assertTrue("First ID is equal to self", bD1.compare(bD1, bD1) == 0);
    }

    /**
     * Checks if equals function is correct across the following cases:
     * <ul>
     * <li>Not equal to non-BlockDate types.
     * <li>Equals to self.
     * <li>Equals to another object with equal attribute values.
     * <li>Not equal to another object with different attribute values.
     * </ul>
     */
    @Test
    public void testEquals() {
        DateTime start1 = new DateTime("11/12/2014", "1111");
        DateTime end1 = new DateTime("13/12/2014", "2222");
        BlockDate bD1 = new BlockDate(start1, end1);
        BlockDate bD2 = new BlockDate(start1, end1);

        DateTime start2 = new DateTime("13/12/2014", "2223");
        DateTime end2 = new DateTime("13/12/2014", "2359");
        BlockDate bD3 = new BlockDate(start2, end2);

        assertFalse("Different types", bD1.equals(new Task()));
        assertTrue("Identical to self", bD1.equals(bD1));
        assertTrue("Equal values", bD1.equals(bD2));
        assertFalse("Not equal values", bD1.equals(bD3));
    }

    @Test
    public void testResetStartDate() {
        DateTime start = new DateTime("11/12/2014", "1111");
        DateTime end = new DateTime("13/12/2014", "2222");
        BlockDate bD = new BlockDate(start, end);
        assertEquals("Cloned start date", "11/12/2014", bD.getStartDate());
        assertEquals("Cloned start time", "1111", bD.getStartTime());
        assertEquals("Cloned end date", "13/12/2014", bD.getEndDate());
        assertEquals("Cloned end time", "2222", bD.getEndTime());
        bD.resetStartDate();
        assertEquals("Empty start date", "", bD.getStartDate());
        assertEquals("Empty start time", "1111", bD.getStartTime());
        assertEquals("Not empty end date", "13/12/2014", bD.getEndDate());
        assertEquals("Not empty end time", "2222", bD.getEndTime());
    }

    @Test
    public void testResetStartTime() {
        DateTime start = new DateTime("11/12/2014", "1111");
        DateTime end = new DateTime("13/12/2014", "2222");
        BlockDate bD = new BlockDate(start, end);
        assertEquals("Cloned start date", "11/12/2014", bD.getStartDate());
        assertEquals("Cloned start time", "1111", bD.getStartTime());
        assertEquals("Cloned end date", "13/12/2014", bD.getEndDate());
        assertEquals("Cloned end time", "2222", bD.getEndTime());
        bD.resetStartTime();
        assertEquals("Empty start date", "11/12/2014", bD.getStartDate());
        assertEquals("Empty start time", "", bD.getStartTime());
        assertEquals("Not empty end date", "13/12/2014", bD.getEndDate());
        assertEquals("Not empty end time", "2222", bD.getEndTime());
    }

    @Test
    public void testResetStart() {
        DateTime start = new DateTime("11/12/2014", "1111");
        DateTime end = new DateTime("13/12/2014", "2222");
        BlockDate bD = new BlockDate(start, end);
        assertEquals("Cloned start date", "11/12/2014", bD.getStartDate());
        assertEquals("Cloned start time", "1111", bD.getStartTime());
        assertEquals("Cloned end date", "13/12/2014", bD.getEndDate());
        assertEquals("Cloned end time", "2222", bD.getEndTime());
        bD.resetStart();
        assertEquals("Empty start date", "", bD.getStartDate());
        assertEquals("Empty start time", "", bD.getStartTime());
        assertEquals("Not empty end date", "13/12/2014", bD.getEndDate());
        assertEquals("Not empty end time", "2222", bD.getEndTime());
    }

    @Test
    public void testResetEnd() {
        DateTime start = new DateTime("11/12/2014", "1111");
        DateTime end = new DateTime("13/12/2014", "2222");
        BlockDate bD = new BlockDate(start, end);
        assertEquals("Cloned start date", "11/12/2014", bD.getStartDate());
        assertEquals("Cloned start time", "1111", bD.getStartTime());
        assertEquals("Cloned end date", "13/12/2014", bD.getEndDate());
        assertEquals("Cloned end time", "2222", bD.getEndTime());
        bD.resetEnd();
        assertEquals("Not empty start date", "11/12/2014", bD.getStartDate());
        assertEquals("Not empty start time", "1111", bD.getStartTime());
        assertEquals("Empty end date", "", bD.getEndDate());
        assertEquals("Empty end time", "", bD.getEndTime());
    }

    @Test
    public void testResetEndDate() {
        DateTime start = new DateTime("11/12/2014", "1111");
        DateTime end = new DateTime("13/12/2014", "2222");
        BlockDate bD = new BlockDate(start, end);
        assertEquals("Cloned start date", "11/12/2014", bD.getStartDate());
        assertEquals("Cloned start time", "1111", bD.getStartTime());
        assertEquals("Cloned end date", "13/12/2014", bD.getEndDate());
        assertEquals("Cloned end time", "2222", bD.getEndTime());
        bD.resetEndDate();
        assertEquals("Not empty start date", "11/12/2014", bD.getStartDate());
        assertEquals("Not empty start time", "1111", bD.getStartTime());
        assertEquals("Empty end date", "", bD.getEndDate());
        assertEquals("Empty end time", "2222", bD.getEndTime());
    }

    @Test
    public void testResetEndTime() {
        DateTime start = new DateTime("11/12/2014", "1111");
        DateTime end = new DateTime("13/12/2014", "2222");
        BlockDate bD = new BlockDate(start, end);
        assertEquals("Cloned start date", "11/12/2014", bD.getStartDate());
        assertEquals("Cloned start time", "1111", bD.getStartTime());
        assertEquals("Cloned end date", "13/12/2014", bD.getEndDate());
        assertEquals("Cloned end time", "2222", bD.getEndTime());
        bD.resetEndTime();
        assertEquals("Not empty start date", "11/12/2014", bD.getStartDate());
        assertEquals("Not empty start time", "1111", bD.getStartTime());
        assertEquals("Empty end date", "13/12/2014", bD.getEndDate());
        assertEquals("Empty end time", "", bD.getEndTime());
    }
}