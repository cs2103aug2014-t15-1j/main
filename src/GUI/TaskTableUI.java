package GUI;

import java.util.List;

import org.eclipse.jface.viewers.TableViewer;

import Logic.Processor;
import Storage.Task;
/*
 * This class updates the task table interface whenever a change is made
 */
public class TaskTableUI {
    
    public TaskTableUI(List<Task> tasks){
        update(tasks);
    }
    
    private void update(List<Task> tasks){
        TableViewer table = getTable();
       /* Processor processor = Processor.getInstance();
        List<Task> tasks = processor.fetchFloatingTasks(); **/
        Object[] tasksArray = tasks.toArray();
        
        
        table.setInput(tasksArray);
        table.refresh();
    }
    
    private TableViewer getTable(){
        SetUp setUp = SetUp.getInstance();
        TableViewer table = setUp.getTableViewer();
        return table;
    }
}
