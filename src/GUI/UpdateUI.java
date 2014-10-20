package gui;

/**
 * This classes calls all the 'update-able' user Interfaces ( Task Table, TaskList, Calendar ) to update themselves.
 * @author Sharon
 *
 */
public class UpdateUI {
    public UpdateUI(){
        update();
    }
    
    private void update(){
        new TaskTableUI();
        new TaskListUI();
        new CalendarUI();
    }
} 
