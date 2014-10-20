package gui;

import java.util.Date;
import java.util.List;

import logic.Processor;

import org.eclipse.swt.widgets.DateTime;

import database.Task;
/*
 * This class updates the calendar interface whenever the change is made
 */
public class CalendarUI {

    public CalendarUI(){
        update();
    }
    
    private void update(){
        DateTime calendar = getCalendar();
        List<Task> tasks = getTasks();
        List<Date> dates = getDates(tasks);
        formatCalendar(calendar, dates) ;
    }
    
    private DateTime getCalendar(){
        SetUp setUp = SetUp.getInstance();
        DateTime calendar = setUp.getCalendar();
        return calendar;
    }
    
    private List<Task> getTasks(){
        Processor processor = Processor.getInstance();
        List<Task> tasks  = processor.fetchTimedTasks();
        return tasks;
    }
    
    private List<Date> getDates(List<Task> tasks){
        // TO DO
        return null;
    }
    
    private void formatCalendar(DateTime calendar, List<Date> dates){
        // TO DO
    }
    
}
