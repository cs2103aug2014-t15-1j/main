package GUI;

import java.util.List;

import logic.Processor;

import org.eclipse.jface.viewers.TableViewer;

import database.Task;
/*
 * This class updates the task table interface whenever a change is made
 */
public class TaskTableUI {
    
    public TaskTableUI(){
        update();
    }
    
    private void update(){
        TableViewer table = getTable();
       Processor processor = Processor.getInstance();
        List<Task> tasks = processor.fetchTimedTasks(); 
       if(!isValid(tasks)){
           return;
       }
        Object[] tasksArray = tasks.toArray();
        
        
        table.setInput(tasksArray);
        table.refresh();
    }
    
    private TableViewer getTable(){
        SetUp setUp = SetUp.getInstance();
        TableViewer table = setUp.getTableViewer();
        return table;
    }
    
    private boolean isValid(List<Task> tasks){
        if(tasks == null || tasks.isEmpty()){
            return false;
        }
        
        return true;
    }
}
