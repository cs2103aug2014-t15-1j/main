package GUI;

import java.util.List;

import org.eclipse.swt.widgets.Label;

import Logic.Processor;
import Storage.Task;
/*
 * This class updates the TaskList Interface whenever a change is made. 
 */
public class TaskListUI {
    private static final String LIST_HEADER_SOMEDAY = "SOMEDAY";
    private static final String LIST_HEADER_UPCOMING = "UPCOMING";
    private static final String DOT_AND_SPACE = ". ";
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    
    public TaskListUI(){
        update();
    }
    
    private void update(){
        Label taskList = getTaskList();
        List<Task> upcomingTasks = getUpcomingTasks();
        List<Task> floatingTasks = getFloatingTasks();
        String upcomingList = getStringList(upcomingTasks, LIST_HEADER_UPCOMING); // to implement: overdue tasks in red
        
        String floatingList = getStringList(floatingTasks, LIST_HEADER_SOMEDAY);
        taskList.setText(upcomingList +  floatingList);
    }
  
    private List<Task> getUpcomingTasks(){
        Processor processor = Processor.getInstance();
      List<Task> tasks =   processor.fetchFloatingTasks();
      return tasks;
    }
    
    private List<Task> getFloatingTasks(){
        Processor processor = Processor.getInstance();
      List<Task> tasks =   processor.fetchFloatingTasks();
      return tasks;
    }
    
    private Label getTaskList(){
        SetUp setUp = SetUp.getInstance();
        Label taskList = setUp.getTaskList();
        return taskList;
    }
    
    private String getStringList(List<Task> tasks, String header){
        if(!isValid(tasks)){
            return "Nothing to display";
        }
        String list = LIST_HEADER_SOMEDAY + LINE_SEPARATOR;
        list = list + changeTaskListToString(tasks);
        return list;
    }
    
    private String changeTaskListToString(List<Task> tasks){
        int size = tasks.size();
        String list = "";
        for(int index = 0; index<size; index++){
            Task currentTask = tasks.get(index);
            String iD = currentTask.getId() + "";
            String name = currentTask.getName();
            list = list + iD + DOT_AND_SPACE + name + LINE_SEPARATOR;
        }
        
        return list;
    }
    
    private boolean isValid(List<Task> tasks){
        if(tasks.isEmpty()){
            return false;
        }
        return true;
    }
}
