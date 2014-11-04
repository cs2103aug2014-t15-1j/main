package logic;

import java.util.List;
import java.util.ArrayList;

import logic.Result.ResultType;
import parser.DateParser;
import parser.TaskParam;
import database.DateTime;
import database.Task;

public class CommandEdit extends Command {
    
    private String id = "";
    private String name = "";
    private DateTime due = new DateTime();
    private DateTime start = new DateTime();
    private List<String> tags = new ArrayList<String>();
    private List<String> delete = new ArrayList<String>();
    
    private static final String PARAM_ID = "id";
    private static final String PARAM_NAME = "name";
    private static final String PARAM_DUE = "due";
    private static final String PARAM_START = "start";
    private static final String PARAM_TAG = "tag";
    private static final String PARAM_DEL = "delete";

    public CommandEdit(List<TaskParam> content) {
        this.type = CommandType.EDIT;

        for (TaskParam param : content) {
            constructUsingParam(param);
        }
    }

    private void constructUsingParam(TaskParam param) {
        switch (param.getName()){
            case PARAM_ID:
                this.id = param.getField();
                break;
                
            case PARAM_NAME:
                this.name = param.getField();
                break;
                
            case PARAM_DUE:
                this.due = DateParser.parseToDateTime(param.getField());
                break;
                
            case PARAM_START:
                this.start = DateParser.parseToDateTime(param.getField());
                break;
                
            case PARAM_TAG:
                this.tags.add(param.getField());
                break;
                
            case PARAM_DEL:
                this.delete.add(param.getField());
                break;
                
            default:
                System.out.println("EDITor, we have a problem.");    
        }
    }
    
    @Override
    public String get(String paramName){
        switch(paramName){
            case PARAM_ID:
                return this.id;
                
            case PARAM_NAME:
                return this.name;
            
            case PARAM_START:
                return this.start.toString();

            case PARAM_DUE:
                return this.due.toString();
                
            default:
                System.out.println("Edit get's got a problem!");
                return null;
        }
    }
    
    @Override
    public List<String> getTags(){
        return this.tags;
    }
    
    /** Returns delete parameter names (Task fields to delete). */
    @Override
    public List<String> getDelete(){
        return this.delete;
    }
    
    /**
     * Executes "edit" operation
     * Allow edit/deletion of parameters of a Task
     * @return Result
     */
    @Override
    protected Result execute(boolean userInput) {
        if (Processor.LOGGING_ENABLED) {
            Processor.getLogger().info("Executing 'Edit' Command...");
        }
        Processor processor = Processor.getInstance();
        List<Task> list = new ArrayList<Task>();
        boolean success = false;
        
        int taskId = getTaskId();
        
        if (taskId > 0) {
            Task existingTask = processor.getFile().getTask(taskId);
            if (existingTask != null) {
                Task oldTask = new Task(existingTask);
                for (String d : delete) {
                    switch (d) {
                        case "name":
                            name = null;
                            break;
                        case "due":
                            due = null;
                            break;
                        case "start":
                            start = null;
                            break;
                        case "tags":
                            tags = null;
                            break;
                    }
                }
                success = processor.getFile().updateTaskInfo(existingTask, name, start, due, tags);
                if (success) {
                    performUpdate(list, oldTask, existingTask);
                }
            }
        }
        return new Result(list, success, getType(), ResultType.TASK);
    }
    
    private int getTaskId() {
        int taskId = 0;
        try {
            taskId = Integer.parseInt(id);
        } catch (Exception e) {
            if (Processor.LOGGING_ENABLED) {
                Processor.getLogger().warning("Invalid Task Id!");
            }
        }
        return taskId;
    }
    
    private void performUpdate(List<Task> list, Task oldTask, Task existingTask) {
        Processor processor = Processor.getInstance();
        processor.getEditedTaskHistory().push(oldTask);
        list.add(existingTask);
    }
    
    /** Undo the 'Edit' Command */
    @Override
    protected Result executeComplement() {
        Processor processor = Processor.getInstance();
        boolean success = false;
        List<Task> tasks = new ArrayList<Task>();
        
        Task prevTask = processor.getEditedTaskHistory().pop();
        
        String taskName = prevTask.getName();
        DateTime taskStart = prevTask.getStart();
        DateTime taskDue = prevTask.getDue();
        List<String> taskTags = prevTask.getTags();
        
        tasks.add(prevTask);
        success = processor.getFile().updateTaskInfo(prevTask.getId(), taskName, taskStart, taskDue, taskTags);
        return new Result(tasks, success, getType(), ResultType.TASK);
    }
}
