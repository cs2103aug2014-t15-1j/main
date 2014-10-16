package GUI;

import java.util.List;

import Storage.Task;
/**
 * This classes calls all the 'update-able' user Interfaces ( Task Table, TaskList, Calendar ) to update themselves.
 * @author Sharon
 *
 */
public class UpdateUI {
    public UpdateUI(List<Task> tasks){
        update(tasks);
    }
    
    private void update(List<Task> tasks){
        new TaskTableUI(tasks);
        new TaskListUI();
        new CalendarUI();
    }
} 
