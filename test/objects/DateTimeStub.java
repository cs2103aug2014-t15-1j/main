package objects;


/*
 * This class is to be used for testing ONLY.
 * DateTimeStub allows dependency injection for DateTime Objects
 * NOTE: I have only implemented the constructor
 */
public class DateTimeStub extends DateTime {

    private String date = "";
    private int day = 0;
    private int month = 0;
    private int year = 0;
    private String time = "";

    public DateTimeStub(String date, String time) {
        super(date, time);
        this.date = date;
        this.time = time;
    }

    public DateTimeStub() {
    }
    
    public String getDate(){
        return date;
    }
    
    public String getTime(){
        return time;
    }
    
    @Override
    public String toString() {
        if (this.isEmpty()) {
            return "";
        } else if (date.isEmpty()) {
            return time;
        } else if (time.isEmpty()) {
            return date;
        } else {
            return date + " " + time;
        }
    }
}
