package objects;

import database.BlockDate;

/*
 * This class is to be used for testing ONLY.
 * BlockDateStub allows for dependency injection of BlockDate objects.
 * NOTE: Only the assessors and mutators have been implemented
 */

public class BlockDateStub extends BlockDate {
    private DateTimeStub start = new DateTimeStub();
    private DateTimeStub end = new DateTimeStub();

    public BlockDateStub(DateTimeStub start, DateTimeStub end) {
        super(start, end);
        setStart(start);
        setEnd(end);
    }

    public DateTimeStub getStart() {
        return this.start;
    }

    public DateTimeStub getEnd() {
        return this.end;
    }

    public void setStart(DateTimeStub start) {
        this.start = start;
    }

    public void setEnd(DateTimeStub end) {
        this.end = end;
    }

}
