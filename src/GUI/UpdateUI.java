package GUI;

import java.util.List;

import Storage.Task;

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
